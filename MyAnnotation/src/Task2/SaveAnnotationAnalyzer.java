package Task2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SaveAnnotationAnalyzer {
    public static void main(String[] args) throws Exception{
        execute(TextContainer.class, "text");
    }

    public static void execute(Class<?> cls, String fieldName) throws Exception{
        Method[] methods = cls.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Saver.class) && cls.isAnnotationPresent(SaveTo.class)) {

                for (Field field : cls.getDeclaredFields()) {
                    if (field.getName().equals(fieldName)){
                        field.setAccessible(true);
                        Object obj = cls.getConstructor().newInstance();
                        method.invoke(obj, cls.getAnnotation(SaveTo.class).path(), field.get(obj).toString());
                    }
                }
            }
        }
    }
}
