package br.com.victorhugolgr.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.victorhugolgr.lab.domain.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property, String>{
}
