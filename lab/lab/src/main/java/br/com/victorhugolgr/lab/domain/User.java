package br.com.victorhugolgr.lab.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class User {
    @Id
    private Long id;
    private String name;
    private String email;
}
