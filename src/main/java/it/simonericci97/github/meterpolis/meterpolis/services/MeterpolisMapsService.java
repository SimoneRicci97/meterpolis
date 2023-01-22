package it.simonericci97.github.meterpolis.meterpolis.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.errors.ZeroResultsException;
import com.google.maps.model.*;
import it.simonericci97.github.meterpolis.meterpolis.exception.GeoApiException;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRoute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A service that represent a wrapper for Google Maps API
 */
@Slf4j
@Service
public class MeterpolisMapsService {

    @Autowired
    private GeoApiContext geoContext;

    /**
     * Return bounds for a specific metropolis
     *
     * @param metropolisName metropolis on which calculate bounds
     * @return Bounds for metropolis
     * @throws GeoApiException in case of error calling API
     */
    public Bounds getMetropolisBounds(String metropolisName) throws GeoApiException {
        GeocodingResult[] results;
        try {
            results = GeocodingApi
                    .geocode(this.geoContext,  metropolisName)
                    .await();
        } catch (ApiException | InterruptedException | IOException e) {
            log.error("Error calling geocoding API for {}", metropolisName);
            throw new GeoApiException(e.getMessage(), e);
        }

        if(results.length == 0) {
            log.error("Calls to geocoding API did not retunr anything");
            throw new GeoApiException("No Results for metropolis " + metropolisName);
        }

        return results[0].geometry.bounds;
    }

    /**
     * Calculate direction to go from route departure point to route arrival point using TravelMode = TRANSIT
     *
     * @param route MeterpolisRoute on which calculate directions
     * @return Directions
     * @throws GeoApiException in case of error calling API
     */
    public List<DirectionsRoute> getRouteDirections(MeterpolisRoute route) throws GeoApiException {
        DirectionsApiRequest req = DirectionsApi.newRequest(geoContext)
                .origin(route.getFrom()).destination(route.getTo())
                .mode(TravelMode.TRANSIT);

        try {
            DirectionsResult result = req.await();
            return Arrays.asList(result.routes);
        } catch (ApiException | InterruptedException | IOException e) {
            log.error("Error calling directions API for {} - {}, {}", route.getFrom(), route.getTo(), e.getClass());
            if(e.getClass().equals(ZeroResultsException.class)) return new ArrayList<>();
            throw new GeoApiException(e.getMessage(), e);
        }

    }
}
