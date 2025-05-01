package com.automation.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;

public class CSVLogger {
    private final String CSV_FILE_PATH;
    private CSVWriter csvWriter;
    private FileWriter fileWriter;

    public CSVLogger() {
        try {
            // Generate timestamped filename (e.g., output_20250501_143022.csv)
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            CSV_FILE_PATH = System.getProperty("user.dir") + File.separator + "output" + File.separator + "output_" + timestamp + ".csv";

            // Ensure the output directory exists
            File outputDir = new File(System.getProperty("user.dir") + File.separator + "output");
            if (!outputDir.exists()) {
                boolean dirCreated = outputDir.mkdirs();
                System.out.println("Output directory created: " + dirCreated);
            }

            // Create a new file (overwrite mode)
            fileWriter = new FileWriter(CSV_FILE_PATH, false); // Overwrite mode
            csvWriter = (CSVWriter) new CSVWriterBuilder(fileWriter)
                    .withSeparator(',')
                    .withQuoteChar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                    .build();

            // Write headers
            String[] header = {
                    "Career Areas", "Occupation Group", "Occupations",
                    "Specialized Occupations", "Skills", "Titles", "Related Occupations"
            };
            csvWriter.writeNext(header);
            csvWriter.flush();
            System.out.println("CSV headers written to new file: " + CSV_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error initializing CSVLogger: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize CSVLogger", e);
        }
    }

    public synchronized void writeData(String careerArea, String occupationGroup, String occupation,
                                       String specializedOccupation, List<String> skills,
                                       List<String> titles, List<String> relatedOccupations) {
        try {
            // Format fields, ensuring proper quoting
            String formattedCareerArea = "\"" + (careerArea != null ? careerArea.replace("\"", "\"\"") : "") + "\"";
            String formattedOccupationGroup = "\"" + (occupationGroup != null ? occupationGroup.replace("\"", "\"\"") : "") + "\"";
            String formattedOccupation = "\"" + (occupation != null ? occupation.replace("\"", "\"\"") : "") + "\"";
            String formattedSpecializedOccupation = "\"" + (specializedOccupation != null ? specializedOccupation.replace("\"", "\"\"") : "") + "\"";
            String skillsStr = skills.isEmpty() ? "" : String.join(" | ", skills);
            String titlesStr = titles.isEmpty() ? "" : String.join(" | ", titles);
            String relatedOccupationsStr = relatedOccupations.isEmpty() ? "" : String.join(" | ", relatedOccupations);

            String[] rowData = {
                    formattedCareerArea, formattedOccupationGroup, formattedOccupation,
                    formattedSpecializedOccupation, skillsStr, titlesStr, relatedOccupationsStr
            };
            csvWriter.writeNext(rowData, false);
            csvWriter.flush();
            System.out.println("Wrote CSV row for specialized occupation: " + specializedOccupation + " to file: " + CSV_FILE_PATH);
        } catch (Exception e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (csvWriter != null) {
                csvWriter.close();
                System.out.println("CSVWriter closed");
            }
            if (fileWriter != null) {
                fileWriter.close();
                System.out.println("FileWriter closed");
            }
        } catch (IOException e) {
            System.err.println("Error closing CSVLogger: " + e.getMessage());
            e.printStackTrace();
        }
    }
}