package it.simonericci97.github.meterpolis.meterpolis.services;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A service that handles files operations
 */
@Slf4j
@Service
public class MeterpolisFileService {

    @Autowired
    private Gson gson;

    /**
     * Create or replace a file
     *
     * @param path file path to write
     * @param content write to path
     */
    public void writeFile(String path, String content) {
        try {
            File f = new File(path);
            if(f.exists() || f.createNewFile()) {
                FileWriter myWriter = new FileWriter(path);
                myWriter.write(content);
                myWriter.close();
            } else {
                log.error("An error occurred creating file {}", path);
            }
        } catch (IOException e) {
            log.error("An error occurred writing file {}", path, e);
        }
    }

    /**
     *
     * @param path file path to read
     * @return a string with the file content
     */
    public String readFile(String path) {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(path));) {
            String currentLine = reader.readLine();
            while(currentLine != null  && currentLine.length() > 0) {
                sb.append(currentLine);
                currentLine = reader.readLine();
            }
        } catch (IOException e) {
            log.error("An error occurred reading file {}", path, e);
            return null;
        }
        return sb.toString();
    }

    /**
     *
     * @param path file path to read
     * @return a list of string. An element of the result list is a file line
     */
    public List<String> readFileLines(String path) {
        List<String> sb = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(path));) {
            String currentLine = reader.readLine();
            while(currentLine != null  && currentLine.length() > 0) {
                sb.add(currentLine);
                currentLine = reader.readLine();
            }
        } catch (IOException e) {
            log.error("An error occurred reading file {}", path, e);
            return null;
        }
        return sb;
    }

    /**
     *
     * @param path field to read
     * @param clazz class to deserialize
     * @param <T> type to be returned
     * @return a deserialization of file content
     */
    public <T> T readFile(String path, Class<T> clazz) {
        String content = Optional.ofNullable(this.readFile(path)).orElse("{}");
        return gson.fromJson(content, clazz);
    }

    /**
     *
     * @param dirPath path to a directory
     * @return a list of path  under the directory dirPath
     */
    public List<String> listDir(String dirPath) {
        File d = new File(dirPath);
        if(d.exists() && d.isDirectory()) {
            File[] content = Optional.ofNullable(d.listFiles()).orElse(new File[0]);

            return Arrays.asList(content).parallelStream()
                    .map(File::getName)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     *
     * @param dirPath path to a directory
     * @return a list of File  under the directory dirPath
     */
    public List<File> listFileDir(String dirPath) {
        File d = new File(dirPath);
        if(d.exists() && d.isDirectory()) {
            File[] content = Optional.ofNullable(d.listFiles()).orElse(new File[0]);

            return Arrays.asList(content);
        }
        return new ArrayList<>();
    }

    /**
     * Create a directory if not exists
     *
     * @param path to create directory
     */
    public void createDirIfNotExists(String path) {
        File d = new File(path);
        if(!d.exists() || !d.isDirectory()) {
            d.mkdirs();
        }
    }

    /**
     * Create a file if not exists
     *
     * @param path to create file
     */
    public void createFileIfNotExists(String path) {
        File f = new File(path);
        if(!f.exists() || !f.isFile()) {
            createDirIfNotExists(f.getParent());
            try {
                f.createNewFile();
            } catch (IOException e) {
                log.error("Cannot create file {}: {}", f.getAbsolutePath(), e.getMessage(), e);
            }
        }
    }

    /**
     *
     * @param path absolute path
     * @return filename indicated by path
     */
    public String extractFilename(String path) {
        return new File(path).getName();
    }
}
