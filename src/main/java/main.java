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
    static CalculateProbability calculateProbability = new CalculateProbability();

    public static void main(String[] args) {
        FaultTree faultTree = xmlToFT.parse();
        System.out.println(faultTree.getNode().getNodes().get(1).getNodes().get(1).getProbability());
        BDDWithProbabilities bdd = ftToBDD.parse(faultTree);
        System.out.println(calculateProbability.calculateSystemFailure(bdd));


        //   bddTest.quickTest();
    }
}
