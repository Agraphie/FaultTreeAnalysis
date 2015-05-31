package Model;

import javax.xml.bind.annotation.*;

/**
 * Created by Kazako on 30.05.2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FaultTree {
    @XmlElement
    Node node;
    @XmlAttribute
    String name;
    int varNum;

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
}
