package net.dunice.mstool.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
public class EventEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String status;

    // Конструкторы
    public EventEntity() {}

    public EventEntity(String title, String date, String location, String status) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.status = status;
    }
}
