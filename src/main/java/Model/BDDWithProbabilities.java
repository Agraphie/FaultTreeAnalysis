package Model;

import net.sf.javabdd.BDD;

/**
 * Created by Kazako on 31.05.2015.
 */
public class BDDWithProbabilities {

    BDD bdd;
    double[] probabilitiesTrue;
    double[] probabilitiesFalse;

    public BDDWithProbabilities(BDD bdd, double[] probabilitiesTrue, double[] probabilitiesFalse) {
        this.bdd = bdd;
        this.probabilitiesTrue = probabilitiesTrue;
        this.probabilitiesFalse = probabilitiesFalse;
    }

    public BDD getBdd() {
        return bdd;
    }

    public double[] getProbabilitiesTrue() {
        return probabilitiesTrue;
    }

    public double[] getProbabilitiesFalse() {
        return probabilitiesFalse;
    }
}
