package com.sapi;

import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class City {

    private Utils utils = new Utils();
    public List<Line> lineList = new ArrayList<Line>();

    public City() {
        String datas = null;
        NodeList nodeList = null;
        try {
            datas = utils.getDatas();
            nodeList = utils.getNodes(datas);
        } catch (Exception e) {
             e.printStackTrace();
        }

        for(int i=0;i<nodeList.getLength();i++) {
            String lineName = nodeList.item(i).getParentNode().getParentNode().getParentNode().getFirstChild().getNextSibling().getTextContent();
            boolean foundLineName = false;
            for(Line line : lineList) {
                if(line.name.equalsIgnoreCase(lineName)) {
                    foundLineName = true;
                }
            } if(!foundLineName) {
                Line line = new Line();
                line.name = lineName;
                lineList.add(line);
            }
        } for(int i=0;i<nodeList.getLength();i++) {
            Integer busNo = Integer.parseInt(nodeList.item(i).getParentNode().getParentNode().getParentNode().getFirstChild().getTextContent());
            String lineName = nodeList.item(i).getParentNode().getParentNode().getParentNode().getFirstChild().getNextSibling().getTextContent();
            String coordinate = nodeList.item(i).getTextContent();
            for(Line line : lineList) {
                if(line.name.equalsIgnoreCase(lineName)) {
                    String[] temp = coordinate.split(",");
                    Bus bus = new Bus();
                    bus.id = busNo;
                    bus.coordinate = new Coordinate(Double.parseDouble(temp[1]),Double.parseDouble(temp[0]));
                    line.busList.add(bus);
                }
            }
        }

    }
}
