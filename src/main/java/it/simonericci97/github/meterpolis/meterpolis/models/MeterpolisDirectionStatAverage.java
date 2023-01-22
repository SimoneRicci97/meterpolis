package it.simonericci97.github.meterpolis.meterpolis.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * represents the average of the statistics calculated on all directions for a metropolis
 */

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class MeterpolisDirectionStatAverage {

    private String metropolisName;

    private float changeNumber;

    private float bigChangeNumber;

    private float alternatives;

    private float totalDuration;

    private float walkingDuration;

    private BigDecimal cost;

    private String producedTimestamp;

    private int totalRoutes;
}
