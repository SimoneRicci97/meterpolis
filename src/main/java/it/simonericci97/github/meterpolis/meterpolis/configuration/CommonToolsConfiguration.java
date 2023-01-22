package it.simonericci97.github.meterpolis.meterpolis.configuration;

import ch.qos.logback.contrib.jackson.JacksonJsonFormatter;
import ch.qos.logback.contrib.json.classic.JsonLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisBounds;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionFilename;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisDirectionStatAverage;
import it.simonericci97.github.meterpolis.meterpolis.models.MeterpolisRoutesInfo;
import it.simonericci97.github.meterpolis.meterpolis.tools.ZoneIdTypeAdapter;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.io.Writer;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.function.Function;

@Configuration
public class CommonToolsConfiguration {

    private static final String[] CACHE_NAMES = {
            "routes"
    };

    @Autowired
    ZoneIdTypeAdapter zoneIdTypeAdapter;

    @Value("${meterpolis.stats.csv.header}")
    String[] statCsvHeader;

    @Value("${meterpolis.global.stats.csv.header}")
    String[] globalStatCsvHeader;

    @Value("${meterpolis.global.stats.path}")
    String outputPath;

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ZoneId.class, zoneIdTypeAdapter)
                .create();
    }

    @Bean
    public ConcurrentMapCacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(Arrays.asList(CACHE_NAMES));
        return cacheManager;
    }

    @Bean(name = "avgStatLineAggregatorHistory")
    public DelimitedLineAggregator<MeterpolisDirectionStatAverage> avgStatLineAggregatorHistory() {
        DelimitedLineAggregator<MeterpolisDirectionStatAverage> dla = new DelimitedLineAggregator<MeterpolisDirectionStatAverage>();
        dla.setDelimiter(";");

        BeanWrapperFieldExtractor<MeterpolisDirectionStatAverage> fe = new BeanWrapperFieldExtractor<>();
        fe.setNames(statCsvHeader);

        dla.setFieldExtractor(fe);
        return dla;
    }

    @Bean(name = "avgStatLineAggregator")
    public DelimitedLineAggregator<MeterpolisDirectionStatAverage> avgStatLineAggregator() {
        DelimitedLineAggregator<MeterpolisDirectionStatAverage> dla = new DelimitedLineAggregator<MeterpolisDirectionStatAverage>();
        dla.setDelimiter(";");

        BeanWrapperFieldExtractor<MeterpolisDirectionStatAverage> fe = new BeanWrapperFieldExtractor<>();
        fe.setNames(globalStatCsvHeader);

        dla.setFieldExtractor(fe);
        return dla;
    }

    @Bean
    public DefaultLineMapper<MeterpolisDirectionStatAverage> avgStatLineMapper(
            @Value("${meterpolis.stats.csv.header}") String[] statCsvHeader
        ) {
        DelimitedLineTokenizer dlt = new DelimitedLineTokenizer();
        dlt.setDelimiter(";");
        dlt.setNames(statCsvHeader);

        BeanWrapperFieldSetMapper<MeterpolisDirectionStatAverage> fsm = new BeanWrapperFieldSetMapper<>();
        fsm.setTargetType(MeterpolisDirectionStatAverage.class);

        DefaultLineMapper<MeterpolisDirectionStatAverage> dlm = new DefaultLineMapper<>();
        dlm.setFieldSetMapper(fsm);
        dlm.setLineTokenizer(dlt);
        return dlm;
    }

    @Bean
    public ItemWriter<MeterpolisDirectionStatAverage> outputWriter(
            @Autowired @Qualifier("avgStatLineAggregator") DelimitedLineAggregator<MeterpolisDirectionStatAverage> avgStatLineAggregator
    ) {
        return new FlatFileItemWriterBuilder<MeterpolisDirectionStatAverage>()
                .name("outputWriter")
                .lineAggregator(avgStatLineAggregator)
                .headerCallback(w -> w.write(String.join(";", globalStatCsvHeader)))
                .resource(new FileSystemResource(outputPath))
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(15);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setQueueCapacity(30);
        return taskExecutor;
    }

}
