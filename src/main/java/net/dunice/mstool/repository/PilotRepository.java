package net.dunice.mstool.repository;

import net.dunice.mstool.entity.PilotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PilotRepository extends JpaRepository<PilotEntity, String> {
    Optional<PilotEntity> findByNickname(String nickname);
    List<PilotEntity> findByNicknameContainingIgnoreCase(String nickname);
    List<PilotEntity> findByTeam(String team);
    List<PilotEntity> findByCountry(String country);
    List<PilotEntity> findByIsActive(Boolean isActive);
    boolean existsByNickname(String nickname);
    void deleteByNickname(String nickname);
} 