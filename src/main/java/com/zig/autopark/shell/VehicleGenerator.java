package com.zig.autopark.shell;

import com.github.javafaker.Faker;
import com.zig.autopark.model.*;
import com.zig.autopark.service.BrandService;
import com.zig.autopark.service.DriverService;
import com.zig.autopark.service.EnterpriseService;
import com.zig.autopark.service.VehicleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@ShellComponent
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class VehicleGenerator {

    private final EnterpriseService enterpriseService;
    private final VehicleService vehicleService;
    private final BrandService brandService;
    private final DriverService driverService;


    @ShellMethod(key = "vehicles")
    public void generateVehicles(@ShellOption(defaultValue = "spring") List<Long> entIdS, int vehicleCount, int driversCount) {
        Random random = new Random();
        List<Vehicle> vehicles = new ArrayList<>();
        List<Driver> drivers = new ArrayList<>();
        List<Enterprise> enterprises = enterpriseService.findByIds(entIdS);
        enterprises.forEach(enterprise -> {
            vehicles.addAll(generateVehicles(enterprise, vehicleCount));
            drivers.addAll(generateDrivers(enterprise, driversCount));
        });

        if (vehicles.size() >= 10) {
            for (int i = 0; i < vehicles.size(); i++) {
                Driver randomDriver = drivers.get(random.nextInt(drivers.size()));
                if (i % 10 == 0) {
                    randomDriver.setActiveInVehicle(vehicles.get(i));
                    List<Vehicle> v = new ArrayList<>();
                    v.add(vehicles.get(i));
                    randomDriver.setVehicles(v);
                    List<Driver> d = new ArrayList<>();
                    d.add(randomDriver);
                    vehicles.get(i).setDrivers(d);
                }
            }
        }

        vehicleService.saveAllEntities(vehicles);
        driverService.saveAllEntities(drivers);
    }

    private List<Vehicle> generateVehicles(Enterprise enterprise, int vehicleCount) {
        Faker faker = new Faker(new Locale("RU"));
        List<Vehicle> vehicles = new ArrayList<>();
        List<Brand> brandDtos = brandService.findAllEntities();
        for (int i = 0; i < vehicleCount; i++) {
            Vehicle vehicle = new Vehicle();
            vehicle.setEnterprise(enterprise);
            vehicle.setMileage(String.valueOf(faker.number().randomNumber()));
            vehicle.setPrice(BigDecimal.valueOf(faker.number().randomDouble(2, 1000, 10_000_000)));

            Date randomDate = faker.date().between(Date.from(Instant.parse("1980-01-01T10:15:30.00Z")), Date.from(Instant.now()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(randomDate);
            int randomYear = calendar.get(Calendar.YEAR);

            vehicle.setYear(String.valueOf(randomYear));
            vehicle.setBrand(brandDtos.get(new Random().nextInt(brandDtos.size())));
            vehicles.add(vehicle);
        }
        return vehicles;
    }

    private List<Driver> generateDrivers(Enterprise enterprise, int driverCount) {
        Faker faker = new Faker(new Locale("RU"));
        int currentYear = LocalDate.now().getYear();
        List<Driver> drivers = new ArrayList<>();
        for (int i = 0; i < driverCount; i++) {
            Driver driver = new Driver();
            driver.setEnterprise(enterprise);
            driver.setName(faker.name().firstName());
            driver.setAge(currentYear - faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear());
            driver.setSalary(BigDecimal.valueOf(faker.number().randomDouble(2, 45_000, 250_000)));
            driver.setExperience(faker.number().numberBetween(0, driver.getAge() - 18));
            drivers.add(driver);
        }
        return drivers;
    }
}