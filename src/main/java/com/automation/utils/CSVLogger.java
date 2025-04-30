package com.automation.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;

public class CSVLogger {
    private static final String CSV_FILE_PATH = System.getProperty("user.dir") + File.separator + "output" + File.separator + "output.csv";
    private CSVWriter csvWriter;
    private FileWriter fileWriter;

    public CSVLogger() {
        try {
            // Ensure the output directory exists
            File outputDir = new File(System.getProperty("user.dir") + File.separator + "output");
            if (!outputDir.exists()) {
                boolean dirCreated = outputDir.mkdirs();
                System.out.println("Output directory created: " + dirCreated);
            }

            boolean fileExists = new File(CSV_FILE_PATH).exists();
            fileWriter = new FileWriter(CSV_FILE_PATH, true); // Append mode
            csvWriter = (CSVWriter) new CSVWriterBuilder(fileWriter)
                    .withSeparator(',')
                    .withQuoteChar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                    .build();

            // Write headers only if the file is new
            if (!fileExists) {
                String[] header = {
                        "Skills Categories", "Sub-Categories", "Skills",
                        "Related Skills", "Related Titles", "Related Occupations"
                };
                csvWriter.writeNext(header);
                csvWriter.flush();
                System.out.println("CSV headers written to: " + CSV_FILE_PATH);
            }
        } catch (IOException e) {
            System.err.println("Error initializing CSVLogger: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void writeData(String skillsCategory, String subCategory, String skill,
                                       List<String> relatedSkills, List<String> relatedTitles,
                                       List<String> relatedOccupations) {
        try {
            String formattedSkillsCategory = "\"" + skillsCategory + "\"";
            String relatedSkillsStr = relatedSkills.isEmpty() ? "" : String.join(" | ", relatedSkills);
            String relatedTitlesStr = relatedTitles.isEmpty() ? "" : String.join(" | ", relatedTitles);
            String relatedOccupationsStr = relatedOccupations.isEmpty() ? "" : String.join(" | ", relatedOccupations);

            String[] rowData = {formattedSkillsCategory, subCategory, skill, relatedSkillsStr, relatedTitlesStr, relatedOccupationsStr};
            csvWriter.writeNext(rowData, false);
            csvWriter.flush();
            System.out.println("Wrote CSV row for skill: " + skill);
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