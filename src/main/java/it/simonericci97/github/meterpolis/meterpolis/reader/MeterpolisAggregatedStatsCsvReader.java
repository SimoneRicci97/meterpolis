package it.simonericci97.github.meterpolis.meterpolis.reader;

import com.google.maps.internal.ratelimiter.Preconditions;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionStatAverage;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionStatAverageHistory;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisFileService;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisPathsManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * reads history for all metropolis in this.metropolis
 */
@Slf4j
@Component
public class MeterpolisAggregatedStatsCsvReader implements ItemReader<MeterpolisDirectionStatAverageHistory>, InitializingBean{

    @Autowired
    private MeterpolisPathsManager pathsManager;

    @Autowired
    private MeterpolisFileService fileService;

    @Value("${meterpolis.base.out.path}")
    private String historiesDir;

    private List<String> metropolis;

    @Autowired
    private DefaultLineMapper<MeterpolisDirectionStatAverage> statLineMapper;

    private int index;

    @Override
    public MeterpolisDirectionStatAverageHistory read() throws Exception {
        if(this.index >= metropolis.size()) {
            log.info("INDEX: {} - METROPOLIS: {}", index, metropolis.size());
            return null;
        }

        List<MeterpolisDirectionStatAverage> res = new ArrayList<>();
        String metropolisName = metropolis.get(this.index++);
        List<String> lines = fileService.readFileLines(pathsManager.getMetropolisStatsHistoryPath(metropolisName));

        for(int i=1; i<lines.size(); i++) {
            MeterpolisDirectionStatAverage tmp = statLineMapper.mapLine(lines.get(i), i);
            res.add(tmp);
        }

        log.info("Read {} for metropolis {}", res.size(), metropolisName);

        return MeterpolisDirectionStatAverageHistory.of(res);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(fileService);
        Preconditions.checkNotNull(pathsManager);
        this.metropolis = new ArrayList<>();
        List<File> histories = fileService.listFileDir(historiesDir);

        for(File f: histories) {
            if(f.isDirectory()) {
                this.metropolis.add(f.getName());
            }
        }
        this.index = 0;
        log.info("Found {} histories files under {}", metropolis.size(), historiesDir);
    }
}
