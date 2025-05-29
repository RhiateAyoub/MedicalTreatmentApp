package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter {

    public static void exportToCSV(String filePath, List<String> headers, List<List<String>> rows) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write headers
            writer.append(String.join(",", headers));
            writer.append("\n");

            // Write rows
            for (List<String> row : rows) {
                writer.append(String.join(",", row));
                writer.append("\n");
            }

            System.out.println("CSV export completed: " + filePath);
        } catch (IOException e) {
            System.out.println("Error exporting CSV: " + e.getMessage());
        }
    }
}