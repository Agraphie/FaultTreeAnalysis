package Calculators;

import net.sf.javabdd.BDD;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.math3.util.CombinatoricsUtils.factorial;

/**
 * Created by Agraphie on 22.06.2015.
 */
public class CalculateMCProbabilities {

    RealMatrix meh;
    List<HashSet<BDD>> mc = new LinkedList<>();
    HashMap<Integer, Double> visited = new HashMap<>();

    public CalculateMCProbabilities() {

    }

    /**
     * Calculates the probability matrix for continous markov chains by using uniformization,
     * @see <a href="https://en.wikipedia.org/wiki/Uniformization_%28probability_theory%29">Uniformization (probability theory</a>
     * First, lambda gets calculated. The column count of the probability matrix is missionTime / samplingInterval + 1. +1 because of the first row,
     * which is the initial probability vector.
     * Then the transition matrix P gets calculated by adding the Identity Matrix to the generator matrix which was multiplied by 1 / lambda.
     * Next, the multiplication matrix Pi(0)*P gets calculated, where a row is the multiplication vector for the iteration n.
     * At last every row in the result is added to the result of the previous iteration to approximate the probability.
     * @param initialProbabilities The vector of the initial probabilities.
     * @param generatorMatrix The generator matrix of the states.
     * @param samplingInterval The sampling interval, or in which intervals t changes.
     * @param missionTime The complete mission time till what t the probabilities should be calculated.
     * @return The probability matrix as RealMatrix, where the row are the probabilities at time t and the columns are the states.
     */
    public RealMatrix calculateMCProbabilities(double[] initialProbabilities, RealMatrix generatorMatrix, double samplingInterval, double missionTime) {
        if (missionTime == 0) {
            return MatrixUtils.createRowRealMatrix(initialProbabilities);
        }
        Integer matrixEntries = ((Double) (missionTime / samplingInterval)).intValue();
        int iterations = 20;
        double lambda = getLambda(generatorMatrix);
        double tempMultiplier;
        int iterationPosition = 1;
        int matrixColumnCount = initialProbabilities.length;

        RealMatrix multiplicationMatrix = MatrixUtils.createRealMatrix(iterations, matrixColumnCount);
        RealMatrix result = MatrixUtils.createRealMatrix(matrixEntries + 1, matrixColumnCount);
        RealVector initialProbabilitiesVector = MatrixUtils.createRealVector(initialProbabilities);
        RealMatrix identityMatrix = MatrixUtils.createRealIdentityMatrix(matrixColumnCount);
        RealMatrix transitionMatrix = identityMatrix.add(generatorMatrix.scalarMultiply(1 / lambda));

        RealVector tempIterationVector;
        RealMatrix tempIterationTransitionMatrix;

        result.setRowVector(0, initialProbabilitiesVector);
        for (int i = 0; i < iterations; i++) {
            tempIterationTransitionMatrix = transitionMatrix.power(i);
            tempIterationVector = tempIterationTransitionMatrix.preMultiply(initialProbabilitiesVector);
            multiplicationMatrix.setRowVector(i, tempIterationVector);
        }

        for (double t = samplingInterval; t <= missionTime; t += samplingInterval) {
            tempIterationVector = result.getRowVector(iterationPosition);

            for (int n = 0; n < iterations; n++) {
                tempMultiplier = Math.pow((lambda * t), n) / factorial(n) * Math.exp(-lambda * t);
                tempIterationVector = tempIterationVector
                        .add(multiplicationMatrix
                                .getRowVector(n)
                                .mapMultiply(tempMultiplier));
            }

            result.setRowVector(iterationPosition, tempIterationVector);
            iterationPosition++;
        }
        System.out.println(result.toString());

        return result;
    }

    /**
     * Find lambda by searching the maximum on the diagonal of the generator matrix.
     * @param generatorMatrix The original generator matrix.
     * @return The maximum on the diagonal of the generator matrix.
     */
    private double getLambda(RealMatrix generatorMatrix) {
        double lambda = 0;
        double tempMax;
        for (int i = 0; i < generatorMatrix.getColumnDimension(); i++) {
            tempMax = Math.abs(generatorMatrix.getEntry(i, i));
            if (tempMax > lambda) {
                lambda = tempMax;
            }
        }
        return lambda;
    }

    public void foo(BDD bdd, RealMatrix realMatrix) {
        meh = realMatrix;
        HashSet<BDD> mc1 = new HashSet<>();
        HashSet<BDD> mc2 = new HashSet<>();
        mc1.add(bdd.getFactory().ithVar(0));
        mc1.add(bdd.getFactory().ithVar(1));
        mc1.add(bdd.getFactory().ithVar(2));

        mc2.add(bdd.getFactory().ithVar(3));
        mc2.add(bdd.getFactory().ithVar(4));
        mc2.add(bdd.getFactory().ithVar(5));

        mc.add(mc1);
        mc.add(mc2);

        System.out.println(prob(bdd) + " prob");
    }

    public double prob(BDD bdd) {
        if (bdd.isOne()) {
            return 1;
        } else if (bdd.isZero()) {
            return 0;
        } else if (visited.containsKey(bdd.hashCode())) {
            System.out.println("visiiited");
            return visited.get(bdd.hashCode());
        } else {
            double probability;
            double probabilityLow = prob(bdd.low());
            boolean found = false;
            for (HashSet<BDD> hashSet : mc) {
                if (hashSet.contains(bdd) && hashSet.contains(bdd.low())) {
                    found = true;
                }
            }
            if (found) {
                probability = probabilityLow + meh.getEntry(1, bdd.var()) * (prob(bdd.high()) - probabilityLow);
            } else {
                probability = probabilityLow + meh.getEntry(1, bdd.var()) * (prob(bdd.high()) - prob(bdd.low().low()));
            }

            visited.put(bdd.hashCode(), probability);

            return probability;
        }
    }

    public boolean aDependentB(BDD a, BDD b) {

        return true;
    }

    public RealMatrix calculateTopEvent(BDD bdd, RealMatrix failureMatrix, HashMap<Integer, Integer> markovChains) {
        List list = (List) bdd.allsat();
        RealMatrix failureRateWithTopEventFailure = MatrixUtils.createRealMatrix(failureMatrix.getRowDimension(), failureMatrix.getColumnDimension() + 1);
        failureRateWithTopEventFailure.setSubMatrix(failureMatrix.getData(), 0, 0);
        double tempProb;
        double calculatedProb;
        for (Object satObject : list) {
            byte[] sat = (byte[]) satObject;
            //scan for independence
            scanForIndependenceAndCalculateProbabilities(failureMatrix, markovChains, failureRateWithTopEventFailure, sat);
        }

        return failureRateWithTopEventFailure;
    }

    private void scanForIndependenceAndCalculateProbabilities(RealMatrix failureMatrix, HashMap<Integer,
            Integer> markovChains, RealMatrix failureRateWithTopEventFailure, byte[] sat) {
        double tempProb;
        double calculatedProb;
        int m;
        int u;

        //are the states independent?
        for (int j = 0; j < sat.length; j++) {
            m = sat[j];
            for (int n = j; n < sat.length; n++) {
                u = sat[n];
                if (j != n && m == 1 && u == 1) {
                    if (markovChains.get(j) == markovChains.get(n)) {
                        return;
                    }
                }
            }
        }

        //they really are independent
        for (int j = 0; j < failureMatrix.getRowDimension(); j++) {
            tempProb = 1;
            calculatedProb = failureRateWithTopEventFailure.getEntry(j, failureMatrix.getColumnDimension());
            for (int i = 0; i < sat.length; i++) {
                if (sat[i] == 1) {
                    tempProb *= failureMatrix.getEntry(j, i);
                } else if (sat[i] == 0) {
                    tempProb *= 1 - failureMatrix.getEntry(j, i);
                }
            }
            failureRateWithTopEventFailure.setEntry(j, failureMatrix.getColumnDimension(), tempProb + calculatedProb);
        }
    }
}
