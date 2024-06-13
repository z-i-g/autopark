package com.zig.autopark.dto.rs;

public abstract class AbstractTrackDto {
    Long getId() {
        return null;
    }

    abstract String getDateTime();

    abstract <T> T getPoint();
}