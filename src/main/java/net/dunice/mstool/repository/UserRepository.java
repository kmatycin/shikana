package net.dunice.mstool.repository;

import net.dunice.mstool.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByUsername(String username);

    List<UserEntity> findByUsernameContainingIgnoreCase(String username);

    Optional<UserEntity> findByUsername(String username);
}
