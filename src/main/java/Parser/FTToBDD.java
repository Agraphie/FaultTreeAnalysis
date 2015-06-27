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
    private HashMap<Integer, Integer> markovChains;
    private String[] variableMapping;

    public BDDWithProbabilities parse(FaultTree faultTree) {
        int varNum = faultTree.getVarNum();
        bddFactory = BDDFactory.init(faultTree.getVarNum(), varNum);
        bddFactory.setVarNum(varNum);
        isContinuous = faultTree.isContinuousMC();
        variableMapping = new String[varNum + 1];
        alreadyVisitedNodes = new HashMap<>();
        Node tempNode = faultTree.getNode();
        BDD bdd;
        BDDWithProbabilities bddWithProbabilities;

        if (isContinuous) {
            generatorMatrix = MatrixUtils.createRealMatrix(varNum, varNum);
            initialProbabilities = generatorMatrix.getRowVector(0);
            markovChains = new HashMap<>();
            bdd = build(tempNode);

            double tempSum;
            int rowDimension = generatorMatrix.getRowDimension();
            int counter = 0;
            for (int i = 0; i < rowDimension; i++) {
                tempSum = 0;
                for (int j = 0; j < generatorMatrix.getColumnDimension(); j++) {
                    if (i != j) {
                        tempSum += generatorMatrix.getEntry(i, j);
                    }
                    counter = checkMarkovChain(counter, i, j);
                }

                if (!markovChains.containsKey(i)) {
                    markovChains.put(i, counter);
                    counter++;
                }
                generatorMatrix.setEntry(i, i, -tempSum);
            }

            variableMapping[varNum] = "Top event";
            bddWithProbabilities = new BDDWithProbabilities(bdd, generatorMatrix, initialProbabilities, isContinuous,
                    faultTree.getMissionTime(), faultTree.getSamplingInterval(), markovChains, variableMapping);
        } else {
            bdd = build(tempNode);
            bddWithProbabilities = new BDDWithProbabilities(bdd, null, initialProbabilities, isContinuous, 0, 0, markovChains, variableMapping);
        }

        return bddWithProbabilities;
    }

    /**
     * Checks whether an entry is in an markov chain already or not. If an entry != 0, it means, the row has a transition to the column (but not vice versa!)
     * If this is the case, then put the row and the column in one markov chain. If there is an entry whose row OR whose column is already in the map,
     * then the row i.e. the column needs to be in the same chain.
     *
     * @param counter
     * @param i
     * @param j
     * @return
     */
    private int checkMarkovChain(int counter, int i, int j) {
        if (generatorMatrix.getEntry(i, j) != 0) {
            if (markovChains.containsKey(i)) {
                markovChains.put(j, markovChains.get(i));
            } else if (markovChains.containsKey(j)) {
                markovChains.put(i, markovChains.get(j));
            } else {
                markovChains.put(i, counter);
                markovChains.put(j, counter);
                counter++;
            }
        }
        return counter;
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
                    variableMapping[counter] = node.getName();
                }
            }
        }
        alreadyVisitedNodes.put(id, bdd);

        return bdd;
    }
}
