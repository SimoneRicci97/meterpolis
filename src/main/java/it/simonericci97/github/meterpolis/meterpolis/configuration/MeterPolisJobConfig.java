package it.simonericci97.github.meterpolis.meterpolis.configuration;

import it.simonericci97.github.meterpolis.meterpolis.decider.MeterpolisValidRoutesDecider;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class MeterPolisJobConfig {

    @Autowired
    JobBuilderFactory jbf;

    /*@Bean
    public JobLauncher meterpolisJobLauncher(
            @Autowired ThreadPoolTaskExecutor taskExecutor,
            @Autowired JobRepository jobRepository){
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setTaskExecutor(taskExecutor);
        jobLauncher.setJobRepository(jobRepository);
        return jobLauncher;
    }*/

    @Bean
    Job meterpolisJob(
            @Autowired Step meterpolisPrepareEnv,
            @Autowired Step meterpolisBoundsStoreStep,
            @Autowired Step meterpolisRoutesStoreStep,
            @Autowired Step meterpolisRoutesInfoStoreStep,
            @Autowired Step meterpolisDirectionsStoreStep,
            @Autowired MeterpolisValidRoutesDecider decider,
            @Autowired Step meterpolisDirectionsStatStep,
            @Autowired Step meterpolisAggergateStats,
            @Autowired Step meterpolisAverageHistory
            ) {
        return jbf.get("meterpolisJob")
                .incrementer(new RunIdIncrementer())
                .start(meterpolisPrepareEnv)
                .next(meterpolisBoundsStoreStep)
                .next(meterpolisRoutesStoreStep)
                .next(meterpolisRoutesInfoStoreStep)
                .next(decider)
                    .on("COMPLETED")
                        .to(meterpolisDirectionsStoreStep)
                        .next(meterpolisDirectionsStatStep)
                        .next(meterpolisAggergateStats)
                        .next(meterpolisAverageHistory)
                    .from(decider).on("AGAIN").to(meterpolisRoutesStoreStep)
                    .from(decider).on("ILLEGAL_STATE").fail()
                .end()
                .build();
    }


    @Bean
    Job outputProducerJob(
            @Autowired Step meterpolisOutputProducerStep
    ) {
        return jbf.get("outputProducerJob")
                .incrementer(new RunIdIncrementer())
                .start(meterpolisOutputProducerStep)
                .build();
    }

}
