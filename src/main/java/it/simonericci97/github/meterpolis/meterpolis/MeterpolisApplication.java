package it.simonericci97.github.meterpolis.meterpolis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import it.simonericci97.github.meterpolis.meterpolis.tools.MeterpolisArgumentParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@Slf4j
@SpringBootApplication
@EnableBatchProcessing
public class MeterpolisApplication {

	public static void main(String[] args) {
		MeterpolisArgumentParser parser = new MeterpolisArgumentParser();
		String[] metropolisNames = parser.parse(args);

		ApplicationContext ctx = SpringApplication.run(MeterpolisApplication.class, args);

		//String metropolisParams = "Paris,Rome,Milan,Turin,London,Berlin";

		JobLauncher jobLauncher = (JobLauncher) ctx.getBean("jobLauncher");
		Job prepareDataJob = (Job) ctx.getBean("meterpolisJob");
		Job outputJob = (Job) ctx.getBean("outputProducerJob");


		for (String metropolisName : metropolisNames) {
			JobParameters jobParameters = new JobParametersBuilder()
					.addLong("time", System.currentTimeMillis())
					.addString("metropolis", metropolisName)
					.toJobParameters();
			jobRun(jobLauncher, prepareDataJob, jobParameters);
		}

		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters();
		jobRun(jobLauncher, outputJob, jobParameters);

		System.exit(0);
	}

	private static void jobRun(JobLauncher jobLauncher, Job job, JobParameters jobParameter) {
		try {
			log.info("Running job {} with parameters {}", job.getName(), jobParameter.getString("metropolis"));
			jobLauncher.run(job, jobParameter);
			log.info("Run job {} with parameters {}", job.getName(), jobParameter.getString("metropolis"));
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			log.error("Cannot run job {}.", job.getName(), e);
		}
	}

}
