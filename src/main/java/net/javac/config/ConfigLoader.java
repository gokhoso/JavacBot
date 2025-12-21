package net.javac.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ConfigLoader {
    private final static Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
    private static ModelConfig data;

    static {
        Yaml yaml = new Yaml();
        try (BufferedReader input = new BufferedReader(new FileReader("config.yml"))) {
            data = yaml.loadAs(input, ModelConfig.class);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static ModelConfig getData() {
        return data;
    }
}
