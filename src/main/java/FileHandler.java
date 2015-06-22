import java.io.File;

/**
 * Created by Kazako on 01.06.2015.
 */
public class FileHandler {

    public File getFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("ft.xml").getFile());

        return file;

    }
}
