package it.simonericci97.github.meterpolis.meterpolis.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Represents history of all stats calculated for the same metropolis
 */

@Getter
@AllArgsConstructor(staticName = "of")
public class MeterpolisDirectionStatAverageHistory {

    List<MeterpolisDirectionStatAverage> history;
}
