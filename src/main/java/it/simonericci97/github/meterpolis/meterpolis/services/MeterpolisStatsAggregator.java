package it.simonericci97.github.meterpolis.meterpolis.services;

import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionCost;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionStatAverage;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionStats;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A service collect status of aggregated stats calculus
 */

@Service
@StepScope
public class MeterpolisStatsAggregator {
    
    private MeterpolisDirectionStats stats;
    
    private int count;

    @Autowired
    public MeterpolisStatsAggregator() {
        this.stats = new MeterpolisDirectionStats();
        this.stats.setCost(MeterpolisDirectionCost.free());
        this.count = 0;
    }

    /**
     * Update status with new entry
     *
     * @param newVal new entry used to update stats calculus
     */
    public void update(MeterpolisDirectionStats newVal) {
        this.stats.setAlternatives(this.stats.getAlternatives() + newVal.getAlternatives());
        this.stats.setWalkingDuration(this.stats.getWalkingDuration() + newVal.getWalkingDuration());
        this.stats.setTotalDuration(this.stats.getTotalDuration() + newVal.getTotalDuration());
        this.stats.setChangeNumber(this.stats.getChangeNumber() + newVal.getChangeNumber());
        this.stats.setBigChangeNumber(this.stats.getBigChangeNumber() + newVal.getBigChangeNumber());
        this.stats.getCost().setValue(this.stats.getCost().getValue().add(newVal.getCost().getValue()));
        this.count ++;
    }

    /**
     * retrieve a MeterpolisDirectionStatAverage which values are calculated on current status
     *
     * @return MeterpolisDirectionStatAverage which values are calculated on current status
     */
    public MeterpolisDirectionStatAverage average() {
        return new MeterpolisDirectionStatAverage()
                .setAlternatives((float) this.stats.getAlternatives() / this.count)
                .setBigChangeNumber((float) this.stats.getBigChangeNumber() / this.count)
                .setChangeNumber((float) this.stats.getChangeNumber() / this.count)
                .setTotalDuration((float) this.stats.getTotalDuration() / this.count)
                .setWalkingDuration((float) this.stats.getWalkingDuration() / this.count)
                .setCost(stats.getCost().getValue().divide(BigDecimal.valueOf(this.count), 2, RoundingMode.HALF_UP))
                .setProducedTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                .setTotalRoutes(this.count);

    }
}
