import Model.FaultTree;
import Parser.XMLToFT;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Kazako on 30.05.2015.
 */
@Configuration
@ComponentScan
public class main {

    static XMLToFT xmlToFT = new XMLToFT();
    static BDDTest bddTest = new BDDTest();
    public static void main(String[] args) {
        FaultTree faultTree = xmlToFT.parse();
        System.out.println(faultTree.getNode().getNodes().get(1).getNodes().get(1).getProbability());
        bddTest.quickTest();
    }
}
