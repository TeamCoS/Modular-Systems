package com.teambr.modularsystems.core.functions;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis "pauljoda"
 * @since 3/24/2016
 */
public class CompressionFunctions {

    /*
     * Code thanks to Pahimar and EE3
     */

    public static byte[] compressStringToByteArray(String uncompressedString)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream;
        try
        {
            gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(uncompressedString.getBytes("UTF-8"));
            gzipOutputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }

    public static String decompressStringFromByteArray(byte[] compressedString)
    {
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(compressedString));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gzipInputStream, "UTF-8"));

            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
