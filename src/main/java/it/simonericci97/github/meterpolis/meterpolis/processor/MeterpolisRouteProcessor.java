package it.simonericci97.github.meterpolis.meterpolis.processor;

import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import it.simonericci97.github.meterpolis.meterpolis.exception.GeoApiException;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirections;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRoute;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRouteInfo;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisMapsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Calculate directions for a MeterpolisRoute
 */
@Slf4j
@Component
public class MeterpolisRouteProcessor implements ItemProcessor<MeterpolisRouteInfo, MeterpolisDirections> {

    @Autowired
    private MeterpolisMapsService mapsService;

    @Override
    public MeterpolisDirections process(MeterpolisRouteInfo meterpolisRoute) throws Exception {
        List<DirectionsRoute> directions;
        try {
            directions = mapsService.getRouteDirections(meterpolisRoute.getRoute());
            log.info("{} directions for {}", directions.size(), meterpolisRoute.getRoute().toString());
        } catch (GeoApiException e) {
            log.error("Cannot calculate directions for route {}", meterpolisRoute.getRoute().toString(), e);
            return null;
        }

        return new MeterpolisDirections(meterpolisRoute.getMetropolis(), meterpolisRoute.getRoute(), directions);
    }
}
