package br.com.victorhugolgr.lab.jobs.importuser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.stereotype.Component;

import br.com.victorhugolgr.lab.service.PropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileMovementStepExecutionListener implements StepExecutionListener{

    private final PropertyService propertyService;
    private static final String CSV_PATH = "PATH_CSV";
    private static final String PATH_CSV_PROCESSED = "PATH_CSV_PROCESSED";

    @Override
    public @Nullable ExitStatus afterStep(StepExecution stepExecution) {
        log.info("📋 Step finalizado: {} - Status: {}", stepExecution.getStepName(), stepExecution.getExitStatus());
        
        if(stepExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
            log.info("✅ Step completado com sucesso! Movendo arquivos...");
            moveProcessedFilesToProcessedFolder();
        } else {
            log.warn("⚠️ Step não completado. Status: {}", stepExecution.getExitStatus());
        }

        return stepExecution.getExitStatus();
    }

    private void moveProcessedFilesToProcessedFolder() {
        try {
            String csvPath = propertyService.getPropertyValueById(CSV_PATH);
            String processedPath = propertyService.getPropertyValueById(PATH_CSV_PROCESSED);

            log.info("📁 Caminho CSV: {}", csvPath);
            log.info("📁 Caminho processados: {}", processedPath);

            File csvFolder = new File(csvPath);
            File processedFolder = new File(processedPath);

            if(!processedFolder.exists()) {
                boolean created = processedFolder.mkdirs();
                log.info("📂 Pasta criada: {} - {}", processedPath, created ? "Sucesso" : "Falha");
            }

            File[] csvFiles = csvFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

            if(csvFiles == null || csvFiles.length == 0) {
                log.warn("⚠️ Nenhum arquivo CSV encontrado em: {}", csvPath);
                return;
            }

            log.info("📦 Encontrados {} arquivos para mover", csvFiles.length);

            for(File csvFile : csvFiles) {
                File destFile = new File(processedFolder, csvFile.getName());
                try {
                    Files.move(csvFile.toPath(), destFile.toPath(), REPLACE_EXISTING);
                    log.info("✅ Arquivo movido: {} → {}", csvFile.getName(), processedPath);
                } catch (IOException e) {
                    log.error("❌ Falha ao mover arquivo: {}", csvFile.getName(), e);
                }
            }
            
            log.info("🎉 Movimento de arquivos concluído!");

        } catch (Exception e) {
            log.error("❌ Erro crítico ao mover arquivos", e);
        }
    }

}
