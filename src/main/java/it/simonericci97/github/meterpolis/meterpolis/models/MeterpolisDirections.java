package it.simonericci97.github.meterpolis.meterpolis.models;

import com.google.maps.model.DirectionsRoute;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Represents directions for a MeterpolisRoute
 */

@Getter
@AllArgsConstructor
public class MeterpolisDirections implements MeterpolisBatchEntity {

    /**
     * Metropolis represented by this
     */
    protected String meterpolisName;

    /**
     * MeterpolisRoute for which this represents directions
     */
    protected MeterpolisRoute route;

    /**
     * Calculated directions
     */
    protected List<DirectionsRoute> directions;

    @Override
    public String getDescription() {
        return meterpolisName + ".directions";
    }
}
