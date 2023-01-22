package it.simonericci97.github.meterpolis.meterpolis.listener;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

/**
 * A listener which logs the start/end event of a step
 */

@Slf4j
@Component
public class MeterpolisStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        MDC.put("event", "START");
        log.info("{}.{}", stepExecution.getJobExecution().getJobInstance().getJobName(), stepExecution.getStepName());
        MDC.remove("event");

        MDC.put("stepName", stepExecution.getStepName());
        MDC.put("jobName", stepExecution.getJobExecution().getJobInstance().getJobName());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        MDC.remove("stepName");
        MDC.remove("jobName");

        MDC.put("event", "END");
        log.info("{}.{}", stepExecution.getJobExecution().getJobInstance().getJobName(), stepExecution.getStepName());
        MDC.remove("event");
        return stepExecution.getExitStatus();
    }
}
