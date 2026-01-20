package br.com.victorhugolgr.lab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.victorhugolgr.lab.domain.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{
    Optional<Address> findByUserId(Long userId);
}
