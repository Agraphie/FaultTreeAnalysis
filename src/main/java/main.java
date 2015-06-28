import Calculators.CalculateDiscreteProbabilities;
import Calculators.CalculateMCProbabilities;
import Chart.Chart;
import Model.BDDWithProbabilities;
import Model.FaultTree;
import Parser.FTToBDD;
import Parser.XMLToFT;
import Printer.Printer;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Created by Kazako on 30.05.2015.
 */
public class main {

    static XMLToFT xmlToFT = new XMLToFT();
    static FTToBDD ftToBDD = new FTToBDD();
    static CalculateMCProbabilities calculateMCProbabilities = new CalculateMCProbabilities();
    static CalculateDiscreteProbabilities calculateDiscreteProbabilities = new CalculateDiscreteProbabilities();
    static FileHandler fileHandler = new FileHandler();
    static Printer printer = new Printer();
    static Chart chart = new Chart("Probabilities");
    public static void main(String[] args) {

        FaultTree faultTree = xmlToFT.parse(fileHandler.getFile());


        BDDWithProbabilities bdd = ftToBDD.parse(faultTree);
        RealMatrix failureMatrix = calculateMCProbabilities.calculateMCProbabilities(bdd.getInitialProbabilities().toArray(),
                bdd.getGeneratorMatrix(), bdd.getSamplingInterval(), bdd.getMissionTime());


        RealMatrix failureMatrixWithTE = calculateMCProbabilities.calculateTopEvent(bdd.getBdd(), failureMatrix, bdd.getMarkovChains());
        //chart.printChart(failureMatrixWithTE, bdd.getColumnToVariableMapping(), bdd.getSamplingInterval(), bdd.getMissionTime());
        System.out.println(failureMatrixWithTE.toString());

        //   printer.printBDDToDOT(bdd.getBdd(), new File("C:\\Users\\Kazakor\\Documents\\newfile.txt"));
    }


}
