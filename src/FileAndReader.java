import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class FileAndReader {
    File file;
    BufferedReader reader;

    FileAndReader(String fileName) throws FileNotFoundException {
        file = new File(fileName);
        reader = new BufferedReader(new FileReader(file));
    }

    public BufferedReader getReader(){
        return reader;
    }
}
