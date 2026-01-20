package br.com.victorhugolgr.lab.jobs.importuser;

import org.springframework.batch.infrastructure.item.file.mapping.FieldSetMapper;
import org.springframework.batch.infrastructure.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import br.com.victorhugolgr.lab.dto.AddressRecord;

public class AddressFieldSetMapper implements FieldSetMapper<AddressRecord>{

    @Override
    public AddressRecord mapFieldSet(FieldSet fieldSet) throws BindException {
        return new AddressRecord(
            fieldSet.readLong("id"),
            fieldSet.readLong("userId"),
            fieldSet.readString("streat"),
            fieldSet.readString("city"),
            fieldSet.readString("state")
        );
    }
}
