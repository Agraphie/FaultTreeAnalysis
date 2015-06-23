package Calculators;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import static org.apache.commons.math3.util.CombinatoricsUtils.factorial;

/**
 * Created by Kazakor on 22.06.2015.
 */
public class CalculateMCProbabilities {

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
        System.out.println(transitionMatrix.toString());

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
                tempIterationVector = tempIterationVector.add(multiplicationMatrix.getRowVector(n).mapMultiply(tempMultiplier));
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

}
