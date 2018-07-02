package Task2;

import java.io.FileWriter;
import java.io.IOException;

@SaveTo(path="data/save.txt")
public class TextContainer {
    private String text = "string";

    @Saver
    public void save(String path, String toSave) throws IOException {
        FileWriter writer = new FileWriter(path);
        writer.write(toSave);
        writer.close();
    }
}
