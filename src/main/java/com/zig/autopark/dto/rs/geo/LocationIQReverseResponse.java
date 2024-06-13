package com.zig.autopark.dto.rs.geo;

import java.util.List;

public class LocationIQReverseResponse {
    private String place_id;
    private String licence;
    private String osm_type;
    private String osm_id;
    private double lat;
    private double lon;
    private String display_name;
    private Address address;
    private List<String> boundingbox;

    public LocationIQReverseResponse(String place_id, String licence, String osm_type, String osm_id, double lat, double lon, String display_name, Address address, List<String> boundingbox) {
        this.place_id = place_id;
        this.licence = licence;
        this.osm_type = osm_type;
        this.osm_id = osm_id;
        this.lat = lat;
        this.lon = lon;
        this.display_name = display_name;
        this.address = address;
        this.boundingbox = boundingbox;
    }

    // Геттеры и сеттеры для всех полей

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getOsm_type() {
        return osm_type;
    }

    public void setOsm_type(String osm_type) {
        this.osm_type = osm_type;
    }

    public String getOsm_id() {
        return osm_id;
    }

    public void setOsm_id(String osm_id) {
        this.osm_id = osm_id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getBoundingbox() {
        return boundingbox;
    }

    public void setBoundingbox(List<String> boundingbox) {
        this.boundingbox = boundingbox;
    }

    // Вложенный класс для поля address
    public static class Address {
        private String house_number;
        private String road;
        private String city;
        private String state;
        private String country;
        private String postcode;

        public Address(String house_number, String road, String city, String state, String country, String postcode) {
            this.house_number = house_number;
            this.road = road;
            this.city = city;
            this.state = state;
            this.country = country;
            this.postcode = postcode;
        }

        // Геттеры и сеттеры для всех полей класса Address

        public String getHouse_number() {
            return house_number;
        }

        public void setHouse_number(String house_number) {
            this.house_number = house_number;
        }

        public String getRoad() {
            return road;
        }

        public void setRoad(String road) {
            this.road = road;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }
    }
}