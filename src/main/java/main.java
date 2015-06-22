import Model.BDDWithProbabilities;
import Model.FaultTree;
import Parser.FTToBDD;
import Parser.XMLToFT;

/**
 * Created by Kazako on 30.05.2015.
 */
public class main {

    static XMLToFT xmlToFT = new XMLToFT();
    static FTToBDD ftToBDD = new FTToBDD();
    static CalculateMCProbabilities calculateMCProbabilities = new CalculateMCProbabilities();
    static FileHandler fileHandler = new FileHandler();

    public static void main(String[] args) {

        FaultTree faultTree = xmlToFT.parse(fileHandler.getFile());
        System.out.println(faultTree.getNode().getNodes().get(1).getNodes().get(1).getProbability());
        BDDWithProbabilities bdd = ftToBDD.parse(faultTree);

        double[] initialProbabilities = {0.9, 0.1, 0};
        double[][] generatorMatrix = {{-0.2, 0.2,0},{0.1,-0.2,0.1},{0,0.05,-0.05}};

        calculateMCProbabilities.calculateMCProbabilities(initialProbabilities, generatorMatrix, 0.5, 40);


        //   bddTest.quickTest();
    }


}
