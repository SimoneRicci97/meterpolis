package it.simonericci97.github.meterpolis.meterpolis.exception;

/**
 * Exception thrown in case of error calling Maps API
 */

public class GeoApiException extends Exception {

    public GeoApiException(String message) {
        super(message);
    }

    public GeoApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
