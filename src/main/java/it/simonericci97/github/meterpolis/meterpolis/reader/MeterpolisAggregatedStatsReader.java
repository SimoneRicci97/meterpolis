package it.simonericci97.github.meterpolis.meterpolis.reader;

import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionStatAverage;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisFileService;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisPathsManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Reads last MeterpolisDirectionStatAverage calculated for a metropolis
 */

@Slf4j
@StepScope
@Component
public class MeterpolisAggregatedStatsReader implements ItemReader<MeterpolisDirectionStatAverage> {

    @Autowired
    private MeterpolisPathsManager pathsManager;

    @Autowired
    private MeterpolisFileService fileService;

    private String metropolis;

    private boolean done;

    @Override
    public MeterpolisDirectionStatAverage read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(done) return null;
        String path = pathsManager.getMetropolisStatsTempPath(metropolis);
        MeterpolisDirectionStatAverage lastOutput = fileService.readFile(path, MeterpolisDirectionStatAverage.class);

        done = true;

        log.info("Returning last output");
        return lastOutput;
    }

    @Value("#{jobParameters['metropolis']}")
    public void setMetropolis(final String metropolisName) {
        this.metropolis = metropolisName;
    }
}
