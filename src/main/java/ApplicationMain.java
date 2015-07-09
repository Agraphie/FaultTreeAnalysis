import Calculators.CalculateDiscreteProbabilities;
import Calculators.CalculateMCProbabilities;
import Chart.Chart;
import Model.BDDWithProbabilities;
import Model.FaultTree;
import Parser.FTToBDD;
import Parser.XMLToFT;
import Printer.CSVPrinter;
import Printer.DOTPrinter;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.*;

/**
 * Created by Kazako on 30.05.2015.
 */
public class ApplicationMain {

    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static XMLToFT xmlToFT = new XMLToFT();
    static FTToBDD ftToBDD = new FTToBDD();
    static CalculateMCProbabilities calculateMCProbabilities = new CalculateMCProbabilities();
    static CalculateDiscreteProbabilities calculateDiscreteProbabilities = new CalculateDiscreteProbabilities();
    static FileHandler fileHandler = new FileHandler();
    static DOTPrinter DOTPrinter = new DOTPrinter();
    static Chart chart = new Chart("Probabilities");
    static CSVPrinter csvPrinter = new CSVPrinter();
    private static PrintStream originalStream = System.out;
    private static RealMatrix failureMatrixWithTE;
    private static BDDWithProbabilities bdd;

    public static void main(String[] args) {
        System.out.println();
        System.out.print("Enter the path to an xml file if program is executed as JAR, otherwise press enter: ");
        String path = "";
        try {
            path = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
        hideOutput();

        FaultTree faultTree;
        if (!path.isEmpty()) {
            faultTree = xmlToFT.parse(fileHandler.getFile(path));
        } else {
            faultTree = xmlToFT.parse(fileHandler.getFile());
        }


        bdd = ftToBDD.parse(faultTree);
        RealMatrix failureMatrix = calculateMCProbabilities.calculateMCProbabilities(bdd.getInitialProbabilities().toArray(),
                bdd.getGeneratorMatrix(), bdd.getSamplingInterval(), bdd.getMissionTime());
        failureMatrixWithTE = calculateMCProbabilities.calculateTopEvent(bdd.getBdd(), failureMatrix, bdd.getMarkovChains());


        showOutput();
        try {
            menu();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showOutput() {
        System.setOut(originalStream);
    }

    private static void hideOutput() {
        PrintStream fakeStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        });
        System.setOut(fakeStream);
        System.setErr(fakeStream);
    }

    private static void menu() throws IOException {
        boolean loop = true;
        while (loop) {
            printMenu();
            int input = 0;
            try {
                input = Integer.parseInt(br.readLine());
                System.out.println("");
            } catch (NumberFormatException e) {
                System.out.println("Input is not a valid number!");
                System.out.println("");
                continue;
            }
            switch (input) {
                case 0:
                    loop = false;
                    break;
                case 1:
                    printGraph();
                    break;
                case 2:
                    printDot();
                    break;
                case 3:
                    printCSV();
                    break;
            }
        }
    }

    private static void printCSV() throws IOException {
        System.out.print("Enter a path where the file should be saved: ");
        String path = br.readLine();
        System.out.print("Enter file name: ");
        String fileName = br.readLine();
        csvPrinter.printMatrixToCSV(failureMatrixWithTE, bdd.getColumnToVariableMapping(), path, fileName);
    }

    private static void printDot() throws IOException {
        System.out.print("Enter a path where the file should be saved: ");
        String path = br.readLine();
        System.out.print("Enter file name: ");
        String fileName = br.readLine();
        DOTPrinter.printBDDToDOT(bdd.getBdd(), path, fileName);
    }

    private static void printGraph() {
        chart.printChart(failureMatrixWithTE, bdd.getColumnToVariableMapping(), bdd.getSamplingInterval(), bdd.getMissionTime());
    }

    private static void printMenu() {
        System.out.println("------Menu------");
        System.out.println("Enter 1 for displaying the resulted graph");
        System.out.println("Enter 2 for printing the result to a dot file");
        System.out.println("Enter 3 for printing the result to a csv file");
        System.out.println("Enter 0 to exit the program");
        System.out.print("Your choice: ");
    }

}
