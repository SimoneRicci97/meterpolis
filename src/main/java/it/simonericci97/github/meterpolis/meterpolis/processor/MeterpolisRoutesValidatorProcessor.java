package it.simonericci97.github.meterpolis.meterpolis.processor;

import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRouteInfo;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRoutesInfo;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisRouteEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Decides if a MeterpolisRouteInfo is a velid set of routes
 */
@Slf4j
@Component
public class MeterpolisRoutesValidatorProcessor implements ItemProcessor<MeterpolisRoutesInfo, MeterpolisRoutesInfo> {

    @Autowired
    private MeterpolisRouteEvaluator routeEvaluator;

    @Override
    public MeterpolisRoutesInfo process(MeterpolisRoutesInfo meterpolisRoutes) throws Exception {
        meterpolisRoutes.getInfos()
                .parallelStream()
                .forEach(i -> i.setValid(routeEvaluator.eval(i)));

        meterpolisRoutes.setValid(routeEvaluator.eval(meterpolisRoutes));

        return meterpolisRoutes;
    }
}
