package com.sapi;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class Utils {

    public float distanceTwoCoordinate(Coordinate from, Coordinate to) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(to.latitude-from.latitude);
        double dLng = Math.toRadians(to.longitude-from.longitude);
        double a =  Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.cos(Math.toRadians(from.latitude)) * Math.cos(Math.toRadians(to.latitude)) *
                    Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        int meterConversion = 1609;

        return (float) (dist * meterConversion);
    }

    public String getDatas() throws Exception {
        String url = "http://sakus.sakarya.bel.tr/Proxy/proxy.ashx?url=http%3A%2F%2Flocalhost%3A8080%2Fgeoserver%2Fwfs";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // Payload For All Bus
        String payload = "<wfs:GetFeature xmlns:wfs=\"http://www.opengis.net/wfs\" service=\"WFS\" version=\"1.0.0\" xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-transaction.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><wfs:Query typeName=\"feature:BelediyeOtobus\" xmlns:feature=\"http://localhost:8080/SBB\"><ogc:Filter xmlns:ogc=\"http://www.opengis.net/ogc\"><ogc:BBOX><ogc:PropertyName>GEOM</ogc:PropertyName><gml:Box xmlns:gml=\"http://www.opengis.net/gml\" srsName=\"EPSG:4326\"><gml:coordinates decimal=\".\" cs=\",\" ts=\" \">16.310555999999,37.476290486075 44.435555999999,44.000366126517</gml:coordinates></gml:Box></ogc:BBOX></ogc:Filter></wfs:Query></wfs:GetFeature>";
        //
        con.setRequestMethod("POST");
        // Self Kit
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.102 Safari/537.36");
        //
        con.setUseCaches(false);
        con.setRequestProperty("Pragma","no-cache");
        con.setRequestProperty("Accept","*/*");
        con.setRequestProperty("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.6,en;q=0.4");
        con.setRequestProperty("Accept-Encoding","gzip,deflate,sdch");
        con.setRequestProperty("Origin","http://sakus.sakarya.bel.tr");
        con.setRequestProperty("Content-Type","application/xml");
        con.setRequestProperty("Cache-Control","no-cache");
        con.setRequestProperty("Referer","http://sakus.sakarya.bel.tr/");
        con.setRequestProperty("Connection","keep-alive");
        con.setDoOutput(true);
        con.setDoInput(true);
        OutputStream os = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        writer.write(payload);
        writer.close();
        os.close();
        InputStream inputStream = con.getInputStream();
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        GZIPInputStream gzipInputStream = null;
        byte[] buf = new byte[1024];
        int len;
        try {
            gzipInputStream = new GZIPInputStream(inputStream);
            while ((len = gzipInputStream.read(buf)) > 0) {
                bas.write(buf, 0, len);
            }
        }
        catch(Exception e){}
        try {
            gzipInputStream.close();
        } catch(Exception e){}
        try {
            bas.close();
        } catch(Exception e){}
        try {
            inputStream.close();
        } catch(Exception e){}
        return bas.toString();
    }

    public NodeList getNodes(String datas) throws Exception{
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        Document xmlDocument = builder.parse(new ByteArrayInputStream(datas.getBytes()));

        return xmlDocument.getElementsByTagName("gml:coordinates");
    }
}
