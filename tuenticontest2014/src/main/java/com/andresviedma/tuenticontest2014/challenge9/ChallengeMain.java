package com.andresviedma.tuenticontest2014.challenge9;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;


public class ChallengeMain {
    
    private static int getNodeId (String nodeName, String cityName) {
        if (nodeName.equals(cityName))
            return TravelMap.NODE_ORIGIN;
        else if (nodeName.equals("AwesomeVille"))
            return TravelMap.NODE_TARGET;
        else
            return Integer.parseInt(nodeName);
    }
    
    public static void main(String[] args) throws Exception {
        
        // Process input
        LineNumberReader in = new LineNumberReader (new InputStreamReader (System.in, "UTF-8"));
        PrintWriter out = new PrintWriter (new OutputStreamWriter (System.out, "UTF-8"));
        
        // Line with cases count
        String line = in.readLine();
        int casesCount = Integer.parseInt(line);
        
        // Cases
        for (int i=0; i<casesCount; i++) {
            TravelMap map = new TravelMap();
            String cityName = in.readLine();
            List<String> speeds = Splitter.on(CharMatcher.WHITESPACE).trimResults().splitToList(in.readLine());
            map.setNormalSpeed(Integer.parseInt(speeds.get(0)));
            map.setDirtySpeed(Integer.parseInt(speeds.get(1)));
            List<String> nodeCount = Splitter.on(CharMatcher.WHITESPACE).trimResults().splitToList(in.readLine());
            map.setIntersectionCount(Integer.parseInt(nodeCount.get(0)));
            int roadCount = Integer.parseInt(nodeCount.get(1));
            
            // Roads
            for (int j=0; j<roadCount; j++) {
                List<String> roadPieces = Splitter.on(CharMatcher.WHITESPACE).trimResults().splitToList(in.readLine());
                map.addRoad (getNodeId(roadPieces.get(0), cityName),
                             getNodeId(roadPieces.get(1), cityName),
                             roadPieces.get(2).equals("dirt"),
                             Integer.parseInt(roadPieces.get(3)));
            }
            
            int res = map.calculateCarsByHour();
            out.println(cityName + " " + res);
        }
        out.close();
        in.close();
    }

}
