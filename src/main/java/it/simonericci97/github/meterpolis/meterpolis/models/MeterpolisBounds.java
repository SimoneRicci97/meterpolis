package it.simonericci97.github.meterpolis.meterpolis.models;

import com.google.maps.model.Bounds;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * Represents the bounds for a metropolis
 */

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
@Accessors(chain = true)
public class MeterpolisBounds implements MeterpolisBatchEntity {

    /**
     * the metropolis name which bounds are represented by this
     */
    @NonNull
    private String metropolisName;

    /**
     * bounds far the metropolis represented by this
     */
    private Bounds bounds;


    @Override
    public String getDescription() {
        return metropolisName + ".bounds";
    }
}
