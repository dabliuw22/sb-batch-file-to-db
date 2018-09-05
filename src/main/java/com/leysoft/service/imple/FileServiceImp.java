
package com.leysoft.service.imple;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.leysoft.model.Person;
import com.leysoft.service.inter.FileService;
import com.opencsv.CSVReader;

@Service
public class FileServiceImp implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImp.class);

    private CSVReader csvReader;

    private FileReader fileReader;

    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @Value(
            value = "${batch.file.path.tasklet}")
    private String path;

    @PostConstruct
    private void init() throws FileNotFoundException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        this.fileReader = new FileReader(file);
        this.csvReader = new CSVReader(fileReader);
    }

    @PreDestroy
    private void close() throws IOException {
        this.fileReader.close();
        this.csvReader.close();
    }

    @Override
    public Person read() {
        try {
            String[] line = csvReader.readNext();
            LOGGER.info("{},{}", line[0], line[1]);
            return new Person(line[0], formatter.parse(line[1]));
        } catch (Exception e) {
            LOGGER.error("error -> {}", e);
        }
        return null;
    }
}
