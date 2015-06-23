package Model;

import net.sf.javabdd.BDD;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Kazako on 31.05.2015.
 */
public class BDDWithProbabilities {

    BDD bdd;
    RealMatrix generatorMatrix;
    RealVector initialProbabilities;
    boolean continuousMC;
    double missionTime;
    double samplingInterval;

    public BDDWithProbabilities(BDD bdd, RealMatrix generatorMatrix, RealVector initialProbabilities,
                                boolean continuousMC, double missionTime, double samplingInterval) {
        this.bdd = bdd;
        this.generatorMatrix = generatorMatrix;
        this.initialProbabilities = initialProbabilities;
        this.continuousMC = continuousMC;
        this.missionTime = missionTime;
        this.samplingInterval = samplingInterval;
    }

    public BDD getBdd() {
        return bdd;
    }


    public void setBdd(BDD bdd) {
        this.bdd = bdd;
    }

    public RealMatrix getGeneratorMatrix() {
        return generatorMatrix;
    }

    public void setGeneratorMatrix(RealMatrix generatorMatrix) {
        this.generatorMatrix = generatorMatrix;
    }

    public RealVector getInitialProbabilities() {
        return initialProbabilities;
    }

    public void setInitialProbabilities(RealVector initialProbabilities) {
        this.initialProbabilities = initialProbabilities;
    }

    public boolean isContinuousMC() {
        return continuousMC;
    }

    public void setContinuousMC(boolean continuousMC) {
        this.continuousMC = continuousMC;
    }

    public double getMissionTime() {
        return missionTime;
    }

    public void setMissionTime(double missionTime) {
        this.missionTime = missionTime;
    }

    public double getSamplingInterval() {
        return samplingInterval;
    }

    public void setSamplingInterval(double samplingInterval) {
        this.samplingInterval = samplingInterval;
    }
}
