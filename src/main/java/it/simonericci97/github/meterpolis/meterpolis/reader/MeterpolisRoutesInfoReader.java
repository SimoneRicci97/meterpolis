package it.simonericci97.github.meterpolis.meterpolis.reader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRoute;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRoutes;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRoutesInfo;
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

import java.util.ArrayList;
import java.util.List;

/**
 * reads all MeterpolisRouteInfo stored for a specific metropolis
 */
@Slf4j
@Component
@StepScope
public class MeterpolisRoutesInfoReader implements ItemReader<MeterpolisRoutesInfo> {

    @Autowired
    private MeterpolisPathsManager pathsManager;

    @Autowired
    private MeterpolisFileService fileService;

    private String metropolis;

    private int index;

    @Override
    public MeterpolisRoutesInfo read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(index >= 1) return null;
        String metropolisName = metropolis;
        this.index++;
        String path = pathsManager.getMetropolisRoutesTempPath(metropolisName);

        String routesString = fileService.readFile(path);
        return new Gson().fromJson(routesString, MeterpolisRoutesInfo.class);
        //return MeterpolisRoutesInfo.of(metropolisName).addAll(routes);
    }

    @Value("#{jobParameters['metropolis']}")
    public void setMetropolis(final String metropolisName) {
        this.metropolis = metropolisName;
    }
}
