package it.simonericci97.github.meterpolis.meterpolis.listener;

import org.slf4j.MDC;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

/**
 * Just a listener that values MDC to recognize logs in a step executed with task executor
 */

@Component
public class MeterpolisChunkListener implements ChunkListener {
    @Override
    public void beforeChunk(ChunkContext chunkContext) {
        StepExecution stepExecution =  chunkContext.getStepContext().getStepExecution();
        MDC.put("stepName", stepExecution.getStepName());
        MDC.put("jobName", stepExecution.getJobExecution().getJobInstance().getJobName());
    }

    @Override
    public void afterChunk(ChunkContext chunkContext) {
        MDC.remove("stepName");
        MDC.remove("jobName");
    }

    @Override
    public void afterChunkError(ChunkContext chunkContext) {

    }
}
