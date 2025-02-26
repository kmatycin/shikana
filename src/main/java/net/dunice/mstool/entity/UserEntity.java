package net.dunice.mstool.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private String username;

    private String email;

    private String password;

    private String role; // SPECTATOR, PILOT, ORGANIZER

    private String avatarUrl;

    private String bio;

}
