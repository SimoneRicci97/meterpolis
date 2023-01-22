package it.simonericci97.github.meterpolis.meterpolis.processor;

import com.google.maps.model.DirectionsRoute;
import it.simonericci97.github.meterpolis.meterpolis.exception.EmptyDirectionsException;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionFilename;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionStats;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRouteDirectionStat;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisDirectionsStatCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Calculate stats for a direction
 */
@Slf4j
@Component
public class MeterpolisDirectionProcessor implements ItemProcessor<MeterpolisDirectionFilename, MeterpolisRouteDirectionStat> {

    @Autowired
    MeterpolisDirectionsStatCalculator statCalculator;

    @Override
    public MeterpolisRouteDirectionStat process(MeterpolisDirectionFilename meterpolisDirections) throws Exception {
        if(meterpolisDirections.getDirections().isEmpty()) {
            log.warn("{} has no directions", meterpolisDirections.getFilename());
            throw new EmptyDirectionsException("Empty direction", meterpolisDirections);
        }
        DirectionsRoute mapsAdvised = meterpolisDirections.getDirections().get(0);

        MeterpolisDirectionStats stat = new MeterpolisDirectionStats();
        stat.setBigChangeNumber(statCalculator.getDirectionBigChangeNumber(mapsAdvised));
        stat.setAlternatives(statCalculator.getAlternatives(mapsAdvised));
        stat.setChangeNumber(statCalculator.getDirectionChangeNumber(mapsAdvised));
        stat.setCost(statCalculator.getDirectionCost(mapsAdvised));
        stat.setTotalDuration(statCalculator.getTotalDuration(mapsAdvised));
        stat.setWalkingDuration(statCalculator.getWalkingDuration(mapsAdvised));

        log.info("Calculated stats for direction in file {}", meterpolisDirections.getFilename());

        return new MeterpolisRouteDirectionStat()
                .setMeterpolisName(meterpolisDirections.getMeterpolisName())
                .setRoute(meterpolisDirections.getRoute())
                .setStats(stat)
                .setFilename(meterpolisDirections.getFilename());
    }
}
