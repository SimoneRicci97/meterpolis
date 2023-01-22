package it.simonericci97.github.meterpolis.meterpolis.processor;

import it.simonericci97.github.meterpolis.meterpolis.models.*;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisCoordinatesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * generate random routes inside bounds of a specific metropolis
 */
@Slf4j
@JobScope
@Component
public class MeterpolisBoundsProcessor implements ItemProcessor<MeterpolisBounds, MeterpolisRoutesInfo> {

    /**
     * number of routes to generate at execution
     */
    @Value("${meterpolis.routes.number.gen}")
    private int routesNumber;

    @Autowired
    private MeterpolisCoordinatesService routesGenerator;

    @Autowired
    private ConcurrentMapCacheManager cacheManager;

    @Override
    public MeterpolisRoutesInfo process(MeterpolisBounds bounds) throws Exception {
        // retrieve routes generated in previous execution of same step
        MeterpolisRoutesInfo mrs = Optional.ofNullable(
                cacheManager.getCache("routes").get(bounds.getMetropolisName(), MeterpolisRoutesInfo.class)
        ).orElse(MeterpolisRoutesInfo.of(bounds.getMetropolisName()));

        if(mrs.getInfos().isEmpty()) log.info("Nothing in cache <=> first execution");

        for(int i=0; i<this.routesNumber; i++) {
            // generates random routes
            MeterpolisRoute mr = routesGenerator.generateRandomRoute(bounds.getBounds().northeast, bounds.getBounds().southwest);
            mrs.add(MeterpolisRouteInfo.of(bounds.getMetropolisName(), mr));
        }

        return mrs;
    }
}
