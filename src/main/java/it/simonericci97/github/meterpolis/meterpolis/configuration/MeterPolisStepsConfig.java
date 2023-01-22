package it.simonericci97.github.meterpolis.meterpolis.configuration;

import it.simonericci97.github.meterpolis.meterpolis.exception.EmptyDirectionsException;
import it.simonericci97.github.meterpolis.meterpolis.listener.MeterpolisBatchSkipListener;
import it.simonericci97.github.meterpolis.meterpolis.listener.MeterpolisChunkListener;
import it.simonericci97.github.meterpolis.meterpolis.listener.MeterpolisItemProcessorListener;
import it.simonericci97.github.meterpolis.meterpolis.listener.MeterpolisStepListener;
import it.simonericci97.github.meterpolis.meterpolis.models.*;
import it.simonericci97.github.meterpolis.meterpolis.processor.*;
import it.simonericci97.github.meterpolis.meterpolis.reader.*;
import it.simonericci97.github.meterpolis.meterpolis.tasklet.AggregatedStatsCalculatorTasklet;
import it.simonericci97.github.meterpolis.meterpolis.tasklet.CreateEnvTasklet;
import it.simonericci97.github.meterpolis.meterpolis.writer.*;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class MeterPolisStepsConfig {

    @Value("${meterpolis.chunksize}")
    private int chunkSize;

    @Autowired
    StepBuilderFactory sbf;

    @Autowired
    TaskExecutor taskExecutor;

    @Autowired
    MeterpolisStepListener stepListener;

    @Autowired
    MeterpolisItemProcessorListener<MeterpolisRouteInfo, MeterpolisDirections> processorListener;

    @Bean
    Step meterpolisPrepareEnv(
            @Autowired CreateEnvTasklet tasklet
    ) {
        return sbf.get("meterpolisPrepareEnv")
                .tasklet(tasklet)
                .listener(stepListener)
                .build();
    }

    @Bean
    Step meterpolisBoundsStoreStep(
            @Autowired MeterpolisGeocodingReader reader,
            @Autowired MeterpolisBoundsWriter writer
            ) {
        return sbf.get("meterpolisBoundsStoreStep")
                .<MeterpolisBounds, MeterpolisBounds>chunk(chunkSize)
                .reader(reader)
                .writer(writer)
                .listener(processorListener)
                .listener(stepListener)
                .build();
    }

    @Bean
    Step meterpolisRoutesStoreStep(
            @Autowired MeterpolisBoundsReader reader,
            @Autowired MeterpolisBoundsProcessor processor,
            @Autowired MeterpolisRoutesWriter writer
    ) {
        return sbf.get("meterpolisRoutesStoreStep")
                .<MeterpolisBounds, MeterpolisRoutesInfo>chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(processorListener)
                .listener(stepListener)
                .build();
    }


    @Bean
    Step meterpolisRoutesInfoStoreStep(
            @Autowired MeterpolisRoutesInfoReader reader,
            @Autowired MeterpolisRoutesDistanceProcessor processor,
            @Autowired MeterpolisRoutesValidatorProcessor validator,
            @Autowired MeterpolisRouteInfoWriter writer
    ) {
        return sbf.get("meterpolisRoutesInfoStoreStep")
                .<MeterpolisRoutesInfo, MeterpolisRoutesInfo>chunk(chunkSize)
                .reader(reader)
                .processor(new CompositeItemProcessorBuilder<MeterpolisRoutesInfo, MeterpolisRoutesInfo>()
                        .delegates(processor, validator)
                        .build()
                )
                .writer(writer)
                .listener(processorListener)
                .listener(stepListener)
                .build();
    }

    @Bean
    Step meterpolisDirectionsStoreStep(
            @Autowired MeterpolisRouteReader reader,
            @Autowired MeterpolisRouteProcessor processor,
            @Autowired MeterpolisDirectionsWriter writer,
            @Autowired MeterpolisChunkListener chunkListener

    ) {
        return sbf.get("meterpolisDirectionsStoreStep")
                .<MeterpolisRouteInfo, MeterpolisDirections>chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(stepListener)
                .listener(chunkListener)
                .listener(processorListener)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    Step meterpolisDirectionsStatStep(
            @Autowired MeterpolisDirectionsReader reader,
            @Autowired MeterpolisDirectionProcessor processor,
            @Autowired MeterpolisDirectionStatWriter writer,
            @Autowired MeterpolisBatchSkipListener<MeterpolisBounds, MeterpolisRoutesInfo> skipListener
    ) {
        return sbf.get("meterpolisDirectionsStatStep")
                .<MeterpolisDirectionFilename, MeterpolisRouteDirectionStat>chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skip(EmptyDirectionsException.class)
                .skipLimit(3000) //replace with skipPolicy
                .listener(skipListener)
                .listener(processorListener)
                .listener(stepListener)
                .build();
    }

    @Bean
    Step meterpolisAggergateStats(
            @Autowired AggregatedStatsCalculatorTasklet tasklet
    ) {
        return sbf.get("meterpolisAggergateStats")
                .tasklet(tasklet)
                .listener(stepListener)
                .build();
    }

    @Bean
    Step meterpolisAverageHistory(
            @Autowired MeterpolisAggregatedStatsReader reader,
            @Autowired MeterpolisAggregatedStatsHistoryWriter writer
    ) {
        return sbf.get("meterpolisAverageHistory")
                .<MeterpolisDirectionStatAverage, MeterpolisDirectionStatAverage>chunk(chunkSize)
                .reader(reader)
                .writer(writer)
                .listener(stepListener)
                .build();
    }

    @Bean
    Step meterpolisOutputProducerStep(
            @Autowired MeterpolisAggregatedStatsCsvReader reader,
            @Autowired MeterpolisStatAverageHistoryProcessor processor,
            @Autowired ItemWriter<MeterpolisDirectionStatAverage> outputWriter
            ) {
        return sbf.get("meterpolisOutputProducerStep")
                .<MeterpolisDirectionStatAverageHistory, MeterpolisDirectionStatAverage>chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(outputWriter)
                .listener(stepListener)
                .build();
    }

}
