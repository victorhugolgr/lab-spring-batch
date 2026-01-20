package br.com.victorhugolgr.lab.jobs.importuser;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.MultiResourceItemReader;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import br.com.victorhugolgr.lab.dto.AddressRecord;
import br.com.victorhugolgr.lab.service.PropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AddressReaderConfig {

    private final PropertyService propertyService;

    private static final String PATH_ADDRESS_CSV = "PATH_ADDRESS_CSV";

    @Bean
    public MultiResourceItemReader<AddressRecord> multiAddressResourceItemReader() {
        MultiResourceItemReader<AddressRecord> reader = new MultiResourceItemReader<>(addressCsvFileItemReader());
        reader.setResources(getAddressCsvResources());
        return reader;
    }

    private Resource[] getAddressCsvResources() {
        var csvPath = propertyService.getPropertyValueById(PATH_ADDRESS_CSV);
        var folder = new File(csvPath);

        if (!folder.exists() || !folder.isDirectory()) {
            log.warn("⚠️ O caminho especificado não é uma pasta válida: {}", csvPath);
            return new Resource[0];
        }

        var csvFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

        if (csvFiles == null || csvFiles.length == 0) {
            log.warn("⚠️ Nenhum arquivo CSV encontrado na pasta: {}", csvPath);
            return new Resource[0];
        }

        log.info("✅ {} arquivos CSV encontrados na pasta: {}", csvFiles.length, csvPath);

        List<FileSystemResource> resources = Arrays.stream(csvFiles)
                .peek(file -> log.debug("Arquivo CSV encontrado: {}", file.getName()))
                .map(FileSystemResource::new)
                .toList();
                
        return resources.toArray(new FileSystemResource[0]);
    }

    @Bean
    public FlatFileItemReader<AddressRecord> addressCsvFileItemReader() {
        var linearMapper = new DefaultLineMapper<AddressRecord>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames("id", "userId", "address", "city", "state");
                        setDelimiter(",");
                    }
                });
                setFieldSetMapper(new AddressFieldSetMapper());
            }
        };
        FlatFileItemReader<AddressRecord> reader = new FlatFileItemReader<>(linearMapper);
        reader.setLinesToSkip(1); // Skip header
        return reader;
    }
}
