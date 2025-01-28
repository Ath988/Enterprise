package com.projectmanagementservice.repositories;

import com.projectmanagementservice.entities.Task;
import com.projectmanagementservice.entities.User;
import com.projectmanagementservice.entities.enums.EStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>
{
    List<User> findAllByNameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByNameAsc(String s, EStatus eStatus, long i, PageRequest of);

    Optional<User> findByIdAndAuthId(Long id, Long authId);

    Optional<User> findByEmailIgnoreCaseAndAuthIdAndStatusIsNot(String email, long l, EStatus eStatus);
}
