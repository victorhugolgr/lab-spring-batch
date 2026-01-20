package br.com.victorhugolgr.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.victorhugolgr.lab.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
}
