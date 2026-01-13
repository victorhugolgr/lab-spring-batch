package br.com.victorhugolgr.lab.service;

import org.springframework.stereotype.Service;

import br.com.victorhugolgr.lab.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository repository;

    public String getPropertyValueById(String id) {
        return repository.findById(id)
                .map(property -> property.getValue())
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }
}
