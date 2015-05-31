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

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
