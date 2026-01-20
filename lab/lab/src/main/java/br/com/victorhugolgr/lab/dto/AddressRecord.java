package br.com.victorhugolgr.lab.dto;

public record AddressRecord(
        Long id,
        Long userId,
        String street,
        String city,
        String state) {
}
