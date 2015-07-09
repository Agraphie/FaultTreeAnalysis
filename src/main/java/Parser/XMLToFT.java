package Parser;

import Model.FaultTree;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.HashSet;

/**
 * Created by Agraphie on 30.05.2015.
 */
public class XMLToFT {
    FaultTree faultTree = null;
    int varCount = 0;
    HashSet<String> encounteredLeafs;

    public FaultTree parse(File file) {
        JAXBContext jaxbContext;
        encounteredLeafs = new HashSet<>();

        try {
            jaxbContext = JAXBContext.newInstance(FaultTree.class);

            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(file));
            xsr = new StreamReaderDelegate(xsr) {

                @Override
                public String getAttributeValue(int index) {

                    String value = super.getAttributeValue(index);
                    if (value.equals("event")) {
                        String previousValue = super.getAttributeValue(index - 1);
                        saveEventId(previousValue);
                    }

                    return value;
                }
            };
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            faultTree = (FaultTree) jaxbUnmarshaller.unmarshal(xsr);
            faultTree.setVarNum(encounteredLeafs.size());

        } catch (JAXBException | XMLStreamException e) {
            e.printStackTrace();
        }

        return faultTree;
    }

    private void saveEventId(String eventId) {
        encounteredLeafs.add(eventId);
    }

}
