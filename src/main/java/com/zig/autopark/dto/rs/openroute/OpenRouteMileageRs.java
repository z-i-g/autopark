package com.zig.autopark.dto.rs.openroute;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class OpenRouteMileageRs {
    private List<Route> routes;
    private List<Double> bbox;
    private Metadata metadata;

    @Getter @Setter
    public static class Route {
        private Summary summary;
        private List<Segment> segments;
        private List<Integer> way_points;
    }

    @Getter @Setter
    public static class Summary {
        private Double distance;
        private Double duration;
    }

    @Getter @Setter
    public static class Segment {
        private Double distance;
        private Double duration;
        private List<Step> steps;
    }

    @Getter @Setter
    public static class Step {
        private Double distance;
        private Double duration;
        private int type;
        private String instruction;
        private String name;
        private List<Integer> way_points;
    }

    @Getter @Setter
    public static class Metadata {
        private String attribution;
        private String service;
        private long timestamp;
        private Query query;
        private Engine engine;
    }

    @Getter @Setter
    public static class Query {
        private List<List<Double>> coordinates;
        private String profile;
        private String format;
    }

    @Getter @Setter
    public static class Engine {
        private String version;
        private String build_date;
        private String graph_date;
    }
}