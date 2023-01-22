package it.simonericci97.github.meterpolis.meterpolis.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Represent the MeterpolisDirectionStats calculated for a specific MeterpolisRoute
 */

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class MeterpolisRouteDirectionStat implements MeterpolisBatchEntity {

    /**
     * Metropolis working on
     */
    private String meterpolisName;

    /**
     * MeterpolisRoute on which stats have been calculated
     */
    private MeterpolisRoute route;

    /**
     * MeterpolisDirectionStats for the specific route
     */
    private MeterpolisDirectionStats stats;

    /**
     * filename from whcih stats have been read
     */
    private String filename;

    @Override
    public String getDescription() {
        return meterpolisName + "." + filename + ".directionStat";
    }
}
