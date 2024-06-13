
package com.zig.autopark.service;

import com.zig.autopark.model.*;
import com.zig.autopark.repository.EnterpriseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class EnterpriseService {
    private final EnterpriseRepository repository;

    public Page<Enterprise> findAllByManager(Manager manager, Pageable paging) {
        return repository.findAllByManagersContains(manager, paging);
    }

    public List<Enterprise> findAll(Manager manager) {
        return repository.findAllByManagersContains(manager);
    }

    public List<Enterprise> findByIds(List<Long> ids) {
        return repository.findAllById(ids);
    }

    public Optional<Enterprise> findById(Long id) {
        return repository.findById(id);
    }

    public void save(Enterprise enterprise) {
        repository.save(enterprise);
    }

    public void update(Enterprise enterprise) {
        repository.save(enterprise);
    }

    public void deleteById(long id) {
        repository.deleteById(id);
    }
}