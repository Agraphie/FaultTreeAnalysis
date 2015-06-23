package Model;

import javax.xml.bind.annotation.*;

/**
 * Created by Kazako on 30.05.2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FaultTree {


    @XmlElement(required = true)
    boolean continuousMC;
    @XmlElement
    Node node;
    @XmlAttribute
    String name;
    int varNum;
    @XmlElement(name = "initialProbability")
    double[] initialProbabilities;
    @XmlElement
    double missionTime;
    @XmlElement
    double samplingInterval;


    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public int getVarNum() {
        return varNum;
    }

    public void setVarNum(int varNum) {
        this.varNum = varNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isContinuousMC() {
        return continuousMC;
    }

    public void setContinuousMC(boolean continuousMC) {
        this.continuousMC = continuousMC;
    }

    public double[] getInitialProbabilities() {
        return initialProbabilities;
    }

    public void setInitialProbabilities(double[] initialProbabilities) {
        this.initialProbabilities = initialProbabilities;
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
