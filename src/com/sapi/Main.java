package com.sapi;

import java.util.List;

public class Main {

    /*
     *
     *  OTOBÜS DURAKLARI
     *
     *  Mavi Durak      : 40.7620487,30.3619058
     *  Kişisel Durak   : 40.852783 ,30.307383
     *  Kampus          : 40.7408367,30.3298024
     *
     */

    private static Coordinate current = new Coordinate(40.7408367,30.3298024);

    public static void main(String[] args) throws Exception {
        Utils utils = new Utils();
        City city = new City();

        List<Line> lineList = city.lineList;
        for (Line line : lineList) {
            List<Bus> busList = line.busList;
            for (Bus bus : busList) {
                System.out.println("ID:"+bus.id+" "+line.name+" hattı üzerinde ve durağınıza "+utils.distanceTwoCoordinate(bus.coordinate,current)+" metre uzaklıkta.");
            }
        }
    }
}
