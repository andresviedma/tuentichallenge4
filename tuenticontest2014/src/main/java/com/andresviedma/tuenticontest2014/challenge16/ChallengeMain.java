package com.andresviedma.tuenticontest2014.challenge16;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;


public class ChallengeMain {
    private final static Logger log = LoggerFactory.getLogger(ChallengeMain.class);
    
    public static void main(String[] args) throws Exception {
        // Process input
        InputStream in0 = System.in;
        LineNumberReader in = new LineNumberReader (new InputStreamReader (in0, "UTF-8"));
        PrintWriter out = new PrintWriter (new OutputStreamWriter (System.out, "UTF-8"));
        
        // Just two values
        String line = in.readLine();
        int iComma = line.indexOf(',');
        int from = Integer.parseInt(line.substring(0, iComma).trim());
        int size = Integer.parseInt(line.substring(iComma + 1).trim());
        log.info("From: " + from + ", size: " + size);
        
        Stopwatch crono = Stopwatch.createStarted();
        PointObservations obs = new PointObservations();
        long res = obs.calculateCollisionDataCount(from, size);
        
        out.println(res);
        log.info("Time: " + crono.toString());
        
        out.close();
        in.close();
    }

}
