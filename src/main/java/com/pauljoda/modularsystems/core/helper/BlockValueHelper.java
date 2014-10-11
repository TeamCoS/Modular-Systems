package com.pauljoda.modularsystems.core.helper;

import com.pauljoda.modularsystems.core.structures.BlockValue;
import com.pauljoda.modularsystems.core.structures.MaterialValue;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BlockValueHelper
{
    public static List<BlockValue>blockValues = new ArrayList<BlockValue>();
    public static List<MaterialValue>materialValues = new ArrayList<MaterialValue>();

    public static void init() throws ParserConfigurationException, TransformerException, IOException, SAXException
    {
        File valuesFile = new File("config/ModularSystems/blockValues.xml");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        if(!valuesFile.exists())
        {
            generateDefaultValues();
            return;
        }

        Document doc = dBuilder.parse(valuesFile);
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("block");

        for (int temp = 0; temp < nList.getLength(); temp++)
        {

            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {

                Element eElement = (Element) nNode;
                BlockValue block = new BlockValue(eElement.getAttribute("unlocalizedName"), eElement.getElementsByTagName("speedValue").item(0).getTextContent(), eElement.getElementsByTagName("efficiencyValue").item(0).getTextContent());
                blockValues.add(block);
            }
        }

        nList = doc.getElementsByTagName("material");

        for (int temp = 0; temp < nList.getLength(); temp++)
        {

            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {

                Element eElement = (Element) nNode;
                MaterialValue material = new MaterialValue(MaterialValue.getMaterialFromString(eElement.getAttribute("name")), Double.parseDouble(eElement.getElementsByTagName("speedValue").item(0).getTextContent()), Double.parseDouble(eElement.getElementsByTagName("efficiencyValue").item(0).getTextContent()));
                materialValues.add(material);
            }
        }
    }

    private static void generateDefaultValues() throws TransformerException, ParserConfigurationException, IOException, SAXException
    {
        URL original = BlockValueHelper.class.getClassLoader().getResource("blockValues.xml");
        File destination = new File("config/ModularSystems/blockValues.xml");
        boolean flag = true;
        try
        {
            FileUtils.copyURLToFile(original, destination);
        } catch (IOException e)
        {
            flag = false;
            LogHelper.error(e.getMessage());
        }
        if(flag)
        init();
    }
}
