package Model;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by Kazako on 30.05.2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Node {

    @XmlAttribute
    Type type;
    @XmlAttribute
    Operator operator;
    @XmlAttribute
    String name;
    @XmlAttribute
    String id;
    @XmlElement
    double probability;
    @XmlElement(name = "node")
    List<Node> nodes;

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public enum Type {
        gate, event
    }

    public enum Operator {
        OR, AND
    }
}
