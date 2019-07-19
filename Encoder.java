package Handler;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Encoder {

    /**
     * From Json to Java
     *
     * @param input is a JSON string to be converted to JAVA
     * @param cls   is the JAVA class to put the string in
     * @return is the JAVA object
     */
    public Object decode(InputStream input, Class<?> cls) {
        InputStreamReader reader = new InputStreamReader(input);

        Gson gson = new Gson();
        return gson.fromJson(reader, cls);
    }


    /**
     * From Java to JSON
     *
     * @param output is a Java object to be converted to JSON
     * @return is a JSON string
     */
    public String encode(Object output) {

        Gson gson = new Gson();
        return gson.toJson(output);
    }
}
