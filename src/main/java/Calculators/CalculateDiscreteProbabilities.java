package Calculators;

import Model.BDDWithProbabilities;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;

/**
 * Created by Kazakor on 23.06.2015.
 */
public class CalculateDiscreteProbabilities {

    public double calculateSystemFailure(BDDWithProbabilities bddWithProbabilities) {

        List list = (List) bddWithProbabilities.getBdd().allsat();
        double systemFailureProb = 0;
        double tempProb;
        byte[] sat;
        RealVector initialProbabilities = bddWithProbabilities.getInitialProbabilities();
        for (Object o : list) {
            tempProb = 1.0;
            sat = (byte[]) o;
            for (int i = 0; i < sat.length; i++) {
                if (sat[i] == 1) {
                    tempProb *= initialProbabilities.getEntry(i);
                } else if (sat[i] == 0) {
                    tempProb *= (1 - initialProbabilities.getEntry(i));
                }
            }
            systemFailureProb += tempProb;
        }
        return systemFailureProb;
    }
}
