package Loaders;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigLoader extends Properties {
    private static final String defaultAddress = "resources/config.properties";
    private static ConfigLoader instance;

    public static ConfigLoader getInstance(){
        if (instance == null)
            instance = new ConfigLoader(defaultAddress);
        return instance;
    }
    private ConfigLoader(String address) {
        super();
        Reader reader;
        try {
            reader = new FileReader(address);
            this.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <E> E getProperty(Class<E> c, String propertyName) {
        return getObject(c, propertyName);
    }

    public <E> List<E> getPropertyList(Class<E> c, String propertyName) {
        List<E> list = new ArrayList<>();
        String[] values = getProperty(propertyName).split(", ");
        for (String value : values) {
            list.add(getObject(c, value));
        }
        return list;
    }
    private <E> E getObject(Class<E> c, String propertyName){
        E e = null;
        try {
            Constructor<E> constructor = c.getConstructor(String.class);
            e = constructor.newInstance(propertyName);
        } catch (ReflectiveOperationException exception) {
            exception.printStackTrace();
        }

        return e;
    }
}
