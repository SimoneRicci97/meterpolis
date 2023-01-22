package it.simonericci97.github.meterpolis.meterpolis.configuration;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapsApiConfig {

    @Value("${meterpolis.maps.api.key}")
    private String mapsApiKey;

    @Bean(destroyMethod = "shutdown")
    public GeoApiContext geoContext() {
        return new GeoApiContext.Builder()
                .apiKey(mapsApiKey)
                .build();
    }

}
