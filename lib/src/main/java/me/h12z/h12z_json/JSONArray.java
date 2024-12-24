package me.h12z.h12z_json;

import java.util.ArrayList;
import java.util.List;

public class JSONArray {
    private final List<Object> list;

    public JSONArray() {
        this.list = new ArrayList<>();
    }

    /**
     * Adds an item to the json array
     * @param value
     * The item to be added (can be: JSONObject, JSONArray, String, int or double)
     */
    public void add(Object value) {
        list.add(value);
    }

    /**
     * Gets the json object at a certain index
     * @param index
     * The index of the wanted json object
     * @return
     * The wanted json object (return null if it isn't a json object)
     */
    public JSONObject get(int index) {
        if (!(list.get(index) instanceof JSONObject))
            return null;
        return (JSONObject) list.get(index);
    }

    /**
     * Gets the json array at a certain index
     * @param index
     * The index of the wanted json array
     * @return
     * The wanted json array (return null if it isn't a json array)
     */
    public JSONArray getAsJsonArray(int index) {
        if (!(list.get(index) instanceof JSONArray))
            return null;
        return (JSONArray) list.get(index);
    }

    /**
     * Gets the string at a certain index
     * @param index
     * The index of the wanted string
     * @return
     * The wanted string (return null if it isn't a string)
     */
    public String getAsString(int index) {
        if (!(list.get(index) instanceof String))
            return null;
        return (String) list.get(index);
    }

    /**
     * Gets the integer at a certain index
     * @param index
     * The index of the wanted integer
     * @return
     * The wanted integer (return null if it isn't an integer)
     */
    public Integer getAsInteger(int index) {
        if (!(list.get(index) instanceof Integer))
            return null;
        return (Integer) list.get(index);
    }

    /**
     * Gets the double at a certain index
     * @param index
     * The index of the wanted double
     * @return
     * The wanted double (return null if it isn't a double)
     */
    public Double getAsDouble(int index) {
        if (!(list.get(index) instanceof Double))
            return null;
        return (Double) list.get(index);
    }

    @Override
    public String toString() {
        StringBuilder json = new StringBuilder();
        json.append("[");

        int size = this.list.size();
        int i = 0;

        for (Object element : this.list) {
            if (element instanceof JSONObject) {
                json.append(element);
            } else if (element instanceof JSONArray) {
                json.append(element);
            } else if (element instanceof String) {
                json.append("\"").append(element).append("\"");
            } else {
                json.append(element);
            }

            if (i < size - 1) {
                json.append(", ");
            }

            i++;
        }

        json.append("]");
        return json.toString();
    }

}
