package it.simonericci97.github.meterpolis.meterpolis.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * represent additional information on a specific MeterpolisRoute
 */

@Getter
@RequiredArgsConstructor(staticName = "of")
public class MeterpolisRouteInfo implements MeterpolisBatchEntity{

    /**
     * metropolis working on
     */
    @NonNull
    private String metropolis;

    /**
     * MeterpolisRoute that this represents
     */
    @NonNull
    private MeterpolisRoute route;

    /**
     * line-of-sight distance between endpoint of this.route
     */
    @Setter
    @Accessors(chain = true)
    private Double distance;

    /**
     * true if-and-only-if this.route is valid to calculate directions and related stats, else false
     */
    @Setter
    @Accessors(chain = true)
    private boolean valid;


    @Override
    public String getDescription() {
        return this.metropolis + "." + valid + ".routeInfo";
    }
}
