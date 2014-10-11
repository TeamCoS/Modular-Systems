package com.pauljoda.modularsystems.core.helper;

import com.pauljoda.modularsystems.core.structures.BlockValues;
import com.pauljoda.modularsystems.core.structures.MaterialValues;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BlockValueHelper
{
    public List<BlockValues>blockValues = new ArrayList<BlockValues>();
    public List<MaterialValues>materialValues = new ArrayList<MaterialValues>();

    public static void init() throws ParserConfigurationException, TransformerException
    {
        File valuesFile = new File("config/ModularSystems/blockValues.xml");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        if(!valuesFile.exists())
        {
            generateDefaultValues();
            return;
        }


    }

    public void loadValues()
    {

    }

    private static void generateDefaultValues() throws TransformerException, ParserConfigurationException
    {


        init();
    }
}
