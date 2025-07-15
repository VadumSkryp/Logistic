package stringutils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SpecialWordCounter {

    private static final Logger logger = LogManager.getLogger(SpecialWordCounter.class);

    public static void main(String[] args) {
        logger.info("Application started.");


        ClassLoader classLoader = SpecialWordCounter.class.getClassLoader();
        URL resource = classLoader.getResource("input.txt");

        if (resource == null) {
            logger.error("File input.txt not found in resources!");
            return;
        }

        File inputFile = new File(resource.getFile());


        File resourceDir = new File("src/main/resources");
        File outputFile = new File(resourceDir, "output.txt");

        try {
            logger.info("Reading input file: {}", inputFile.getAbsolutePath());
            String content = FileUtils.readFileToString(inputFile, StandardCharsets.UTF_8);

            String[] words = content.split("\\W+");

            long count = Arrays.stream(words)
                    .filter(StringUtils::isAlpha)
                    .filter(word -> word.length() > 7)
                    .count();

            String result = "Special words found: " + count + System.lineSeparator();
            FileUtils.writeStringToFile(outputFile, result, StandardCharsets.UTF_8, true);

            logger.info("Result saved to file: {}", outputFile.getAbsolutePath());
            logger.info("Special words count: {}", count);

        } catch (IOException e) {
            logger.error("Error reading or writing files: ", e);
        }

        logger.info("Application finished.");
    }
}

