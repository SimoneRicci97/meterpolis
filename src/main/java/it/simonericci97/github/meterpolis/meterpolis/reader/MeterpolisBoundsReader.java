package it.simonericci97.github.meterpolis.meterpolis.reader;

import com.google.maps.model.Bounds;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisBounds;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisFileService;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisPathsManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * reads bounds calculated for a specific metropolis
 */
@Slf4j
@Component
@StepScope
public class MeterpolisBoundsReader implements ItemReader<MeterpolisBounds> {

    @Autowired
    private MeterpolisPathsManager pathsManager;

    @Autowired
    private MeterpolisFileService fileService;

    private String metropolis;

    private int index;

    @Override
    public MeterpolisBounds read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(index >= 1) return null;
        String meterpolisName = metropolis;
        index++;

        String boundsPath = pathsManager.getMetropolisBoundsTempPath(meterpolisName);

        Bounds b = fileService.readFile(boundsPath, Bounds.class);

        log.info("Read {} for metropolis {}", b.toString(), meterpolisName);

        return MeterpolisBounds.of(meterpolisName).setBounds(b);

    }

    @Value("#{jobParameters['metropolis']}")
    public void setMetropolis(final String metropolisName) {
        this.metropolis = metropolisName;
    }
}
