package Task1;

import java.lang.reflect.Method;

public class TestAnnotationAnalyzer {

    public static void main(String[] args) throws Exception {
        execute(TestClass.class);
    }

    public static void execute(Class<?> cls) throws Exception {
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)){
                method.invoke(cls.getConstructor().newInstance(), method.getAnnotation(Test.class).a(),
                        method.getAnnotation(Test.class).b());

            }
        }
    }
}
