package com.sivalabs.devzone.domain.repositories;

import com.sivalabs.devzone.domain.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query("update User u set u.imageUrl=?2 where u.id=?1")
    void updateImageUrl(Long id, String imageUrl);

    @Query("select u.imageUrl from User u where u.id=?1")
    String getImageUrl(Long userId);
}
