package it.simonericci97.github.meterpolis.meterpolis.services;

import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRouteInfo;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRoutesInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * A service that eval a route to establish if it's useful to calculate stats on it
 */
@Slf4j
@Service
public class MeterpolisRouteEvaluator {

    @Value("${meterpolis.routes.min.distance}")
    private Double minDistance;

    @Value("${meterpolis.routes.number.req}")
    private Integer minValidRoutes;

    /**
     * Eval a single route relying on line-of-sight distance
     *
     * @param routeInfo route info to eval validity
     * @return true if line-of-sight distance is strictly major of property minDistance
     */
    public boolean eval(MeterpolisRouteInfo routeInfo) {
        return routeInfo.getDistance().compareTo(this.minDistance) > 0;
    }

    /**
     * Eval a set of routes
     *
     * @param routesInfo set of routes info to eval validity
     * @return true if all routes of the set are valid
     */
    public boolean eval(MeterpolisRoutesInfo routesInfo) {
        long validRoutes = routesInfo.getInfos()
                .parallelStream()
                .filter(MeterpolisRouteInfo::isValid)
                .count();

        log.info("{} valid routes for {} on {} checked => {}",
                validRoutes, routesInfo.getMetropolisName(), routesInfo.getInfos().size(), validRoutes >= minValidRoutes);

        return validRoutes >= minValidRoutes;
    }
}
