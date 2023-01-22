package it.simonericci97.github.meterpolis.meterpolis.services;

import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.TravelMode;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionCost;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * A service that calculates stats on directions
 */
@Service
public class MeterpolisDirectionsStatCalculator {

    /**
     *
     * @param directionsRoute info of a direction on which calculate stats
     * @return number of travel mode changes
     */
    public int getDirectionBigChangeNumber(DirectionsRoute directionsRoute) {
        DirectionsStep[] steps = directionsRoute.legs[0].steps;
        TravelMode travelMode = TravelMode.WALKING;
        int changes = 0;
        for(DirectionsStep step: steps) {
            if(!travelMode.equals(step.travelMode)) {
                changes++;
            }
            travelMode = step.travelMode;
        }

        return changes;
    }

    /**
     *
     * @param directionsRoute info of a direction on which calculate stats
     * @return number of means of transposrtation changes
     */
    public int getDirectionChangeNumber(DirectionsRoute directionsRoute) {
        return directionsRoute.legs[0].steps.length;
    }

    /**
     *
     * @param directionsRoute info of a direction on which calculate stats
     * @return return number of alternative directions
     */
    public int getAlternatives(DirectionsRoute directionsRoute) {
        return directionsRoute.legs.length - 1;
    }

    /**
     *
     * @param directionsRoute info of a direction on which calculate stats
     * @return total diration of direction
     */
    public long getTotalDuration(DirectionsRoute directionsRoute) {
        return directionsRoute.legs[0].duration.inSeconds;
    }

    /**
     *
     * @param directionsRoute info of a direction on which calculate stats
     * @return total duration of steps which use TravelMode.WALKING
     */
    public long getWalkingDuration(DirectionsRoute directionsRoute) {
        DirectionsStep[] direction = directionsRoute.legs[0].steps;
        return Arrays.asList(direction).parallelStream()
                .filter(s -> s.travelMode.equals(TravelMode.WALKING))
                .map(s -> s.duration.inSeconds)
                .reduce(0L, Long::sum, Long::sum);
    }

    /**
     *
     * @param direction info of a direction on which calculate stats
     * @return the cost of direction
     */
    public MeterpolisDirectionCost getDirectionCost(DirectionsRoute direction) {
        if(direction.fare == null) return MeterpolisDirectionCost.free();
        return MeterpolisDirectionCost.of(direction.fare.value, direction.fare.currency);
    }

}
