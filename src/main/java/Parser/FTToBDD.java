package Parser;

import Model.BDDWithProbabilities;
import Model.FaultTree;
import Model.Node;
import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

/**
 * Created by Kazako on 31.05.2015.
 */
public class FTToBDD {
    private BDDFactory bddFactory;
    private int counter = 0;
    private double[] probabilitiesTrue;
    private double[] probabilitiesFalse;

    public BDDWithProbabilities parse(FaultTree faultTree) {
        int varNum = faultTree.getVarNum();
        bddFactory = BDDFactory.init(faultTree.getVarNum(), varNum);
        bddFactory.setVarNum(varNum);
        probabilitiesTrue = new double[varNum];
        probabilitiesFalse = new double[varNum];

        Node tempNode = faultTree.getNode();
        BDD bdd = build(tempNode);

        BDDWithProbabilities bddWithProbabilities = new BDDWithProbabilities(bdd, probabilitiesTrue, probabilitiesFalse);

        return bddWithProbabilities;
    }

    private BDD build(Node node) {
        BDD bdd = null;

        if (node.getType() == Node.Type.gate) {
            if (node.getOperator() == Node.Operator.AND) {
                bdd = build(node.getNodes().get(0)).and(build(node.getNodes().get(1)));
            } else if (node.getOperator() == Node.Operator.OR) {
                bdd = build(node.getNodes().get(0)).or(build(node.getNodes().get(1)));
            }
        } else if (node.getType() == Node.Type.event) {
            bdd = bddFactory.ithVar(counter);
            probabilitiesTrue[counter] = node.getProbability();
            probabilitiesFalse[counter] = 1 - node.getProbability();
            counter++;
        }

        return bdd;
    }
}
