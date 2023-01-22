package it.simonericci97.github.meterpolis.meterpolis.writer;

import com.google.gson.Gson;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirections;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisFileService;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisPathsManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A writer which stores calculated directions
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MeterpolisDirectionsWriter implements ItemWriter<MeterpolisDirections> {

    @Autowired
    @NonNull
    private final MeterpolisPathsManager pathsManager;

    @Autowired
    private MeterpolisFileService fileService;

    @Autowired
    private Gson gson;

    @Override
    public void write(List<? extends MeterpolisDirections> list) throws Exception {
        for(MeterpolisDirections d: list) {
            String path = null;
            synchronized (this.pathsManager) {
                path = pathsManager.getMetropolisDirectioonsIncrementorTempPath(d.getMeterpolisName());
            }
            log.info("Writing directions on file {}", path);
            fileService.writeFile(path, gson.toJson(d));
        }
    }
}
