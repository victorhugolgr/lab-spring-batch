package br.com.victorhugolgr.lab.jobs.importuser;

import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.victorhugolgr.lab.domain.Address;
import br.com.victorhugolgr.lab.repository.AddressRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AddressWriterConfig {

    private final AddressRepository addressRepository;

    @Bean
    public ItemWriter<Address> addresItemWriter() {
        RepositoryItemWriter<Address> writer = new RepositoryItemWriter<>(addressRepository);
        writer.setMethodName("save");
        return writer;
    }
}
