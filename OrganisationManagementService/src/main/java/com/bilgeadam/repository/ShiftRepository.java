package com.bilgeadam.repository;

import com.bilgeadam.entity.Shift;
import com.bilgeadam.entity.enums.EState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findAllByState(EState state);

}

