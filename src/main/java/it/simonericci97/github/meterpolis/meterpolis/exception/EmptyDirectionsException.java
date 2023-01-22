package it.simonericci97.github.meterpolis.meterpolis.exception;

import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionFilename;

/**
 * Exception thrown in case of a route for which no direction has been found
 */

public class EmptyDirectionsException extends Exception {

    private MeterpolisDirectionFilename directionsRoute;

    public EmptyDirectionsException(String message, MeterpolisDirectionFilename directionsRoute) {
        super(message);

        this.directionsRoute = directionsRoute;
    }

}
