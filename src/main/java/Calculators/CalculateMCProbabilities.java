package Calculators;

import net.sf.javabdd.BDD;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.*;

import static org.apache.commons.math3.util.CombinatoricsUtils.factorial;

/**
 * Created by Agraphie on 22.06.2015.
 */
public class CalculateMCProbabilities {

    RealMatrix meh;
    List<HashSet<BDD>> mc = new LinkedList<>();
    HashMap<Integer, Double> visited = new HashMap<>();


    /**
     * Calculates the probability matrix for continous markov chains by using uniformization,
     *
     * @param initialProbabilities The vector of the initial probabilities.
     * @param generatorMatrix      The generator matrix of the states.
     * @param samplingInterval     The sampling interval, or in which intervals t changes.
     * @param missionTime          The complete mission time till what t the probabilities should be calculated.
     * @return The probability matrix as RealMatrix, where the row are the probabilities at time t and the columns are the states.
     * @see <a href="https://en.wikipedia.org/wiki/Uniformization_%28probability_theory%29">Uniformization (probability theory</a>
     * First, lambda gets calculated. The column count of the probability matrix is missionTime / samplingInterval + 1. +1 because of the first row,
     * which is the initial probability vector.
     * Then the transition matrix P gets calculated by adding the Identity Matrix to the generator matrix which was multiplied by 1 / lambda.
     * Next, the multiplication matrix Pi(0)*P gets calculated, where a row is the multiplication vector for the iteration n.
     * At last every row in the result is added to the result of the previous iteration to approximate the probability.
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

        RealMatrix result = MatrixUtils.createRealMatrix(matrixEntries + 1, matrixColumnCount);
        RealVector initialProbabilitiesVector = MatrixUtils.createRealVector(initialProbabilities);
        RealMatrix identityMatrix = MatrixUtils.createRealIdentityMatrix(matrixColumnCount);
        RealMatrix transitionMatrix = identityMatrix.add(generatorMatrix.scalarMultiply(1 / lambda));

        RealVector tempIterationVector;

        result.setRowVector(0, initialProbabilitiesVector);
        RealMatrix multiplicationMatrix = calculateMultiplicationMatrix(iterations, initialProbabilitiesVector, transitionMatrix);
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

        return result;
    }

    private RealMatrix calculateMultiplicationMatrix(int iterations, RealVector initialProbabilitiesVector, RealMatrix transitionMatrix) {
        RealMatrix multiplicationMatrix = MatrixUtils.createRealMatrix(iterations, transitionMatrix.getColumnDimension());
        for (int i = 0; i < iterations; i++) {
            RealMatrix tempIterationTransitionMatrix = transitionMatrix.power(i);
            RealVector tempIterationVector = tempIterationTransitionMatrix.preMultiply(initialProbabilitiesVector);
            multiplicationMatrix.setRowVector(i, tempIterationVector);
        }

        return multiplicationMatrix;
    }

    /**
     * Find lambda by searching the maximum on the diagonal of the generator matrix.
     *
     * @param generatorMatrix The original generator matrix.
     * @return The maximum on the diagonal of the generator matrix.
     */
    private double getLambda(RealMatrix generatorMatrix) {
        double lambda = 0;
        for (int i = 0; i < generatorMatrix.getColumnDimension(); i++) {
            double tempMax = Math.abs(generatorMatrix.getEntry(i, i));
            if (tempMax > lambda) {
                lambda = tempMax;
            }
        }
        return lambda;
    }

    public RealMatrix calculateTopEvent(BDD bdd, RealMatrix failureMatrix, HashMap<Integer, Integer> markovChains) {
        List list = (List) bdd.allsat();
        RealMatrix failureRateWithTopEventFailure = MatrixUtils.createRealMatrix(failureMatrix.getRowDimension(), failureMatrix.getColumnDimension() + 1);
        failureRateWithTopEventFailure.setSubMatrix(failureMatrix.getData(), 0, 0);
        for (Object satObject : list) {
            byte[] sat = (byte[]) satObject;
            //scan for independence
            scanForIndependenceAndCalculateProbabilities(failureMatrix, markovChains, failureRateWithTopEventFailure, sat);
        }

        return failureRateWithTopEventFailure;
    }

    /**
     * Method scans first if the demanded states are independent. It iterates over every solution obtained by calling allSat(). Every solution will provide an
     * array. Over every such array the method iterates and compares every entry (variable) to every other variable. If the compared variables are set to 1 and
     * are in the same markov chains, the probability for that solution is set to 0.
     * If a variable is set to 1, it gets multiplied with the temp probability. If it is set to 0, 1 - the probability gets multiplied.
     *
     * @param failureMatrix                  The matrix containing all probabilities for the failures.
     * @param markovChains                   The markov chains, stating which variables are in one chain.
     * @param failureRateWithTopEventFailure The resulting failure matrix with the top event probability.
     * @param sat                            The current sat solution.
     */
    private void scanForIndependenceAndCalculateProbabilities(RealMatrix failureMatrix, HashMap<Integer,
            Integer> markovChains, RealMatrix failureRateWithTopEventFailure, byte[] sat) {
        if (!scanForIndependence(markovChains, sat)) {
            return;
        }
        System.out.println(Arrays.toString(sat));
        //they really are independent
        for (int j = 0; j < failureMatrix.getRowDimension(); j++) {
            double tempProb = 1;
            double calculatedProb = failureRateWithTopEventFailure.getEntry(j, failureMatrix.getColumnDimension());
            for (int i = 0; i < sat.length; i++) {
                if (sat[i] == 1) {
                    tempProb *= failureMatrix.getEntry(j, i);
                } else if (sat[i] == 0) {
                    //check if there is another state in the same markov chain
                    if (Collections.frequency(markovChains.values(), markovChains.get(i)) == 1) {
                        tempProb *= 1 - failureMatrix.getEntry(j, i);
                    }
                }
            }
            failureRateWithTopEventFailure.setEntry(j, failureMatrix.getColumnDimension(), tempProb + calculatedProb);
        }
    }

    private boolean scanForIndependence(HashMap<Integer, Integer> markovChains, byte[] sat) {
        //are the states independent?
        for (int j = 0; j < sat.length; j++) {
            int m = sat[j];
            int zeroCounter = 0;
            for (int n = j + 1; n < sat.length; n++) {
                int u = sat[n];
                if (Objects.equals(markovChains.get(j), markovChains.get(n))) {
                    if (m == 1 && u == 1) {
                        return false;
                    } else if (m == 0 && u == 0) {
                        zeroCounter++;
                    }
                }
            }

            if (zeroCounter == Collections.frequency(markovChains.values(), markovChains.get(j)) - 1) return false;
        }
        return true;
    }
}
