package com.andresviedma.tuenticontest2014.challenge14;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andresviedma.tuenticontest2014.challenge14.TrainGame.Station;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;


public class ChallengeMain {
    private final static Logger log = LoggerFactory.getLogger(ChallengeMain.class);
    
    
    public static void main(String[] args) throws Exception {
        // Process input
        InputStream in0 = System.in;
        LineNumberReader in = new LineNumberReader (new InputStreamReader (in0, "UTF-8"));
        PrintWriter out = new PrintWriter (new OutputStreamWriter (System.out, "UTF-8"));
        
        // Cases
        String line = in.readLine();
        int nCases = Integer.parseInt(line);
        
        for (int i=1; i<=nCases; i++) {
            log.info("*** Case #" + i);
            
            List<String> pieces = Splitter.on(',').trimResults().splitToList(in.readLine());
            int stationCount = Integer.parseInt(pieces.get(0));
            int routeCount = Integer.parseInt(pieces.get(1));
            int fuel = Integer.parseInt(pieces.get(2));
            
            TrainGame game = new TrainGame(fuel);
            for (int j=0;j<stationCount; j++) {
                List<String> statPieces = Splitter.on(CharMatcher.WHITESPACE).trimResults().splitToList(in.readLine());
                Station station = new Station();
                station.setName(statPieces.get(0));
                int iComma = statPieces.get(1).indexOf(',');
                station.setX(Integer.parseInt(statPieces.get(1).substring(0, iComma)));
                station.setY(Integer.parseInt(statPieces.get(1).substring(iComma + 1)));
                station.setWagonDestination(statPieces.get(2));
                station.setWagonScore(Integer.parseInt(statPieces.get(3)));
                
                game.addStation(station);
            }
            
            for (int j=0; j<routeCount; j++) {
                List<String> routePieces = Splitter.on(CharMatcher.WHITESPACE).trimResults().splitToList(in.readLine());
                game.addRoute(routePieces.get(0), routePieces.subList(1, routePieces.size()));
            }
            
            int res = game.getMaxScore();
            out.println(res);
        }
        
        out.close();
        in.close();
    }

}
