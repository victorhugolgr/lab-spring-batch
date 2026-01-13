package br.com.victorhugolgr.lab.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PROPERTIES")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Property {
    @Id
    private String id;
    private String value;
    private String description;
}
