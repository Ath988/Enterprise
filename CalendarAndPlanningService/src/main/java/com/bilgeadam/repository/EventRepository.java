package com.businessapi.repository;

import com.businessapi.entity.Event;
import com.businessapi.entity.enums.EStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

   Optional<List<Event>> findAllByUserIdAndStatus(Long userId, EStatus eStatus);
}
