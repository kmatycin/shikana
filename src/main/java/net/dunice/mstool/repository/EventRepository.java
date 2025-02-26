package net.dunice.mstool.repository;

import net.dunice.mstool.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EventRepository extends JpaRepository<EventEntity, UUID> {}
