package it.simonericci97.github.meterpolis.meterpolis.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Represents the stats related to one direction
 */

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class MeterpolisDirectionStats {

    /**
     * means of transportation changes number for a direction
     */
    private int changeNumber;

    /**
     * number of changes of travel for a direction
     */
    private int bigChangeNumber;

    /**
     * number of alternatives directions for the same route
     */
    private int alternatives;

    /**
     * total duration of a direction
     */
    private long totalDuration;

    /**
     * total duration for steps that use WALKING travel mode of a direction
     */
    private long walkingDuration;

    /**
     * Cost for the direction
     */
    private MeterpolisDirectionCost cost;
}
