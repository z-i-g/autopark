package com.zig.autopark.service;

import com.zig.autopark.dto.rs.openroute.CoordinatesDTO;
import com.zig.autopark.dto.rs.openroute.OpenRouteMileageRs;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class OpenRouteInteractionService {

    private final WebClient webClient;

    @SneakyThrows
    public OpenRouteMileageRs call(CoordinatesDTO coordinatesDTO) {
        Thread.sleep(5000);
        OpenRouteMileageRs response = webClient.post()
                .uri("https://api.openrouteservice.org/v2/directions/driving-car/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 5b3ce3597851110001cf624857d6d61de97a48f59b45d5f883027475")
                .bodyValue(coordinatesDTO)
                .retrieve()
                .bodyToMono(OpenRouteMileageRs.class)
                .block();
        return response;
    }
}