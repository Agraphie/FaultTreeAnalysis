import Calculators.CalculateDiscreteProbabilities;
import Calculators.CalculateMCProbabilities;
import Model.BDDWithProbabilities;
import Model.FaultTree;
import Parser.FTToBDD;
import Parser.XMLToFT;
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
    static Uhma uhma = new Uhma();
    public static void main(String[] args) {

        FaultTree faultTree = xmlToFT.parse(fileHandler.getFile());


        BDDWithProbabilities bdd = ftToBDD.parse(faultTree);
        RealMatrix failureMatrix = calculateMCProbabilities.calculateMCProbabilities(bdd.getInitialProbabilities().toArray(),
                bdd.getGeneratorMatrix(), bdd.getSamplingInterval(), bdd.getMissionTime());
        calculateMCProbabilities.foo(bdd.getBdd(), failureMatrix);

        calculateMCProbabilities.calculateTopEvent(bdd.getBdd(), failureMatrix);
    }


}
