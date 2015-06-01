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

/**
 * Created by Kazako on 30.05.2015.
 */
public class XMLToFT {
    FaultTree faultTree = null;
    int varCount = 0;

    public FaultTree parse(File file) {
        JAXBContext jaxbContext;

        try {
            jaxbContext = JAXBContext.newInstance(FaultTree.class);

            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(file));
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
