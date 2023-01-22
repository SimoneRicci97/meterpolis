package it.simonericci97.github.meterpolis.meterpolis.writer;

import com.google.gson.Gson;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRoutesInfo;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisFileService;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisPathsManager;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A writer which stores routes info
 */
@Component
public class MeterpolisRouteInfoWriter implements ItemWriter<MeterpolisRoutesInfo> {

    @Autowired
    private MeterpolisPathsManager pathsManager;

    @Autowired
    private MeterpolisFileService fileService;

    @Autowired
    private Gson gson;

    @Override
    public void write(List<? extends MeterpolisRoutesInfo> list) throws Exception {
        for(MeterpolisRoutesInfo info: list) {
            String routesPath = pathsManager.getMetropolisRoutesTempPath(info.getMetropolisName());
            fileService.writeFile(routesPath, gson.toJson(info));
        }
    }
}
