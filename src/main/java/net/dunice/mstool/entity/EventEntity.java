package net.dunice.mstool.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.dunice.mstool.constants.EventStatus;
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

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String date;

    @Column
    private String location;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private boolean isExternal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status = EventStatus.UPCOMING;

    @Column
    private String game;

    @Column
    private String stages;

    @Column
    private String organizer;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private UserEntity createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private UserEntity updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
