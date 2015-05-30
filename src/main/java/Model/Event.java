package Model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Enumeration;

/**
 * Created by Kazako on 30.05.2015.
 */
@XmlRootElement
public class Event {
    String name;
    String id;
    Type type;
    double probability;
    Event eventLeft;
    Event eventRight;

    public String getName() {
        return name;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    @XmlAttribute
    public void setType(Type type) {
        this.type = type;
    }

    public double getProbability() {
        return probability;
    }

    @XmlAttribute
    public void setProbability(double probability) {
        this.probability = probability;
    }

    public Event getEventLeft() {
        return eventLeft;
    }

    @XmlElement
    public void setEventLeft(Event eventLeft) {
        this.eventLeft = eventLeft;
    }

    public Event getEventRight() {
        return eventRight;
    }

    @XmlElement
    public void setEventRight(Event eventRight) {
        this.eventRight = eventRight;
    }

    enum Type{
        AND, OR
    }
}
