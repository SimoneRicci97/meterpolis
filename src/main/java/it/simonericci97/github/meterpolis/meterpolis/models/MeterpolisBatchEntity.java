package it.simonericci97.github.meterpolis.meterpolis.models;

/**
 * A marker interface for type that marks types processed by meterpolis batch steps
 */

public interface MeterpolisBatchEntity {

    /**
     *
     * @return a human readable description for MeterpolisBatchEntity instance
     */
    public String getDescription();

}
