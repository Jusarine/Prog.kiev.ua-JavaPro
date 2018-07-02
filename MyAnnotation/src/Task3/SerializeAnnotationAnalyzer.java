package Task3;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;

public class SerializeAnnotationAnalyzer {

    public static void writeFields(Object o, String fileName) throws Exception {
        FileWriter writer = new FileWriter(fileName);
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field: fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Save.class)){
                writer.write(field.get(o).toString() + " ");
            }
        }
        writer.close();
    }

    public static <T> T readFields(Class<T> cls, String fileName) throws Exception {
        BufferedReader reader = new BufferedReader( new FileReader (fileName));
        T obj = cls.getConstructor().newInstance();
        String line;
        LinkedList<String> list = new LinkedList<>();
        while((line = reader.readLine()) != null){
            list.addAll(Arrays.asList(line.split(" ")));
        }
        Field[] fields = cls.getDeclaredFields();
        int i = 0;
        for (Field field: fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Save.class)){
                if (field.getType() == int.class) {
                    field.setInt(obj, Integer.parseInt(list.get(i)));
                }
                else if (field.getType() == String.class){
                    field.set(obj, list.get(i));
                }
                i++;
            }
        }
        reader.close();
        return obj;
    }

    public static void main(String[] args) throws Exception {
        Cat cat = new Cat();
        cat.setName("Mars");
        cat.setAge(5);
        cat.setColor("RED");

        writeFields(cat, "data/serialize.txt");
        Cat deserialize = readFields(cat.getClass(), "data/serialize.txt");
        System.out.println(deserialize);
    }
}
