package it.simonericci97.github.meterpolis.meterpolis.models;

import com.google.maps.model.DirectionsRoute;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

public class MeterpolisDirectionFilename extends MeterpolisDirections {

    /**
     * filename from which the direction has been read
     */

    @Getter
    @Setter
    @Accessors(chain = true)
    private String filename;

    public MeterpolisDirectionFilename(String meterpolisName, MeterpolisRoute route, List<DirectionsRoute> directions) {
        super(meterpolisName, route, directions);
    }

    public MeterpolisDirectionFilename(String meterpolisName, MeterpolisRoute route, List<DirectionsRoute> directions, String filename) {
        super(meterpolisName, route, directions);
        this.filename = filename;
    }

    @Override
    public String getDescription() {
        return super.getMeterpolisName() + "." + filename + ".directions";
    }
}
