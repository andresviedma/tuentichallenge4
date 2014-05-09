package com.andresviedma.tuenticontest2014.challenge2;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


public class ChallengeMain {

    public static void main(String[] args) throws Exception {
        
        // Process input
        LineNumberReader in = new LineNumberReader (new InputStreamReader (System.in, "UTF-8"));
        PrintWriter out = new PrintWriter (new OutputStreamWriter (System.out, "UTF-8"));

        String linea = in.readLine();
        while ((linea != null) && (linea.length() > 0)) {
            Track t = new Track(linea);
            t.print(out);
            
            linea = in.readLine();
        }
        out.close();
    }

}
