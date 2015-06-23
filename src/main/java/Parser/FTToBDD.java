package Parser;

import Model.BDDWithProbabilities;
import Model.FaultTree;
import Model.Node;
import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.HashMap;

/**
 * Created by Agraphie on 31.05.2015.
 */
public class FTToBDD {
    private BDDFactory bddFactory;
    private int counter = 0;
    private HashMap<Integer, BDD> alreadyVisitedNodes;
    private RealMatrix generatorMatrix;
    private RealVector initialProbabilities;
    private boolean isContinuous;

    public BDDWithProbabilities parse(FaultTree faultTree) {
        int varNum = faultTree.getVarNum();
        bddFactory = BDDFactory.init(faultTree.getVarNum(), varNum);
        bddFactory.setVarNum(varNum);
        isContinuous = faultTree.isContinuousMC();

        alreadyVisitedNodes = new HashMap<>();
        initialProbabilities = MatrixUtils.createRealVector(faultTree.getInitialProbabilities());
        Node tempNode = faultTree.getNode();
        BDD bdd;
        BDDWithProbabilities bddWithProbabilities;

        if (isContinuous) {
            generatorMatrix = MatrixUtils.createRealMatrix(varNum, varNum);
            bdd = build(tempNode);
            bddWithProbabilities = new BDDWithProbabilities(bdd, generatorMatrix, initialProbabilities, isContinuous, faultTree.getMissionTime(), faultTree.getSamplingInterval());
        } else {
            bdd = build(tempNode);
            bddWithProbabilities = new BDDWithProbabilities(bdd, null, initialProbabilities, isContinuous, 0, 0);
        }



        return bddWithProbabilities;
    }


    private BDD build(Node node) {
        BDD bdd = null;
        Integer hashValue = node.hashCode();

        if(alreadyVisitedNodes.containsKey(hashValue)){
            bdd = alreadyVisitedNodes.get(hashValue);
        } else if (node.getType() == Node.Type.gate) {
            if (node.getOperator() == Node.Operator.AND) {
                bdd = build(node.getNodes().get(0)).and(build(node.getNodes().get(1)));
            } else if (node.getOperator() == Node.Operator.OR) {
                bdd = build(node.getNodes().get(0)).or(build(node.getNodes().get(1)));
            }
        } else if (node.getType() == Node.Type.event) {
            bdd = bddFactory.ithVar(counter);
            if (isContinuous) {
                if (node.getProbabilities() != null) {
                    generatorMatrix.setRow(counter, node.getProbabilities());
                }
            }
            counter++;
        }
        alreadyVisitedNodes.put(bdd.hashCode(), bdd);

        return bdd;
    }
}
