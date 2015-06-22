import Model.BDDWithProbabilities;

import java.util.List;

/**
 * Created by Kazako on 31.05.2015.
 */
public class CalculateProbability {

    public double calculateSystemFailure(BDDWithProbabilities bddWithProbabilities) {

        List list = (List) bddWithProbabilities.getBdd().allsat();
        double systemFailureProb = 0;
        double tempProb;
        byte[] sat;
        for (Object o : list) {
            tempProb = 1.0;
            sat = (byte[]) o;
            for (int i = 0; i < sat.length; i++) {
                if (sat[i] == 1) {
                    tempProb *= bddWithProbabilities.getProbabilitiesTrue()[i];
                } else if (sat[i] == 0) {
                    tempProb *= bddWithProbabilities.getProbabilitiesFalse()[i];
                }
            }
            systemFailureProb += tempProb;
        }
        return systemFailureProb;
    }
}
