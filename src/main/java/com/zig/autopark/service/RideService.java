package com.zig.autopark.service;

import com.zig.autopark.dto.rq.RidePointsRqDto;
import com.zig.autopark.dto.rs.RideInfoDto;
import com.zig.autopark.dto.rs.RideInfoRsDto;
import com.zig.autopark.dto.rs.RidePointsRsDto;
import com.zig.autopark.dto.rs.geo.LocationIQReverseResponse;
import com.zig.autopark.model.Manager;
import com.zig.autopark.model.Ride;
import com.zig.autopark.model.Track;
import com.zig.autopark.model.Vehicle;
import com.zig.autopark.repository.RideRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class RideService {
    private final RideRepository repository;
    private final VehicleService vehicleService;
    private final TrackService trackService;
    private final GeoCodingInteractionService geoCodingInteractionService;

    public List<RidePointsRsDto> findAllRidePointsBetweenDateTime(RidePointsRqDto rqDto) {
        List<Ride> rides = repository.findAllByStartDateTimeIsGreaterThanEqualAndEndDateTimeLessThanEqual(rqDto.getStartDateTime(), rqDto.getEndDateTime());

        Map<Vehicle, List<Ride>> ridesByVehicle = rides.stream().collect(Collectors.groupingBy(Ride::getVehicle));
        List<RidePointsRsDto> ridePointsRsDtos = ridesByVehicle.entrySet().stream().map((entry) -> {
            LocalDateTime earliestDateTime = entry.getValue().stream().map(Ride::getStartDateTime).filter(Objects::nonNull).min(LocalDateTime::compareTo).orElseThrow();
            LocalDateTime latestDateTime = entry.getValue().stream().map(Ride::getEndDateTime).filter(Objects::nonNull).max(LocalDateTime::compareTo).orElseThrow();
            return trackService.findAllByVehicleAndDateTimeBetween(entry.getKey(), earliestDateTime, latestDateTime);
        }).flatMap(Collection::stream).map(this::convertRidePointsToDto).toList();

        return ridePointsRsDtos;
    }

    public List<RideInfoRsDto> findAllRidesBetweenDateTime(RidePointsRqDto rqDto) {
        List<Ride> rides = repository.findAllByStartDateTimeIsGreaterThanEqualAndEndDateTimeLessThanEqual(rqDto.getStartDateTime(), rqDto.getEndDateTime());
        List<RideInfoRsDto> rideInfoRsDtos = rides.stream()
                .map(ride -> {
                    Track startPoint = trackService.findStartPointByVehicleAndStartDateTime(ride.getVehicle(), ride.getStartDateTime());
                    Track endPoint = trackService.findEndPointByVehicleAndStartDateTime(ride.getVehicle(), ride.getEndDateTime());
                    return convertAndFillRidesToDto(startPoint, endPoint, ride);
                })
                .collect(Collectors.toList());
        return rideInfoRsDtos;
    }

    public List<List<List<Double>>> findAllRidesById(List<Long> rideIds) {
        List<Ride> rides = repository.findAllByIdIn(rideIds);

        return rides.stream()
                .map(ride -> {
                    List<Track> tracks = trackService.findAllByVehicleAndDateTimeBetween(ride.getVehicle(), ride.getStartDateTime(), ride.getEndDateTime());
                    List<List<Double>> route = new ArrayList<>();
                    tracks.forEach(track -> route.add(List.of(track.getPt().getY(), track.getPt().getX())));
                    return route;
                })
                .collect(Collectors.toList());
    }

    public List<RideInfoDto> findAllRidesByVehicleAndBetweenDateTimeAndConvertToDto(Long vehicleId, String fromDate, String toDate) {
        LocalDateTime startDateTime = LocalDate.parse(fromDate).atStartOfDay();
        LocalDateTime endDateTime = LocalDate.parse(toDate).atStartOfDay();
        Vehicle vehicle = vehicleService.findById(vehicleId).orElseThrow();
        List<Ride> rides = repository.findAllByVehicleAndStartDateTimeIsGreaterThanEqualAndEndDateTimeLessThanEqual(vehicle, startDateTime, endDateTime);
        return rides.stream().map(this::convertToRideDto).collect(Collectors.toList());
    }

    private RidePointsRsDto convertRidePointsToDto(Track track) {
        RidePointsRsDto dto = new RidePointsRsDto();
        Manager manager = (Manager) MultiUserDetailsService.getAuthUser();
        dto.setDateTime(manager.getFormattedDateTime(track.getCreatedAt()));
        dto.setVehicleId(track.getVehicle().getId());
        dto.setPoint(track.getPt());
        return dto;
    }

    private RideInfoDto convertToRideDto(Ride ride) {
        RideInfoDto dto = new RideInfoDto();
        dto.setId(ride.getId());
        dto.setVehicleId(ride.getVehicle().getId());
        Manager manager = (Manager) MultiUserDetailsService.getAuthUser();
        dto.setStartDateTime(manager.getFormattedDateTime(ride.getStartDateTime()));
        return dto;
    }

    @SneakyThrows
    private RideInfoRsDto convertAndFillRidesToDto(Track startTrackPoint, Track endTrackPoint, Ride ride) {
        RideInfoRsDto dto = new RideInfoRsDto();
        Manager manager = (Manager) MultiUserDetailsService.getAuthUser();
        dto.setVehicleId(startTrackPoint.getVehicle().getId());
        dto.setDuration(Duration.between(startTrackPoint.getCreatedAt(), endTrackPoint.getCreatedAt()).toMinutes());
        dto.setDistance(ride.getDistance());
        dto.setStartDateTime(manager.getFormattedDateTime(startTrackPoint.getCreatedAt()));
        dto.setEndDateTime(manager.getFormattedDateTime(endTrackPoint.getCreatedAt()));
        dto.setStartPoint(startTrackPoint.getPt());
        dto.setEndPoint(endTrackPoint.getPt());

        LocationIQReverseResponse startGeoCoding = geoCodingInteractionService.call(startTrackPoint.getPt());
        Thread.sleep(2000l);
        LocationIQReverseResponse endGeoCoding = geoCodingInteractionService.call(endTrackPoint.getPt());
        dto.setStartAddress(startGeoCoding.getDisplay_name());
        dto.setEndAddress(endGeoCoding.getDisplay_name());

        return dto;
    }
}