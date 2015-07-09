package Printer;

import org.apache.commons.math3.linear.RealMatrix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.valueOf;

/**
 * Created by Agraphie on 29.06.2015.
 */
public class CSVPrinter {

    public File printMatrixToCSV(RealMatrix realMatrix, String[] columnToVariableMapping, String path, String name) {
        File csvFile = new File(path + "\\" + name + ".csv");
        BufferedWriter writer;

        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            for (String aColumn : columnToVariableMapping) {
                writer.write(aColumn);
                writer.write(";");
            }
            writer.newLine();
            for (int i = 0; i < realMatrix.getRowDimension(); i++) {
                String line = rowToString(realMatrix.getRow(i));
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("File path or name not correct.");
            e.printStackTrace();
        }
        return csvFile;
    }

    private String rowToString(double[] row) {
        StringBuilder builder = new StringBuilder();
        for (double aEntry : row) {
            builder.append(valueOf(aEntry).setScale(5, ROUND_HALF_UP).toPlainString());
            builder.append(";");
        }
        return builder.toString();
    }
}
