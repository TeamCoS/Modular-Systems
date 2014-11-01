package com.teamcos.modularsystems.core.helper;

import com.teamcos.modularsystems.calculations.*;
import com.teamcos.modularsystems.calculations.Calculation.*;
import com.teamcos.modularsystems.furnace.config.BlockConfig;
import com.teamcos.modularsystems.registries.FurnaceConfigHandler;
import com.teamcos.modularsystems.registries.MaterialRegistry;
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
                Element speed = getElement(element, "speed");
                Element efficiency = getElement(element, "efficiency");
                Element smelting = getElement(element, "smelting");
                FurnaceConfigHandler.publishBlockConfig(
                    name,
                    blockConfig(name, speed, efficiency, smelting)
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
                    Element speed = getElement(element, "speed");
                    Element efficiency = getElement(element, "efficiency");
                    Element smelting = getElement(element, "smelting");

                    FurnaceConfigHandler.publishMaterialConfig(
                        material,
                        blockConfig(name, speed, efficiency, smelting)
                    );
                }
            }
        }
    }

    private static BlockConfig blockConfig(String name, Element speed, Element efficiency, Element smelting) {
        return new BlockConfig(
                name,
                getEfficiencyCalculation(efficiency),
                getSpeedCalculation(speed),
                getSmeltingCalculation(smelting)
        );
    }

    private static Element getElement(Element node, String name) {
        NodeList eles = node.getElementsByTagName(name);
        if (eles.getLength() > 0) {
            return (Element) eles.item(0);
        } else {
            return null;
        }
    }

    private static Calculation getSmeltingCalculation(Element node) {
        return node == null
                ? defaultSmeltingCalculation()
                : getCalculation(node);
    }

    private static Calculation getEfficiencyCalculation(Element node) {
        return node == null
                ? defaultCalculation()
                : getCalculation(node);
    }

    private static Calculation getSpeedCalculation(Element node) {
        return node == null
                ? defaultCalculation()
                : getCalculation(node);
    }

    private static Calculation getCalculation(Element node) {
        String name = getStringValue(node, "equation", "linear");
        if (name.equals("linear")) {
            return linearCalculation(node);
        } else if (name.equals("constant")) {
            return constantCalculation(node);
        } else if (name.equals("log")) {
            return logCalculation(node);
        } else if (name.equals("parabolic")) {
            return parabolicCalculation(node);
        } else {
            return linearCalculation(node);
        }
    }

    private static Calculation constantCalculation(Element node) {
        try {
            return new ConstantCalculation(getValues(node));
        } catch (Exception e) {
            return defaultCalculation();
        }
    }

    private static Calculation logCalculation(Element node) {
        try {
            return new LogCalculation(getValues(node));
        } catch (Exception e) {
            return defaultCalculation();
        }
    }

    private static Calculation parabolicCalculation(Element node) {
        try {
            return new ParabolicCalculation(getValues(node));
        } catch (Exception e) {
            return defaultCalculation();
        }
    }

    private static Calculation linearCalculation(Element node) {
        try {
            return new LinearCalculation(getValues(node));
        } catch (Exception e) {
            return defaultCalculation();
        }
    }

    private static Calculation defaultCalculation() {
        return new LinearCalculation(new StandardValues(0, 0, 1, 1, Double.MAX_VALUE));
    }

    private static Calculation defaultSmeltingCalculation() {
        return new LinearCalculation(new StandardValues(0, 0, 0, 0, Double.MAX_VALUE));
    }

    private static Calculation.StandardValues getValues(Element node) {
        double xOffset = getDoubleValue(node, "xOffset", 0);
        double yOffset = getDoubleValue(node, "yOffset", 0);
        double xCoefficient = getDoubleValue(node, "xCoefficient", 1);
        double yCoefficient = getDoubleValue(node, "yCoefficient", 1);
        double perBlockCap = getDoubleValue(node, "perBlockCap", Double.MAX_VALUE);
        return new StandardValues(xOffset, yOffset, xCoefficient, yCoefficient, perBlockCap);
    }

    private static String getStringValue(Element node, String name) {
        return node.getElementsByTagName(name).item(0).toString();
    }

    private static String getStringValue(Element node, String name, String defaultValue) {
        NodeList equationList = node.getElementsByTagName(name);
        String value;
        if (equationList.getLength() == 0) {
            value = defaultValue;
        } else {
            value = equationList.item(0).toString();
        }
        return value;
    }

    private static double getDoubleValue(Element node, String name) {
        String value = getStringValue(node, name);
        return Double.parseDouble(value);
    }

    private static double getDoubleValue(Element node, String name, double defaultValue) {
        try {
            String value = getStringValue(node, name);
            return Double.parseDouble(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static int getIntValue(Element node, String name) {
        String value = getStringValue(node, name);
        return Integer.parseInt(value);
    }

    private static int getIntValue(Element node, String name, int defaultValue) {
        try {
            String value = getStringValue(node, name);
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
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
        if(flag) init();
    }
}
