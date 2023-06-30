import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

public class CSVFileManager {
    private String filePath;


    public CSVFileManager(String filePath) {
        this.filePath = filePath;
    }


    public void createCSVFile() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            System.out.println("Die CSV-Datei wurde erfolgreich erstellt: " + filePath);
        } catch (IOException e) {
            System.out.println("Fehler beim Erstellen der CSV-Datei: " + e.getMessage());
        }
    }

    public void appendDataToCSV(List<double[]> data) throws IOException {
        FileWriter writer = new FileWriter(filePath, true);

        for (double[] row : data) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < row.length; i++) {
                sb.append(row[i]);
                if (i < row.length - 1) {
                    sb.append(",");
                }
            }
            sb.append("\n");
            writer.write(sb.toString());
        }

        writer.close();
    }

    public void deleteCSVFile() {
        try {
            boolean deleted = java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(filePath));
            if (deleted) {
                System.out.println("Die CSV-Datei wurde erfolgreich gelöscht: " + filePath);
            } else {
                System.out.println("Die CSV-Datei konnte nicht gelöscht werden: " + filePath);
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Löschen der CSV-Datei: " + e.getMessage());
        }
    }

    public static List<double[]> convertPointListToDoubleArray(List<Point> pointList) {
        List<double[]> doubleArrayList = new ArrayList<>();

        for (Point point : pointList) {
            double[] coordinates = new double[2];
            coordinates[0] = point.getX();
            coordinates[1] = point.getY();
            doubleArrayList.add(coordinates);
        }
        return doubleArrayList;
    }

    public String getFilePath() {
        File file = new File(filePath);
        return file.getAbsolutePath();
    }
}
