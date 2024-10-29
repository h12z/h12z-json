package me.h12z.h12z_json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Json {

    public static class JSONObject {
        private final Map<String, Object> map;

        public JSONObject() {
            this.map = new HashMap<>();
        }

        public void put(String key, Object value) {
            map.put(key, value);
        }

        public Object get(String key) {
            return map.get(key);
        }

        @Override
        public String toString() {
            return map.toString();
        }
    }

    public static class JSONArray {
        private final List<Object> list;

        public JSONArray() {
            this.list = new ArrayList<>();
        }

        public void add(Object value) {
            list.add(value);
        }

        public Object get(int index) {
            return list.get(index);
        }

        @Override
        public String toString() {
            return list.toString();
        }
    }

    public static JSONObject parseJSONObject(String json) {
        json = json.trim();
        if (!json.startsWith("{") || !json.endsWith("}")) {
            throw new IllegalArgumentException("Ungültiges JSON-Objekt");
        }
        json = json.substring(1, json.length() - 1).trim();

        JSONObject jsonObject = new JSONObject();
        int length = json.length();
        int i = 0;

        while (i < length) {
            // Schlüssel parsen
            if (json.charAt(i) == '"') {
                int keyEnd = json.indexOf('"', i + 1);
                String key = json.substring(i + 1, keyEnd);
                i = keyEnd + 1;

                // Suchen des Doppelpunkts
                while (json.charAt(i) != ':') i++;
                i++;

                // Wert parsen
                while (json.charAt(i) == ' ') i++;
                Object value;
                if (json.charAt(i) == '{') {
                    int objEnd = findClosingBrace(json, i);
                    value = parseJSONObject(json.substring(i, objEnd + 1));
                    i = objEnd + 1;
                } else if (json.charAt(i) == '[') {
                    int arrEnd = findClosingBracket(json, i);
                    value = parseJSONArray(json.substring(i, arrEnd + 1));
                    i = arrEnd + 1;
                } else if (json.charAt(i) == '"') {
                    int valEnd = json.indexOf('"', i + 1);
                    value = json.substring(i + 1, valEnd);
                    i = valEnd + 1;
                } else {
                    int valEnd = findValueEnd(json, i);
                    value = parsePrimitive(json.substring(i, valEnd));
                    i = valEnd;
                }

                jsonObject.put(key, value);
            }

            while (i < length && json.charAt(i) != ',') i++;
            i++;
        }

        return jsonObject;
    }

    public static JSONArray parseJSONArray(String json) {
        json = json.trim();
        if (!json.startsWith("[") || !json.endsWith("]")) {
            throw new IllegalArgumentException("Ungültiges JSON-Array");
        }
        json = json.substring(1, json.length() - 1).trim();

        JSONArray jsonArray = new JSONArray();
        int length = json.length();
        int i = 0;

        while (i < length) {
            while (i < length && json.charAt(i) == ' ') i++;
            Object value;
            if (json.charAt(i) == '{') {
                int objEnd = findClosingBrace(json, i);
                value = parseJSONObject(json.substring(i, objEnd + 1));
                i = objEnd + 1;
            } else if (json.charAt(i) == '[') {
                int arrEnd = findClosingBracket(json, i);
                value = parseJSONArray(json.substring(i, arrEnd + 1));
                i = arrEnd + 1;
            } else if (json.charAt(i) == '"') {
                int valEnd = json.indexOf('"', i + 1);
                value = json.substring(i + 1, valEnd);
                i = valEnd + 1;
            } else {
                int valEnd = findValueEnd(json, i);
                value = parsePrimitive(json.substring(i, valEnd));
                i = valEnd;
            }

            jsonArray.add(value);
            while (i < length && json.charAt(i) != ',') i++;
            i++;
        }

        return jsonArray;
    }

    private static int findClosingBrace(String json, int start) {
        int count = 0;
        for (int i = start; i < json.length(); i++) {
            if (json.charAt(i) == '{') count++;
            else if (json.charAt(i) == '}') count--;
            if (count == 0) return i;
        }
        throw new IllegalArgumentException("Ungeschlossene Klammer gefunden");
    }

    private static int findClosingBracket(String json, int start) {
        int count = 0;
        for (int i = start; i < json.length(); i++) {
            if (json.charAt(i) == '[') count++;
            else if (json.charAt(i) == ']') count--;
            if (count == 0) return i;
        }
        throw new IllegalArgumentException("Ungeschlossene eckige Klammer gefunden");
    }

    private static int findValueEnd(String json, int start) {
        int i = start;
        while (i < json.length() && json.charAt(i) != ',' && json.charAt(i) != '}') i++;
        return i;
    }

    private static Object parsePrimitive(String value) {
        if ("true".equals(value)) return true;
        if ("false".equals(value)) return false;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return value;
        }
    }

    public static void main(String[] args) {
        String jsonString = "{\"hi\":{\"hi\":\"hi\",\"bye\":[\"bye\":\"bye\"]}}";
        JSONObject jsonObject = parseJSONObject(jsonString);
        System.out.println(jsonObject);
    }
    
}