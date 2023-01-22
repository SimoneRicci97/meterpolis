package it.simonericci97.github.meterpolis.meterpolis.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of MeterpolisRouteInfo for the same metropolis
 */

@Getter
public class MeterpolisRoutesInfo implements MeterpolisBatchEntity {

    /**
     * metropolis for whcih this represents the set of MeterpolisRouteInfo
     */
    @NonNull
    private String metropolisName;

    /**
     * list of MeterpolisRouteInfo
     */
    private List<MeterpolisRouteInfo> infos;

    /**
     * true if-and-only-if this.infos is a valid set to calculate directions and related stats, else false
     */
    @Setter
    @Accessors(chain = true)
    private boolean valid;

    private MeterpolisRoutesInfo(String metropolisName) {
        this.metropolisName = metropolisName;
        this.infos = new ArrayList<>();
    }

    public static MeterpolisRoutesInfo of(String metropolisName) {
        return new MeterpolisRoutesInfo(metropolisName);
    }

    public List<MeterpolisRouteInfo> getInfos() {
        return new ArrayList<>(this.infos);
    }

    public MeterpolisRoutesInfo add(MeterpolisRouteInfo mr) {
        this.infos.add(mr);
        return this;
    }

    public MeterpolisRoutesInfo addAll(List<MeterpolisRouteInfo> mrs) {
        this.infos.addAll(mrs);
        return this;
    }

    @Override
    public String getDescription() {
        return metropolisName + ".routeInfo";
    }
}
