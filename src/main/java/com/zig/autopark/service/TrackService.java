package com.zig.autopark.service;

import com.zig.autopark.dto.rq.TrackRqDto;
import com.zig.autopark.dto.rs.AbstractTrackDto;
import com.zig.autopark.dto.rs.TrackRsDto;
import com.zig.autopark.dto.rs.TrackRsGeoDto;
import com.zig.autopark.model.Manager;
import com.zig.autopark.model.Track;
import com.zig.autopark.model.Vehicle;
import com.zig.autopark.repository.TrackRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TrackService {
    private final TrackRepository repository;
    private final VehicleService vehicleService;

    public List<AbstractTrackDto> findAllGeoByVehicleAndTimeBetween(TrackRqDto rqDto) {
        Vehicle vehicle = vehicleService.findById(rqDto.getVehicleId()).orElseThrow();
        List<Track> tracks = repository.findAllByVehicleAndCreatedAtBetweenOrderByCreatedAt(vehicle, rqDto.getStartDateTime(), rqDto.getFinishDateTime());
        return tracks.stream().map(track -> {
            if (TrackRqDto.RsType.GEOJSON.equals(rqDto.getRsType()))
                return convertToGeoDto(track);
            return convertToDto(track);

        }).collect(Collectors.toList());
    }

    public List<Track> findAllByVehicleAndDateTimeBetween(Vehicle vehicle, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return repository.findAllByVehicleAndCreatedAtBetween(vehicle, startDateTime, endDateTime);
    }

    public Track findStartPointByVehicleAndStartDateTime(Vehicle vehicle, LocalDateTime startDateTime) {
        return repository.findByVehicleAndCreatedAtIs(vehicle, startDateTime).stream().findFirst().orElseThrow();
    }

    public Track findEndPointByVehicleAndStartDateTime(Vehicle vehicle, LocalDateTime endDateTime) {
        return repository.findByVehicleAndCreatedAtIs(vehicle, endDateTime).stream().findFirst().orElseThrow();
    }

    public void save(Track track) {
        repository.save(track);
    }

    public TrackRsGeoDto convertToGeoDto(Track track) {
        TrackRsGeoDto dto = new TrackRsGeoDto();
        Manager manager = (Manager) MultiUserDetailsService.getAuthUser();
        dto.setDateTime(manager.getFormattedDateTime(track.getCreatedAt()));
        dto.setPoint(track.getPt());
        return dto;
    }

    public TrackRsDto convertToDto(Track track) {
        TrackRsDto dto = new TrackRsDto();
        Manager manager = (Manager) MultiUserDetailsService.getAuthUser();
        dto.setId(track.getId());
        dto.setDateTime(manager.getFormattedDateTime(track.getCreatedAt()));
        dto.setPoint(track.getPt().toString());
        return dto;
    }
}