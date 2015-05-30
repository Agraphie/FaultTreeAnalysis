package Model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Kazako on 30.05.2015.
 */
@XmlRootElement
public class FaultTree {
    List<Event> events = null;
    String name;

    public List<Event> getEvents() {
        return events;
    }

    @XmlElement(name="event")
    public void setEvents(List<Event> events) {
        this.events = events;
    }


    public String getName() {
        return name;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }
}
