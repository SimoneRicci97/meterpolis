package it.simonericci97.github.meterpolis.meterpolis.decider;

import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRoutesInfo;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisFileService;
import it.simonericci97.github.meterpolis.meterpolis.services.MeterpolisPathsManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

/**
 * Job decider that eval if enough routes have been generated
 */

@Slf4j
@Component
public class MeterpolisValidRoutesDecider implements JobExecutionDecider {

    @Autowired
    private MeterpolisPathsManager pathsManager;

    @Autowired
    private MeterpolisFileService fileService;

    @Autowired
    private ConcurrentMapCacheManager cacheManager;

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        String meterpolisParam = jobExecution.getJobParameters().getString("metropolis");
        if(meterpolisParam == null) return new FlowExecutionStatus("ILLEGAL_STATE");
        String routesPath = pathsManager.getMetropolisRoutesTempPath(meterpolisParam);
        MeterpolisRoutesInfo routes = fileService.readFile(routesPath, MeterpolisRoutesInfo.class);

        log.info("{} has a {}valid set of routes", meterpolisParam, routes.isValid() ? "" : "non ");

        if(!routes.isValid()) {
            cacheManager.getCache("routes").put(meterpolisParam, routes);
            log.warn("Routes will be regenerated");
            return new FlowExecutionStatus("AGAIN");
        }

        log.info("Routes generation complete");
        return new FlowExecutionStatus("COMPLETED");
    }
}
