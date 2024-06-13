package com.zig.autopark.repository;

import com.zig.autopark.model.Enterprise;
import com.zig.autopark.model.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {
    List<Enterprise> findAllByManagersContains(Manager manager);

    Page<Enterprise> findAllByManagersContains(Manager manager, Pageable pageable);
}