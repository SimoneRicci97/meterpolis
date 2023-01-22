package it.simonericci97.github.meterpolis.meterpolis.processor;

import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionStatAverage;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionStatAverageHistory;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisHistoryStatSelector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Decides which calculated stats for a metropolis use to produce output csv
 */
@Slf4j
@Component
public class MeterpolisStatAverageHistoryProcessor implements ItemProcessor<MeterpolisDirectionStatAverageHistory, MeterpolisDirectionStatAverage> {

    @Autowired
    public MeterpolisHistoryStatSelector avgStatSelector;


    @Override
    public MeterpolisDirectionStatAverage process(MeterpolisDirectionStatAverageHistory meterpolisDirectionStatAverageHistory) throws Exception {
        log.info("Processing {}", meterpolisDirectionStatAverageHistory.getHistory().get(0).getMetropolisName());
        return avgStatSelector.getMaxRouteProcessed(meterpolisDirectionStatAverageHistory);
    }
}
