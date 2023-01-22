package it.simonericci97.github.meterpolis.meterpolis.services;

import com.google.maps.model.LatLng;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRoute;
import org.springframework.stereotype.Service;


/**
 * A service that handl operations with coordinates
 */
@Service
public class MeterpolisCoordinatesService {

    private static final Integer EARTH_RADIUS = 6371;

    /**
     *
     * @param ne north-east bound
     * @param sw south-west bound
     * @return a rondom MeterpolisRoute whcih endpoints are inside bounds
     */
    public MeterpolisRoute generateRandomRoute(LatLng ne, LatLng sw) {
        double lat1 = Math.random() * (ne.lat - sw.lat) + sw.lat;
        double lat2 = Math.random() * (ne.lat - sw.lat) + sw.lat;
        double lng1 = Math.random() * (ne.lng - sw.lng) + sw.lng;
        double lng2 = Math.random() * (ne.lng - sw.lng) + sw.lng;

        return MeterpolisRoute.of(new LatLng(lat1, lng1), new LatLng(lat2, lng2));
    }

    /**
     *
     * @param pos1 departure lat and long
     * @param pos2 arrival lat and long
     * @return line-of-sight distance between ps1 and pos2
     */
    public Double coordinatesCrowFliesDinstance(LatLng pos1, LatLng pos2) {
        double dLat = Math.toRadians(pos2.lat - pos1.lat);
        double dLon = Math.toRadians(pos2.lng - pos1.lng);

        double lat1 = Math.toRadians(pos1.lat);
        double lat2 = Math.toRadians(pos2.lat);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return EARTH_RADIUS * c;
    }

}
