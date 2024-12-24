package me.h12z.h12z_json;

import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONObject {
    private final Map<String, Object> map;

    public JSONObject() {
        this.map = new HashMap<>();
    }

    /**
     * Adds a value under a certain name (If the value already exists it is overwritten)
     * @param key
     * The key of the value to be added
     * @param value
     * The value to be added
     */
    public void add(String key, Object value) {
        map.put(key, value);
    }

    private Object getAsObject(String key) {
        return map.get(key);
    }

    /**
     * Gets the json object of a certain key
     * @param key
     * The wanted key
     * @return
     * The wanted json object (returns null if it isn't a json object or if it doesn't exist)
     */
    public JSONObject get(String key) {
        if (!map.containsKey(key))
            return null;
        if (!(getAsObject(key) instanceof JSONObject))
            return null;
        return (JSONObject) getAsObject(key);
    }

    /**
     * Gets the json array of a certain key
     * @param key
     * The wanted key
     * @return
     * The wanted json array (returns null if it isn't a json array or if it doesn't exist)
     */
    public JSONArray getAsJsonArray(String key) {
        if (!map.containsKey(key))
            return null;
        if (!(getAsObject(key) instanceof JSONArray))
            return null;
        return (JSONArray) getAsObject(key);
    }

    /**
     * Gets the string value of a certain key
     * @param key
     * The wanted key
     * @return
     * The wanted string value (returns null if it isn't a string or if it doesn't exist)
     */
    public String getAsString(String key) {
        if (!map.containsKey(key))
            return null;
        if (!(getAsObject(key) instanceof String))
            return null;
        return (String) getAsObject(key);
    }

    /**
     * Gets the double value of a certain key
     * @param key
     * The wanted key
     * @return
     * The wanted double value (returns null if it isn't a double or if it doesn't exist)
     */
    public Double getAsDouble(String key) {
        if (!map.containsKey(key))
            return null;
        if (!(getAsObject(key) instanceof Double))
            return 0.0;
        return (Double) getAsObject(key);
    }

    /**
     * Gets the integer value of a certain key
     * @param key
     * The wanted key
     * @return
     * The wanted integer value (returns null if it isn't an integer or if it doesn't exist)
     */
    public Integer getAsInteger(String key) {
        if (!map.containsKey(key))
            return null;
        if (!(getAsObject(key) instanceof Integer))
            return 0;
        return (Integer) getAsObject(key);
    }

    @Override
    public String toString() {
        StringBuilder json = new StringBuilder();
        json.append("{");

        int size = this.map.size();
        int i = 0;

        for (Map.Entry<String, Object> entry : this.map.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\": ");

            if (entry.getValue() instanceof JSONObject) {
                json.append(entry.getValue().toString());
            } else if (entry.getValue() instanceof JSONArray) {
                json.append(((JSONArray) entry.getValue()).toString());
            } else if (entry.getValue() instanceof String) {
                json.append("\"").append(entry.getValue()).append("\"");
            } else {
                json.append(entry.getValue());
            }

            if (i < size - 1) {
                json.append(", ");
            }

            i++;
        }

        json.append("}");
        return json.toString();
    }

    /**
     * Saves the json object to a file
     * @param file
     * The File to be saved to
     */
    public void save(File file) throws IOException {

        if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if(!file.exists()) file.createNewFile();

        FileWriter fw = new FileWriter(file);

        fw.write(this.toString());

    }

    /**
     * Saves the json object to a given file path
     * @param path
     * The path of the File to be saved to
     */
    public void save(String path) throws IOException {

        save(new File(path));

    }

}
