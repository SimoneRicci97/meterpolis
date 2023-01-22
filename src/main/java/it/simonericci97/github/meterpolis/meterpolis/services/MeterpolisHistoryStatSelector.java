package it.simonericci97.github.meterpolis.meterpolis.services;

import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionStatAverage;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionStatAverageHistory;
import org.springframework.stereotype.Service;

/**
 * A service which extract the MeterpolisDirectionStatAverageHistory to write in output csv for a metropolis
 */
@Service
public class MeterpolisHistoryStatSelector {

    /**
     *
     * @param history all calculated stats for a metropolis in any batch run
     * @return the stats calculated on max number of routes
     */
    public MeterpolisDirectionStatAverage getMaxRouteProcessed(MeterpolisDirectionStatAverageHistory history) {
        int maxProcessed = history.getHistory().get(0).getTotalRoutes();
        int resIndex = 0;

        for(int i=1; i<history.getHistory().size(); i++) {
            if(history.getHistory().get(i).getTotalRoutes() > maxProcessed) {
                maxProcessed = history.getHistory().get(i).getTotalRoutes();
                resIndex = i;
            }
        }

        return history.getHistory().get(resIndex);
    }

}
