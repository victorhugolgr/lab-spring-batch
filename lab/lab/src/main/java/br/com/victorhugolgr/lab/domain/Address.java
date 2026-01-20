package br.com.victorhugolgr.lab.domain;

import br.com.victorhugolgr.lab.dto.AddressRecord;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ADDRESS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    private String street;
    private String city;
    private String state;

    public static Address of(Address address) {
        return new Address(
                address.getId(),
                address.getUserId(),
                address.getStreet(),
                address.getCity(),
                address.getState()
        );
    }

    public static Address of(AddressRecord addressRecord) {
        return new Address(
                addressRecord.id(),
                addressRecord.userId(),
                addressRecord.street(),
                addressRecord.city(),
                addressRecord.state()
        );
    }
}
