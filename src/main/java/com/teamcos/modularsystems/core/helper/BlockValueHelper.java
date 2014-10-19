package com.teamcos.modularsystems.core.helper;

import com.teamcos.modularsystems.furnace.FurnaceConfigHandler;
import com.teamcos.modularsystems.furnace.config.BlockConfig;
import com.teamcos.modularsystems.furnace.config.Calculation;
import com.teamcos.modularsystems.registries.MaterialRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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

public class BlockValueHelper
{
    private BlockValueHelper() {}

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
                Element element = (Element) nNode;
                String name = element.getAttribute("unlocalizedName");
                String speed = element.getElementsByTagName("speedValue").item(0).getTextContent();
                NodeList speedEqNode = element.getElementsByTagName("speedEq");
                String speedFunction = "linear";
                if (speedEqNode.getLength() > 0) {
                    speedFunction = speedEqNode.item(0).getTextContent();
                }
                String efficiency = element.getElementsByTagName("efficiencyValue").item(0).getTextContent();
                NodeList effEqNode = element.getElementsByTagName("efficiencyEq");
                String effFunction = "linear";
                if (effEqNode.getLength() > 0) {
                    effFunction = effEqNode.item(0).getTextContent();
                }
                FurnaceConfigHandler.publishBlockConfig(
                        name,
                        new BlockConfig(
                                name,
                                getCalculation(effFunction, efficiency),
                                getCalculation(speedFunction, speed)
                        )
                );
            }
        }

        nList = doc.getElementsByTagName("material");

        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) nNode;
                String name = element.getAttribute("name");
                Material material = MaterialRegistry.retrieveMaterial(name);
                if (material != null) {
                    String speed = element.getElementsByTagName("speedValue").item(0).getTextContent();
                    NodeList speedEqNode = element.getElementsByTagName("speedEq");
                    String speedFunction = "linear";
                    if (speedEqNode.getLength() > 0) {
                        speedFunction = speedEqNode.item(0).getTextContent();
                    }
                    String efficiency = element.getElementsByTagName("efficiencyValue").item(0).getTextContent();
                    NodeList effEqNode = element.getElementsByTagName("efficiencyEq");
                    String effFunction = "linear";
                    if (effEqNode.getLength() > 0) {
                        effFunction = effEqNode.item(0).getTextContent();
                    }

                    FurnaceConfigHandler.publishMaterialConfig(
                            material,
                            new BlockConfig(
                                    name,
                                    getCalculation(effFunction, efficiency),
                                    getCalculation(speedFunction, speed)
                            )
                    );
                }
            }
        }
    }

    private static Calculation getCalculation(String name, String value) {
        if (name.equals("linear")) {
            return linearCalculation(value);
        } else if (name.equals("constant")) {
            return constantCalculation(value);
        } else if (name.equals("log")) {
            return logCalculation(value);
        } else if (name.equals("parabolic")) {
            return parabolicCalculation(value);
        } else {
            return linearCalculation(value);
        }
    }

    private static Calculation constantCalculation(String value) {
        try {
            double scalar = Double.parseDouble(value);
            return new Calculation.ConstantCalculation(scalar);
        } catch (Exception e) {
            return defaultCalculation();
        }
    }

    private static Calculation logCalculation(String value) {
        try {
            double scalar = Double.parseDouble(value);
            return new Calculation.LogCalculation(scalar);
        } catch (Exception e) {
            return defaultCalculation();
        }
    }

    private static Calculation parabolicCalculation(String value) {
        try {
            double scalar = Double.parseDouble(value);
            return new Calculation.ParabolicCalculation(scalar);
        } catch (Exception e) {
            return defaultCalculation();
        }
    }

    private static Calculation linearCalculation(String value) {
        try {
            double scalar = Double.parseDouble(value);
            return new Calculation.LinearCalculation(scalar);
        } catch (Exception e) {
            return defaultCalculation();
        }
    }

    private static Calculation defaultCalculation() {
        return new Calculation.LinearCalculation(1);
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
        if(flag) init();
    }

    public static BlockConfig getBlockValueForBlock(Block block) {
        return FurnaceConfigHandler.retrieveBlockConfig(block.getUnlocalizedName());
    }

    public static BlockConfig getMaterialValueForBlock(Block block) {
        return FurnaceConfigHandler.retrieveMaterialConfig(block.getMaterial());
    }
}
