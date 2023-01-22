package it.simonericci97.github.meterpolis.meterpolis.writer;

import com.google.gson.Gson;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisBounds;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisFileService;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisPathsManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A writer which stores bounds of a specific metropolis
 */
@Slf4j
@Component
public class MeterpolisBoundsWriter implements ItemWriter<MeterpolisBounds> {

    @Autowired
    private MeterpolisPathsManager pathsManager;

    @Autowired
    private MeterpolisFileService fileService;

    @Override
    public void write(List<? extends MeterpolisBounds> list) throws Exception {

        for(MeterpolisBounds b: list) {
            String storeBoundsTempPath = pathsManager.getMetropolisBoundsTempPath(b.getMetropolisName());
            writeBounds(storeBoundsTempPath, b);
        }

    }

    private void writeBounds(String path, MeterpolisBounds bounds) {
        fileService.writeFile(path, new Gson().toJson(bounds.getBounds()));
    }
}
