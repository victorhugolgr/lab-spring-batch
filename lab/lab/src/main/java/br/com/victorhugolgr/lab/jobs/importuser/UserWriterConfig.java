package br.com.victorhugolgr.lab.jobs.importuser;

import javax.sql.DataSource;

import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.victorhugolgr.lab.dto.UserDTO;

@Configuration
public class UserWriterConfig {

    @Bean
    public JdbcBatchItemWriter<UserDTO> writer(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<UserDTO>()
                .dataSource(dataSource)
                .sql("INSERT INTO USERS (NAME, EMAIL) VALUES (:name, :email)")
                .beanMapped()
                .build();
    }
}
