package it.simonericci97.github.meterpolis.meterpolis.services;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * A service which handles paths used by meterpolis batch
 */

@Service
public class MeterpolisPathsManager implements InitializingBean {

    @Value("${meterpolis.base.temp.path}")
    private String baseTempPath;

    @Value("${meterpolis.base.out.path}")
    private String baseOutPath;

    @Value("${meterpolis.temp.bound.dir}")
    private String boundsDir;

    @Value("${meterpolis.temp.routes.dir}")
    private String routesDir;

    @Value("${meterpolis.temp.directions.dir}")
    private String directionsDir;

    @Value("${meterpolis.temp.stats.dir}")
    private String statsDir;

    @Value("${meterpolis.temp.stats.dir}")
    private String historyDir;

    @Autowired
    private MeterpolisFileService fileService;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(fileService == null) throw new IllegalStateException("Missing MeterpolisFileService");
        statsDir = fixPathPropertiy(statsDir);
        directionsDir = fixPathPropertiy(directionsDir);
        routesDir = fixPathPropertiy(routesDir);
        boundsDir = fixPathPropertiy(boundsDir);
        baseTempPath = fixPathPropertiy(baseTempPath);
    }

    public String  fixPathPropertiy(String path) {
        if(!path.endsWith("/")) {
            path += "/";
        }
        return path;
    }

    public String getMetropolisBoundsTempPath(String meterpolisName) {
        return getBoundsTempDir() + meterpolisName + ".temp.json";
    }

    public String getMetropolisRoutesTempPath(String meterpolisName) {
        return getRoutesTempDir() + meterpolisName + ".temp.json";
    }

    public String getMetropolisDirectioonsTempPath(String meterpolisName) {
        return getDirectionsTempDir(meterpolisName) + meterpolisName + ".temp.json";
    }

    public String getMetropolisDirectioonsIncrementorTempPath(String metropolisName) {
        String path = this.getDirectionsTempDir(metropolisName);
        fileService.createDirIfNotExists(path);
        List<String> matchingFiles = fileService.listDir(path);
        int incrementor = matchingFiles.parallelStream()
                .map(filename -> this.extractIncrementorByFilename(metropolisName, filename))
                .max(Comparator.naturalOrder()).orElse(0) + 1;
        String result = getDirectionsTempDir(metropolisName) + metropolisName + "_" + incrementor + ".temp.json";
        fileService.createFileIfNotExists(result);
        return result;
    }

    public synchronized String getMetropolisStatsIncrementorTempPath(String metropolisName, String referenceFilename) {
        String path = this.getStatsTempDir(metropolisName);
        fileService.createDirIfNotExists(path);
        return getStatsTempDir(metropolisName) + referenceFilename;
    }

    public String getMetropolisStatsTempPath(String metropolisName) {
        return this.getStatsTempDir(metropolisName) + metropolisName + ".temp.json";
    }

    public String getMetropolisStatsHistoryPath(String metropolisName) {
        return this.getStatsHistoryDir(metropolisName) + metropolisName + ".csv";
    }

    public String getBoundsTempDir() {
        return this.baseTempPath + boundsDir;
    }

    public String getRoutesTempDir() {
        return this.baseTempPath + routesDir;
    }

    public String getDirectionsTempDir(String metropolisName) {
        return this.baseTempPath + directionsDir + metropolisName + "/";
    }

    public String getStatsTempDir(String metropolisName) {
        return this.baseTempPath + statsDir + metropolisName + "/";
    }

    public String getStatsHistoryDir(String metropolisName) {
        return this.baseOutPath + metropolisName + "/" + historyDir;
    }

    public String getStatsTempDir() {
        return this.baseTempPath + statsDir;
    }

    public String getDirectionsTempDir() {
        return this.baseTempPath + directionsDir;
    }

    public Integer extractIncrementorByFilename(String metropolisName, String filename) {
        return Integer.valueOf(filename.substring((metropolisName + "_").length(), filename.indexOf(".temp.json")));
    }
}
