package net.dunice.mstool.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PilotResponse {
    private String id;
    private String nickname;
    private String realName;
    private String team;
    private String country;
    private String city;
    private String number;
    private Integer age;
    private String discipline;
    private String licenseLevel;
    private String titles;
    private Integer seasonWins;
    private Integer totalWins;
    private String cars;
    private String photoUrl;
    private Boolean isActive;
    private String status;
} 