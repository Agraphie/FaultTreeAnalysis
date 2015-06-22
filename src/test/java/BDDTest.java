import Model.BDDWithProbabilities;
import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Kazako on 31.05.2015.
 */
public class BDDTest {

    BDDFactory bddFactory = BDDFactory.init(3, 3);
    CalculateProbability calculateProbability;
    /**
     * Simple example for calculating the probability of the BDD on
     * https://www3.elearning.rwth-aachen.de/ss15/15ss-04237/Lists/StructuredMaterials/20150506_FSSD_08_S15_Fault_Tree_Analysis.pdf
     * slide 12
     */
    @Test
    public void quickTest() {
        bddFactory.setVarNum(3);
        BDD a = bddFactory.ithVar(0);
        BDD b = bddFactory.ithVar(1);
        BDD c = bddFactory.ithVar(2);
        //   bddFactory.setVarOrder(new int[]{0, 1, 2});
        double[] probabilitiesTrue = {0.2, 0.2, 0.1};
        double[] probabilitiesFalse = {0.8, 0.8, 0.9};

        calculateProbability = new CalculateProbability();
        BDD and = a.and(b);
        BDD or = c.or(and);

        BDDWithProbabilities bddWithProbabilities = new BDDWithProbabilities(or, probabilitiesTrue, probabilitiesFalse);

        assertEquals(0.136, calculateProbability.calculateSystemFailure(bddWithProbabilities), 0.00001);
    }
}
