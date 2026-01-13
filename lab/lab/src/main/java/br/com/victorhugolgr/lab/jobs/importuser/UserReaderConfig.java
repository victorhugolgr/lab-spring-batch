package br.com.victorhugolgr.lab.jobs.importuser;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.MultiResourceItemReader;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import br.com.victorhugolgr.lab.dto.User;
import br.com.victorhugolgr.lab.service.PropertyService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UserReaderConfig {

    private final PropertyService propertyService;
    
    private static final String PATH_CSV = "PATH_CSV";

    @Bean
    public MultiResourceItemReader<User> multiResourceItemReader() {
        MultiResourceItemReader<User> reader = new MultiResourceItemReader<>(csvFileItemReader());
        reader.setResources(getCsvResources());
        return reader;
    }

@Bean
    public FlatFileItemReader<User> csvFileItemReader() {
        var linearMapper = new DefaultLineMapper<User>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames("id", "name", "email");
                setDelimiter(",");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<User>() {{
                setTargetType(User.class);
            }});
        }};
        FlatFileItemReader<User> reader = new FlatFileItemReader<>(linearMapper);
        reader.setLinesToSkip(1); // Skip header
        return reader;
    }

    private Resource[] getCsvResources() {
        var csvPath = propertyService.getPropertyValueById(PATH_CSV);
        var folder = new File(csvPath);

        if(!folder.exists() || !folder.isDirectory()) {
            throw new RuntimeException("CSV path is invalid");
        }

        var csvFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

        if(csvFiles == null || csvFiles.length == 0) {
            throw new RuntimeException("No CSV files found in the specified path");
        }

        List<Resource> resources = Arrays.stream(csvFiles)
                .map(file -> new FileSystemResource(file))
                .collect(Collectors.toList());

        return resources.toArray(new Resource[0]);
    }
}
