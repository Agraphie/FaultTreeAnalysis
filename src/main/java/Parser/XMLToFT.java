package Parser;

import Model.FaultTree;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by Kazako on 30.05.2015.
 */
@Component
public class XMLToFT {

    public FaultTree parse(){
        JAXBContext jaxbContext = null;
        FaultTree faultTree = null;
        try {
            jaxbContext = JAXBContext.newInstance(FaultTree.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            faultTree = (FaultTree) jaxbUnmarshaller.unmarshal(new File("C:\\Users\\Kazako\\Documents\\RWTH\\SS2015\\Functional\\ft.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return faultTree;
    }

}
