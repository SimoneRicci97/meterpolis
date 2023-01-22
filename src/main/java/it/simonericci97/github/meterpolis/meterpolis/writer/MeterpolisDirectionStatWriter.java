package it.simonericci97.github.meterpolis.meterpolis.writer;

import com.google.gson.Gson;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRouteDirectionStat;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisFileService;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisPathsManager;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A writer which stores stats calculated for a direction
 */
@Component
public class MeterpolisDirectionStatWriter implements ItemWriter<MeterpolisRouteDirectionStat> {

    @Autowired
    private MeterpolisPathsManager pathsManager;

    @Autowired
    private MeterpolisFileService fileService;

    @Autowired
    private Gson gson;

    @Override
    public void write(List<? extends MeterpolisRouteDirectionStat> list) throws Exception {

        for(MeterpolisRouteDirectionStat stat: list) {
            fileService.writeFile(
                    pathsManager.getMetropolisStatsIncrementorTempPath(stat.getMeterpolisName(), stat.getFilename()),
                    gson.toJson(stat));
        }

    }
}
