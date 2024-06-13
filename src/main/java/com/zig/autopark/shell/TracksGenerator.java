package com.zig.autopark.shell;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zig.autopark.dto.rs.openroute.OpenRouteDirectionResponse;
import com.zig.autopark.model.Track;
import com.zig.autopark.model.Vehicle;
import com.zig.autopark.service.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ShellComponent
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TracksGenerator {
    private final TrackService trackService;
    private final VehicleService vehicleService;
    private final WebClient webClient;
    private static final double EARTH_RADIUS = 6371;

    @ShellMethod(key = "tracks")
    public void generateTracks(@ShellOption(defaultValue = "spring") long vehicleId, double latitude, double longitude, double radius) {
        double[] coordinates = generateRandomCoordinates(latitude, longitude, radius);
        OpenRouteDirectionResponse route = callOpenRouteService(latitude, longitude, coordinates[0], coordinates[1]);
        fillAndSaveTrack(route, vehicleId);

    }

    public double[] generateRandomCoordinates(double centerLat, double centerLon, double radiusInKm) {
        Random random = new Random();

        // Генерация случайного угла для определения направления
        double angle = 2 * Math.PI * random.nextDouble();
        // Генерация случайного радиуса в пределах указанного радиуса
        double distance = Math.sqrt(random.nextDouble()) * radiusInKm;

        // Преобразование расстояния из километров в градусы
        double deltaLat = Math.toDegrees(distance / EARTH_RADIUS);
        double deltaLon = Math.toDegrees(distance / (EARTH_RADIUS * Math.cos(Math.toRadians(centerLat))));

        // Вычисление новой широты и долготы
        double newLat = centerLat + deltaLat * Math.cos(angle);
        double newLon = centerLon + deltaLon * Math.sin(angle);

        return new double[]{newLat, newLon};
    }

    private OpenRouteDirectionResponse callOpenRouteService(double sourceLatitude, double sourceLongitude, double targetLatitude, double targetLongitude) {
        String body = "{\"coordinates\":[[" + sourceLongitude + "," + sourceLatitude + "],[" + targetLongitude + "," + targetLatitude + "]]}";
        return webClient.post()
                .uri("https://api.openrouteservice.org/v2/directions/driving-car/geojson")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 5b3ce3597851110001cf624857d6d61de97a48f59b45d5f883027475")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(OpenRouteDirectionResponse.class)
                .block();
    }

    private void fillAndSaveTrack(OpenRouteDirectionResponse route, long vehicleId) {
        List<List<Double>> routeCoordinates = new ArrayList<>();
        route.getFeatures().stream().findFirst().ifPresent(feature -> {
            routeCoordinates.addAll(feature.getGeometry().getCoordinates());
        });

        Vehicle vehicle = vehicleService.findById(vehicleId).orElseThrow();
        LocalDateTime localDateTime = LocalDateTime.now();

        for (List<Double> coor : routeCoordinates) {
            GeometryFactory geometryFactory = new GeometryFactory();
            Coordinate coordinate = new Coordinate(coor.get(0), coor.get(1));
            Point point = geometryFactory.createPoint(coordinate);

            Track track = new Track();
            track.setVehicle(vehicle);
            track.setPt(point);
            track.setCreatedAt(localDateTime);
            trackService.save(track);
            localDateTime = localDateTime.plusSeconds(10);
            log.debug("Saved point: {}", point);
        }
    }
}