package it.simonericci97.github.meterpolis.meterpolis.reader;

import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionFilename;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisFileService;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisPathsManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Reads all stored directions for all routes related to a specific metropolis
 */
@Slf4j
@StepScope
@Component
public class MeterpolisDirectionsReader implements ItemReader<MeterpolisDirectionFilename> {

    @Autowired
    private MeterpolisPathsManager pathsManager;

    @Autowired
    private MeterpolisFileService fileService;

    private String metropolis;

    private int index;

    private List<String> metropolisDirections;

    @Override
    public MeterpolisDirectionFilename read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        this.init();
        if(this.index >= this.metropolisDirections.size()) return null;
        String filename = pathsManager.getDirectionsTempDir(metropolis) + metropolisDirections.get(this.index++);
        log.info("Reading direction file {}", fileService.extractFilename(filename));
        MeterpolisDirectionFilename md = fileService.readFile(
                filename,
                MeterpolisDirectionFilename.class);
        return md.setFilename(fileService.extractFilename(filename));
    }

    private void init() {
        if(this.metropolisDirections == null || this.metropolisDirections.isEmpty()) {
            String directionsDir = pathsManager.getDirectionsTempDir(metropolis);
            metropolisDirections = fileService.listDir(directionsDir);
            this.index = 0;
        }
    }

    @Value("#{jobParameters['metropolis']}")
    public void setMetropolis(final String metropolisName) {
        this.metropolis = metropolisName;
    }
}
