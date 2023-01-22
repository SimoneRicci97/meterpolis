package it.simonericci97.github.meterpolis.meterpolis.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * represents a set of MeterpolisRoute found inside a specific metropolis
 */

@Accessors(chain = true)
public class MeterpolisRoutes {

    /**
     * metropolis for whcih this represents the set of MeterpolisRoute
     */
    @Getter
    private String metropolis;

    /**
     * a list of MeterpolisRoute found inside a specific metropolis
     */
    private List<MeterpolisRoute> routes;

    private MeterpolisRoutes(String metropolisName) {
        this.metropolis = metropolisName;
        this.routes = new ArrayList<>();
    }

    public static MeterpolisRoutes of(String metropolisName) {
        return new MeterpolisRoutes(metropolisName);
    }

    public List<MeterpolisRoute> getRoutes() {
        return new ArrayList<>(this.routes);
    }

    /**
     *
     * @param mr a new MeterpolisRoute found inside a specific metropolis
     * @return this
     */
    public MeterpolisRoutes add(MeterpolisRoute mr) {
        this.routes.add(mr);
        return this;
    }

}


