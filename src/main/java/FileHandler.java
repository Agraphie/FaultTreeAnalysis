import java.io.File;

/**
 * Created by Agraphie on 01.06.2015.
 */
public class FileHandler {

    public File getFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("meh.xml").getFile());

        return file;
    }
}
