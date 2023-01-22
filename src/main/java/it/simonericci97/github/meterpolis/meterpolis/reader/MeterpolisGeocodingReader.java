package it.simonericci97.github.meterpolis.meterpolis.reader;

import com.google.maps.model.Bounds;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisBounds;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisMapsService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * read bounds for a specific metropolis
 */
@Component
@StepScope
public class MeterpolisGeocodingReader implements ItemReader<MeterpolisBounds> {

    @Autowired
    private MeterpolisMapsService service;

    private String metropolis;

    private int index;

    @Override
    public MeterpolisBounds read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(index >= 1) return null;
        String metropolisName = metropolis;
        this.index++;

        Bounds b = this.service.getMetropolisBounds(metropolisName);

        return MeterpolisBounds.of(metropolisName)
                .setBounds(b);
    }

    @Value("#{jobParameters['metropolis']}")
    public void setMetropolis(final String metropolisName) {
        this.metropolis = metropolisName;
    }
}


