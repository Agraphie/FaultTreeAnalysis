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
    private HashMap<String, BDD> alreadyVisitedNodes;
    private RealMatrix generatorMatrix;
    private RealVector initialProbabilities;
    private boolean isContinuous;

    public BDDWithProbabilities parse(FaultTree faultTree) {
        int varNum = faultTree.getVarNum();
        bddFactory = BDDFactory.init(faultTree.getVarNum(), varNum);
        bddFactory.setVarNum(varNum);
        isContinuous = faultTree.isContinuousMC();

        System.out.println(varNum + " varnum");
        alreadyVisitedNodes = new HashMap<>();
        Node tempNode = faultTree.getNode();
        BDD bdd;
        BDDWithProbabilities bddWithProbabilities;

        if (isContinuous) {
            generatorMatrix = MatrixUtils.createRealMatrix(varNum, varNum);
            initialProbabilities = generatorMatrix.getRowVector(0);
            bdd = build(tempNode);
            double tempSum;
            int rowDimension = generatorMatrix.getRowDimension();

            for (int i = 0; i < rowDimension; i++) {
                tempSum = 0;
                for (int j = 0; j < rowDimension; j++) {
                    if (i != j) {
                        tempSum += generatorMatrix.getEntry(i, j);
                    }
                }
                generatorMatrix.setEntry(i, i, -tempSum);
            }
            bddWithProbabilities = new BDDWithProbabilities(bdd, generatorMatrix, initialProbabilities, isContinuous, faultTree.getMissionTime(), faultTree.getSamplingInterval());
        } else {
            bdd = build(tempNode);
            bddWithProbabilities = new BDDWithProbabilities(bdd, null, initialProbabilities, isContinuous, 0, 0);
        }

        return bddWithProbabilities;
    }


    private BDD build(Node node) {
        BDD bdd = null;
        String id = node.getId();

        if (alreadyVisitedNodes.containsKey(id)) {
            bdd = alreadyVisitedNodes.get(id);
        } else if (node.getType() == Node.Type.gate) {
            if (node.getOperator() == Node.Operator.AND) {
                bdd = build(node.getNodes().get(0));
                for (int i = 1; i < node.getNodes().size(); i++) {
                    bdd = bdd.and(build(node.getNodes().get(i)));
                }
            } else if (node.getOperator() == Node.Operator.OR) {
                bdd = build(node.getNodes().get(0));
                for (int i = 1; i < node.getNodes().size(); i++) {
                    bdd = bdd.or(build(node.getNodes().get(i)));
                }
            }
        } else if (node.getType() == Node.Type.event) {
            counter = Integer.parseInt(node.getId()) - 1;
            bdd = bddFactory.ithVar(counter);
            if (isContinuous) {
                if (node.getProbabilities() != null) {
                    generatorMatrix.setRow(counter, node.getProbabilities());
                    initialProbabilities.setEntry(counter, node.getInitialProbability());
                }
            }
        }
        alreadyVisitedNodes.put(id, bdd);

        return bdd;
    }
}
