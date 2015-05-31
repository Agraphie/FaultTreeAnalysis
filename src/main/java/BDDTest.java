import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import java.util.List;

/**
 * Created by Kazako on 31.05.2015.
 */
public class BDDTest {

    BDDFactory bddFactory = BDDFactory.init(3, 3);

    /**
     * Simple example for calculating the probability of the BDD on
     * https://www3.elearning.rwth-aachen.de/ss15/15ss-04237/Lists/StructuredMaterials/20150506_FSSD_08_S15_Fault_Tree_Analysis.pdf
     * slide 12
     */
    public void quickTest() {
        bddFactory.setVarNum(3);
        BDD a = bddFactory.ithVar(0);
        BDD b = bddFactory.ithVar(1);
        BDD c = bddFactory.ithVar(2);
        //   bddFactory.setVarOrder(new int[]{0, 1, 2});
        double[] probabilitiesTrue = {0.2, 0.2, 0.1};
        double[] probabilitiesFalse = {0.8, 0.8, 0.9};

        BDD and = a.and(b);
        BDD or = c.or(and);

        List list = (List) or.allsat();
        double systemFailureProb = 0;
        double tempProb;
        byte[] sat;
        for (Object o : list) {
            tempProb = 1.0;
            sat = (byte[]) o;
            for (int i = 0; i < sat.length; i++) {
                if (sat[i] == 1) {
                    tempProb *= probabilitiesTrue[i];
                } else if (sat[i] == 0) {
                    tempProb *= probabilitiesFalse[i];
                }
            }
            systemFailureProb += tempProb;
        }
        System.out.println(systemFailureProb);
    }
}
