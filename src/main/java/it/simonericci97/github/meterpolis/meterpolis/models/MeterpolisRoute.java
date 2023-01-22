package it.simonericci97.github.meterpolis.meterpolis.models;

import com.google.maps.model.LatLng;
import lombok.*;

/**
 * Reprsent a route inside the bounds of a metropolis which meterpolis-batch will uses to calculate direction and stats
 */

@Getter
@AllArgsConstructor(staticName = "of")
public class MeterpolisRoute {

    /**
     * Departure point represent by lat and long
     */
    private LatLng from;

    /**
     * Arrival point represent by lat and long
     */
    private LatLng to;

    @Override
    public String toString() {
        return from.toString() + "," + to.toString();
    }
}
