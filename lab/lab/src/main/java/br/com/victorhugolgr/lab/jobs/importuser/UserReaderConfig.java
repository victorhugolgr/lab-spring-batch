package br.com.victorhugolgr.lab.jobs.importuser;

import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import br.com.victorhugolgr.lab.dto.User;

@Configuration
public class UserReaderConfig {

    @Bean
    public FlatFileItemReader<User> reader() {
        return new FlatFileItemReaderBuilder<User>()
                .name("userItemReader")
                .resource(new ClassPathResource("users.csv"))
                .delimited()
                .names("id", "name", "email")
                .linesToSkip(1)
                .targetType(User.class)
                .build();
    }
}
