package com.pauljoda.modularsystems.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pauljoda.modularsystems.core.helpers.LogHelper;

import java.io.*;

public class JsonUtils {
    public static boolean writeToJson(Object toWrite, String path) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(toWrite);
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(json);
            writer.close();
            return true;
        } catch (IOException e) {
            LogHelper.severe("Failed to write to: " + path);
            return false;
        }
    }

    public static <C> C readFromJson(TypeToken type, String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            Gson gson = new Gson();
            return gson.fromJson(reader, type.getType());
        } catch (FileNotFoundException e) {
            LogHelper.severe("Could not find file: " + path);
            return null;
        }
    }
}