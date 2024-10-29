package me.h12z.h12z_json;

import java.io.*;
import java.nio.file.*;

public class Json {

    private static String removeWhitespace(String json) {
        StringBuilder result = new StringBuilder();
        boolean inString = false;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '"') {
                inString = !inString;
            }
            if (!inString && Character.isWhitespace(c)) {
                continue;
            }
            result.append(c);
        }
        return result.toString();
    }

    private static JSONObject parseJSONObject(String json) {
        json = removeWhitespace(json);
        json = json.trim();
        if (!json.startsWith("{") || !json.endsWith("}")) {
            throw new IllegalArgumentException("Ungültiges JSON-Objekt");
        }
        json = json.substring(1, json.length() - 1).trim();

        JSONObject jsonObject = new JSONObject();
        int length = json.length();
        int i = 0;

        while (i < length) {
            if (json.charAt(i) == '"') {
                int keyEnd = json.indexOf('"', i + 1);
                String key = json.substring(i + 1, keyEnd);
                i = keyEnd + 1;

                while (json.charAt(i) != ':') i++;
                i++;

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

                jsonObject.add(key, value);
            }

            while (i < length && json.charAt(i) != ',') i++;
            i++;
        }

        return jsonObject;
    }

    private static JSONArray parseJSONArray(String json) {
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

    public static JSONObject parse(String json) {

        return parseJSONObject(json);

    }

    public static JSONObject parseFile(File jsonFile) throws IOException {

        if(!jsonFile.getParentFile().exists()) jsonFile.getParentFile().mkdirs();

        if(!jsonFile.exists()) jsonFile.createNewFile();

        String json = Files.readString(Paths.get(jsonFile.getAbsolutePath()));

        return parse(json);

    }

    public static JSONObject parseFile(String path) throws IOException {

        File jsonFile = new File(path);

        return parseFile(jsonFile);

    }

    public static void save(JSONObject json, File file) throws IOException {

        json.save(file);

    }

    public static void save(JSONObject json, String path) throws IOException {

        json.save(path);

    }
    
}