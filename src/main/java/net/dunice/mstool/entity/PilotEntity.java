package net.dunice.mstool.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pilots")
public class PilotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String realName;

    @Column(nullable = false)
    private String team;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String number;

    private Integer age;

    @Column(nullable = false)
    private String discipline; // Формула 1, Формула 2, Формула 3, GT3, GT4, DTM, WTCR и т.д.

    @Column(nullable = false)
    private String licenseLevel; // A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z

    private String titles; // JSON строка с титулами

    @Column(nullable = false)
    private Integer seasonWins;

    @Column(nullable = false)
    private Integer totalWins;

    @Column(nullable = false)
    private String cars; // JSON строка с машинами

    private String photoUrl;

    private Boolean isActive;

    private String status; // Active, Inactive, Retired
} 