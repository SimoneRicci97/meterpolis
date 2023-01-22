package it.simonericci97.github.meterpolis.meterpolis.writer;

import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionStatAverage;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisPathsManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

/**
 * A writer taht append the last stats calculus to history of a specific metropolis
 */
@Slf4j
@StepScope
@Component
public class MeterpolisAggregatedStatsHistoryWriter extends FlatFileItemWriter<MeterpolisDirectionStatAverage> implements InitializingBean {

    @Autowired
    private MeterpolisPathsManager pathsManager;

    @Autowired
    @Qualifier("avgStatLineAggregatorHistory")
    DelimitedLineAggregator<MeterpolisDirectionStatAverage> avgStatLineAggregator;

    private String metropolis;

    @Value("${meterpolis.stats.csv.header}")
    String[] statCsvHeader;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.setLineAggregator(avgStatLineAggregator);
        super.setResource(new FileSystemResource(pathsManager.getMetropolisStatsHistoryPath(metropolis)));
        super.setAppendAllowed(true);
        super.setHeaderCallback(w -> w.write(String.join(";", statCsvHeader)));
    }

    @Value("#{jobParameters['metropolis']}")
    public void setMetropolis(final String metropolisName) {
        this.metropolis = metropolisName;
    }
}
