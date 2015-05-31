package Parser;

import Model.FaultTree;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import javax.xml.transform.stream.StreamSource;

/**
 * Created by Kazako on 30.05.2015.
 */
@Component
public class XMLToFT {
    FaultTree faultTree = null;
    int varCount = 0;

    public FaultTree parse(){
        JAXBContext jaxbContext;

        try {
            jaxbContext = JAXBContext.newInstance(FaultTree.class);

            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource("C:\\Users\\Kazako\\Documents\\RWTH\\SS2015\\Functional\\ft.xml"));
            xsr = new StreamReaderDelegate(xsr) {

                @Override
                public String getAttributeValue(int index) {
                    String value = super.getAttributeValue(index);
                    if (value.equals("event")) {
                        increaseVarNum();

                    }

                    return value;
                }
            };
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            faultTree = (FaultTree) jaxbUnmarshaller.unmarshal(xsr);
            faultTree.setVarNum(varCount);

        } catch (JAXBException | XMLStreamException e) {
            e.printStackTrace();
        }

        return faultTree;
    }

    private void increaseVarNum() {
        varCount++;
    }

}
