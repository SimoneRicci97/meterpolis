package it.simonericci97.github.meterpolis.meterpolis.tasklet;

import com.google.gson.Gson;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionStatAverage;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionStats;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRouteDirectionStat;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisFileService;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisPathsManager;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisStatsAggregator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A Tasklet calculate aggregated stats (i.e. avg) for a metropolis based on stats calculated on all directions
 */
@Slf4j
@Component
@StepScope
public class AggregatedStatsCalculatorTasklet implements Tasklet {

    @Autowired
    MeterpolisPathsManager pathsManager;

    @Autowired
    MeterpolisFileService fileService;

    @Autowired
    Gson gson;

    @Autowired
    MeterpolisStatsAggregator aggregated;

    private String metropolis;

    @Value("${meterpolis.chunksize}")
    private int readAtOnce;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        String dir = pathsManager.getStatsTempDir(metropolis);
        List<File> statFiles = fileService.listFileDir(dir);

        log.info("{} stats file in {}", statFiles.size(), dir);

        int offset = 0;
        while(offset < statFiles.size()) {
            List<MeterpolisDirectionStats> page = readOnce(statFiles, offset);

            page.forEach(aggregated::update);

            offset += page.size();
            log.info("Aggregated {} stats", offset);
        }

        MeterpolisDirectionStatAverage average = aggregated.average().setMetropolisName(metropolis);

        String path = pathsManager.getMetropolisStatsTempPath(metropolis);
        log.info("Dumping aggregated stats on {}", path);
        fileService.writeFile(path, gson.toJson(average));

        return RepeatStatus.FINISHED;
    }

    private List<MeterpolisDirectionStats> readOnce(List<File> files, int from) {
        List<MeterpolisDirectionStats> res = new ArrayList<>();
        int to = Math.min(from + this.readAtOnce, files.size());
        for(int i=from; i<to; i++) {
            res.add(fileService.readFile(files.get(i).getAbsolutePath(), MeterpolisRouteDirectionStat.class).getStats());
        }
        log.info("Read {} files {}-{}", res.size(), from, to);
        return res;
    }

    @Value("#{jobParameters['metropolis']}")
    public void setMetropolis(final String metropolisName) {
        this.metropolis = metropolisName;
    }
}
