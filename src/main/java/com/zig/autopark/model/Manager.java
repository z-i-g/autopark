package com.zig.autopark.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "manager")
@Getter @Setter
public class Manager implements UserDetails {
    @Id @Column
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @ManyToMany
    @JoinTable(
            name = "enterprise_manager",
            joinColumns = @JoinColumn(name = "manager_id"),
            inverseJoinColumns = @JoinColumn(name = "enterprise_id")
    )
    private List<Enterprise> enterprises;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority grantedAuthority = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_MANAGER";
            }
        };
        return Arrays.asList(grantedAuthority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(
                    "dd/MM/uuuu HH:mm:ss"
            );

    @Transient
    private ZoneOffset zoneOffset;

    public Manager setTimeZoneOffset(String timeZoneOffset) {
        if (timeZoneOffset != null) {
            int offsetMinutes = Integer.valueOf(timeZoneOffset) * -1;
            this.zoneOffset = ZoneOffset.ofTotalSeconds(offsetMinutes * 60);
        }
        return this;
    }

    public String getFormattedDateTime(LocalDateTime dateTime) {
        if (dateTime == null)
            return null;
        if(zoneOffset != null) {
            OffsetDateTime serverOffsetDateTime = dateTime.atZone(
                    ZoneId.of("Europe/Berlin")
            ).toOffsetDateTime();
            OffsetDateTime clientOffsetDateTime = serverOffsetDateTime
                    .withOffsetSameInstant(zoneOffset);

            return DATE_TIME_FORMATTER.format(clientOffsetDateTime);
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }
}