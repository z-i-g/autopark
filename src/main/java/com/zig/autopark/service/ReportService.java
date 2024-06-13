package com.zig.autopark.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zig.autopark.dto.ReportCreateDto;
import com.zig.autopark.dto.rq.ReportDto;
import com.zig.autopark.dto.rs.openroute.CoordinatesDTO;
import com.zig.autopark.dto.rs.openroute.OpenRouteMileageRs;
import com.zig.autopark.model.*;
import com.zig.autopark.repository.CarMileageReportRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ReportService {
    private final CarMileageReportRepository carMileageReportRepository;
    private final VehicleService vehicleService;
    private final TrackService trackService;
    private final OpenRouteInteractionService openRouteInteractionService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public List<ReportDto> getOrGenerateAndSave(ReportCreateDto rqDto) {
        switch (rqDto.getType()) {
            case "carMileageReport" -> {
                return executeCarMileageReport(rqDto);
            }
            default -> {
                return List.of();
            }
        }
    }

    @SneakyThrows
    private List<ReportDto> executeCarMileageReport(ReportCreateDto rqDto) {
        Vehicle vehicle = vehicleService.findById(rqDto.getVehicleId()).orElseThrow();
        List<CarMileageReport> reports = carMileageReportRepository.findByVehicleIdAndStartDateEqualsAndAndEndDateEqualsAndPeriodEquals(vehicle.getId(), rqDto.getStartDate(), rqDto.getEndDate(), rqDto.getPeriod());
        if (!reports.isEmpty())
            return reports.stream().map(this::convertToReportDto).collect(Collectors.toList());

        List<Track> tracks = trackService.findAllByVehicleAndDateTimeBetween(vehicle, rqDto.getStartDate().atStartOfDay(), rqDto.getEndDate().atStartOfDay());

        Map<String, List<List<Double>>> map = tracks.stream()
                .collect(Collectors.groupingBy(
                        data -> convertLocalDateTimeByPeriod(data.getCreatedAt(), rqDto.getPeriod()),
                        Collectors.mapping(value -> {
                            List<Double> coordinate = new ArrayList<>();
                            coordinate.add(value.getPt().getX());
                            coordinate.add(value.getPt().getY());
                            return coordinate;
                        }, Collectors.toList()))
                );

        Map<String, Double> mileageMap = new HashMap<>();
        map.forEach((key, val) -> {
            List<List<List<Double>>> chunkList = splitListIntoSublists(val, 70);

            List<OpenRouteMileageRs>  mileageRsList= chunkList.stream()
                    .map(list -> {
                        CoordinatesDTO coordinatesDTO = new CoordinatesDTO();
                        coordinatesDTO.setCoordinates(list);
                        return coordinatesDTO;
                    })
                    .map(openRouteInteractionService::call)
                    .toList();

            Double mileageSum = mileageRsList.stream()
                    .map(rs -> rs.getRoutes().get(0).getSummary().getDistance())
                    .reduce(0.0, Double::sum);
            mileageMap.put(key, mileageSum);
        });

        CarMileageReport carMileageReport = new CarMileageReport();
        carMileageReport.setVehicle(vehicle);
        carMileageReport.setStartDate(rqDto.getStartDate());
        carMileageReport.setEndDate(rqDto.getEndDate());
        carMileageReport.setPeriod(rqDto.getPeriod());
        carMileageReport.setName(rqDto.getType() + " " + vehicle.getId());
        carMileageReport.setResult(objectMapper.writeValueAsString(mileageMap));
        carMileageReportRepository.save(carMileageReport);

        return List.of(convertToReportDto(carMileageReport));
    }


    private String convertLocalDateTimeByPeriod(LocalDateTime dateTime, Period period) {
        LocalDate date = dateTime.toLocalDate();
        switch (period) {
            case DAY -> {
                return String.format("%s.%s.%s", date.getDayOfMonth(), date.getMonthValue(), date.getYear());
            }
            case MONTH -> {
                return String.format("%s.%s", date.getMonthValue(), date.getYear());
            }
            case YEAR -> {
                return String.format("%s", date.getYear());
            }
            default -> throw new IllegalArgumentException();
        }
    }

    public static List<List<List<Double>>> splitListIntoSublists(List<List<Double>> originalList, int chunkSize) {
        List<List<List<Double>>> listOfChunkLists = new ArrayList<>();
        List<List<Double>> currentChunk = new ArrayList<>();
        int counter = 0;

        for (List<Double> element : originalList) {
            currentChunk.add(element);
            counter++;
            if (counter == chunkSize) {
                listOfChunkLists.add(new ArrayList<>(currentChunk));
                currentChunk.clear();
                counter = 0;
            }
        }
        if (!currentChunk.isEmpty()) {
            listOfChunkLists.add(currentChunk);
        }
        return listOfChunkLists;
    }

    private ReportDto convertToReportDto(CarMileageReport carMileageReport) {
        ReportDto reportDto = new ReportDto();
        reportDto.setName(carMileageReport.getName());
        reportDto.setVehicleRegistrationNumber(carMileageReport.getVehicle().getRegistrationNumber());
        reportDto.setPeriod(carMileageReport.getPeriod());
        reportDto.setMileage(carMileageReport.getResult());
        return reportDto;
    }
}