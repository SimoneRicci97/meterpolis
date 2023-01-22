package it.simonericci97.github.meterpolis.meterpolis.processor;

import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRouteInfo;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRoutesInfo;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRoutes;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisCoordinatesService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Calculate line-of-sight distance between end-point of a MeterpolisRouteInfo
 */
@Component
public class MeterpolisRoutesDistanceProcessor implements ItemProcessor<MeterpolisRoutesInfo, MeterpolisRoutesInfo> {

    @Autowired
    private MeterpolisCoordinatesService coordinatesService;

    @Override
    public MeterpolisRoutesInfo process(MeterpolisRoutesInfo meterpolisRoutes) throws Exception {
        MeterpolisRoutesInfo md = MeterpolisRoutesInfo.of(meterpolisRoutes.getMetropolisName());

        List<MeterpolisRouteInfo> distances = meterpolisRoutes.getInfos()
                .parallelStream()
                .map(mr -> mr.setDistance(
                        coordinatesService.coordinatesCrowFliesDinstance(mr.getRoute().getFrom(), mr.getRoute().getTo())
                        ))
                .collect(Collectors.toList());


        return md.addAll(distances);
    }
}
