package br.com.victorhugolgr.lab.jobs.importuser;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.file.MultiResourceItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.victorhugolgr.lab.domain.Address;
import br.com.victorhugolgr.lab.dto.AddressRecord;
import br.com.victorhugolgr.lab.dto.UserDTO;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ImportUsersJobConfig {

    private final FileMovementStepExecutionListener fileMovementListener;

    @Bean
    public Step importUserStep(JobRepository jobRepository,
            MultiResourceItemReader<UserDTO> reader, UserItemProcessor processor, JdbcBatchItemWriter<UserDTO> writer) {
        return new StepBuilder("csv-to-db-step", jobRepository)
                .<UserDTO, UserDTO>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(fileMovementListener)
                .build();
    }

    public Step importAddressStep(MultiResourceItemReader<AddressRecord> addressReader,
        JobRepository jobRepository,
            AddressItemProcessor addressProcessor,
            ItemWriter<Address> addressWriter) {
                return new StepBuilder("address-csv-to-db-step", jobRepository)
                .<AddressRecord, Address>chunk(10)
                .reader(addressReader)
                .processor(addressProcessor)
                .writer(addressWriter)
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, Step importUserStep, Step importAddressStep) {
        return new JobBuilder("importUserJob", jobRepository)
                .start(importUserStep)
                .next(importAddressStep)
                .build();
    }

}
