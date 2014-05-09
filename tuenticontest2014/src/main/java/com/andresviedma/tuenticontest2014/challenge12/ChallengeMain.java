package com.andresviedma.tuenticontest2014.challenge12;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;


public class ChallengeMain {
    private final static Logger log = LoggerFactory.getLogger(ChallengeMain.class);
    
    
    public static void main(String[] args) throws Exception {
        // Process input
        InputStream in0 = System.in;
//in0 = ChallengeMain.class.getResourceAsStream("in.txt");
        LineNumberReader in = new LineNumberReader (new InputStreamReader (in0, "UTF-8"));
        PrintWriter out = new PrintWriter (new OutputStreamWriter (System.out, "UTF-8"));
        
        // Cases
        String line = in.readLine();
        int nCases = Integer.parseInt(line);
        
        for (int i=1; i<=nCases; i++) {
            log.info("*** Case #" + i);
            
            line = in.readLine();
            List<String> pieces = Splitter.on(CharMatcher.WHITESPACE).splitToList(line);
            int n = Integer.parseInt(pieces.get(1));
            
            String[] map = new String[n];
            for (int j=0; j<n; j++) {
                map[j] = in.readLine();
            }

            TaxiMap taxiMap = new TaxiMap(map);
            PathFinder calc = new PathFinder(taxiMap);
            int res = calc.getShortestPath();
            out.println("Case #" + i + ": " + (res < 0? "ERROR" : res));
            log.info("Case #" + i + ": " + (res < 0? "ERROR" : res));
        }
        
        out.close();
        in.close();
    }

}
