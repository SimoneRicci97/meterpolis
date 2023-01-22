package it.simonericci97.github.meterpolis.meterpolis.tasklet;

import it.simonericci97.github.meterpolis.meterpolis.MeterpolisApplication;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisFileService;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisPathsManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * A Tasklet tha prepare file system for batch running cleaning temp results of old runs
 */
@Slf4j
@Component
public class CreateEnvTasklet implements Tasklet {

    @Autowired
    private MeterpolisPathsManager pathsManager;

    @Autowired
    private MeterpolisFileService fileService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        boolean res = true;
        res &= cleanTempPath(pathsManager.getBoundsTempDir());
        res &= cleanTempPath(pathsManager.getRoutesTempDir());
        res &= cleanTempPath(pathsManager.getDirectionsTempDir());
        res &= cleanTempPath(pathsManager.getStatsTempDir());
        log.info("Results of env creation: {}", res);
        return  RepeatStatus.continueIf(!res);
    }

    private boolean cleanTempPath(String path) {
        File d = new File(path);
        if(d.exists() && d.isDirectory()) {
            List<File> content = fileService.listFileDir(path);
            boolean res = true;
            for(File c: content) {
                if((c.getName().split("\\.").length > 2
                && c.getName().split("\\.")[c.getName().split("\\.").length - 2].endsWith("temp"))
                || c.getName().endsWith(".temp")) {
                    log.info("Deleting file {}", c.getAbsolutePath());
                    res &= c.delete();
                } else if(c.isDirectory()) {
                    cleanTempPath(c.getAbsolutePath());
                }
            }
            return res;
        } else {
            log.info("Creating directory {}", d.getAbsolutePath());
            return d.mkdirs();
        }
    }
}
