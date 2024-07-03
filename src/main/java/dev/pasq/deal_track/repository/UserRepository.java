package dev.pasq.deal_track.repository;

import dev.pasq.deal_track.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
