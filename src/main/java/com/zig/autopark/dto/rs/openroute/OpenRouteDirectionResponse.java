package com.zig.autopark.dto.rs.openroute;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter @Setter
public class OpenRouteDirectionResponse {
    private String type;
    private List<Feature> features;
    private double[] bbox;

    public OpenRouteDirectionResponse(String type, List<Feature> features, double[] bbox) {
        this.type = type;
        this.features = features;
        this.bbox = bbox;
    }

    @Getter @Setter
    // Вложенный класс для объектов Feature
    public static class Feature {
        private String type;
        private Map<String, Object> properties;
        private Geometry geometry;

        public Feature(String type, Map<String, Object> properties, Geometry geometry) {
            this.type = type;
            this.properties = properties;
            this.geometry = geometry;
        }
    }

    @Getter @Setter
    // Вложенный класс для геометрических данных
    public static class Geometry {
        private String type;
        private List<List<Double>> coordinates;

        public Geometry(String type, List<List<Double>> coordinates) {
            this.type = type;
            this.coordinates = coordinates;
        }
    }
}