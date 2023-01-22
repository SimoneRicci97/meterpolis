package it.simonericci97.github.meterpolis.meterpolis.reader;

import com.google.gson.Gson;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRoute;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRouteInfo;
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

/**
 * reads all MeterpolisRouteInfo stored for a specific metropolis
 */
@Slf4j
@StepScope
@Component
public class MeterpolisRouteReader implements ItemReader<MeterpolisRouteInfo> {

    @Autowired
    private MeterpolisPathsManager pathsManager;

    @Autowired
    private MeterpolisFileService fileService;

    private String metropolis;

    private int index;

    private MeterpolisRoutesInfo routesInfo;

    @Override
    public MeterpolisRouteInfo read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        this.init();
        if(index >= routesInfo.getInfos().size()) return null;
        MeterpolisRouteInfo mri = routesInfo.getInfos().get(this.index++);

        while(!mri.isValid()) {
            mri = routesInfo.getInfos().get(this.index++);
        }

        return mri;
    }

    private void init() {
        if(this.routesInfo == null) {
            String path = pathsManager.getMetropolisRoutesTempPath(metropolis);
            log.info("Reading path {}", path);
            this.routesInfo = fileService.readFile(path, MeterpolisRoutesInfo.class);
            this.index = 0;
        }
    }

    @Value("#{jobParameters['metropolis']}")
    public void setMetropolis(final String metropolisName) {
        this.metropolis = metropolisName;
    }
}
