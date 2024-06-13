package com.zig.autopark.service;

import com.zig.autopark.dto.rs.geo.LocationIQReverseResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class GeoCodingInteractionService {

    private final WebClient webClient;
    public LocationIQReverseResponse call(Point startPoint) {
        Coordinate coordinate = startPoint.getCoordinate();
        Map<String, Object> params = new HashMap<>();
        params.put("key", "pk.44dc2530f704b8db39d2711942d32f7f");
        params.put("lat", coordinate.y);
        params.put("lon", coordinate.x);

        LocationIQReverseResponse response = webClient.get()
                .uri("https://us1.locationiq.com/v1/reverse?key=pk.44dc2530f704b8db39d2711942d32f7f&lat=" +coordinate.y + "&lon=" + coordinate.x + "&format=json&")
                .header("accept", "application/json")
                .retrieve()
                .bodyToMono(LocationIQReverseResponse.class)
                .block();
        return response;
    }
}