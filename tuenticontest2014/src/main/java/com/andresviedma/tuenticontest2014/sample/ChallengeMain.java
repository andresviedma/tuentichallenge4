package com.andresviedma.tuenticontest2014.sample;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.StringTokenizer;

public class ChallengeMain {

    public static void main(String[] args) throws Exception {
        LineNumberReader in = new LineNumberReader (new InputStreamReader (System.in, "UTF-8"));
        PrintWriter out = new PrintWriter (new OutputStreamWriter (System.out, "UTF-8"));

        String linea = in.readLine();
        while ((linea != null) && (linea.length() > 0)) {
            StringTokenizer tok = new StringTokenizer(linea);
            BigInteger res = BigInteger.valueOf(0);
            while (tok.hasMoreTokens()) {
                String p = tok.nextToken();
                res = res.add(new BigInteger(p));
            }

            out.println (res);
            
            linea = in.readLine();
        }
        out.close();
    }

}
