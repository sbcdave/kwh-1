package org.kwh.tcp.util;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to parse our remote telemetry data.  Currently only parses an extract file.  May have to handle
 * UDP datagrams if we have to write our own listener
 */

public class Parser {

    /**
     * Parses a time string of the form: 'TM:1404120015'.
     * <p>
     * Note: Since we don't have timezone info we use LocalDateTime
     *
     * @param timeString a string of the form 'TM:08/10/2011,13:19:09'
     * @return a LocalDateTime instance
     */
	static Logger logger = LoggerFactory.getLogger(Parser.class);
    public static LocalDateTime parseDate(String timeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'TM:'MM'/'dd'/'yyyy','HH':'mm':'ss");
        return LocalDateTime.parse(timeString, formatter);

    }

    /**
     * Load the data from the file, transform into Record POJOs
     *
     * @param filePath the path to the file to parse
     * @return a List of Records
     *
     * @throws java.io.IOException when a parsing problem happens
     */
    public static List<Record> parseFile(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath)).
                filter(s -> !s.isEmpty()).
                map(Parser::toRecord).
                collect(Collectors.toList());
    }

    /**
     * Extract record metadata, channel data, and make a Record
     *
     * @param line a line of data
     * @return a Record
     */
    public static Record toRecord(String line) {
    	logger.info(line);
        String[] columns = line.split(";");
        String[] headers = columns[0].split(":");

        return new Record(headers[1],
                parseDate(columns[1]),
                Stream.of(Arrays.copyOfRange(columns, 2, columns.length - 1)).
                        collect(Collectors.
                                toMap(datumLine -> datumLine.split(":")[0],
                                        datumLine -> Double.parseDouble(datumLine.split(":")[1])))
        );
    }


}
