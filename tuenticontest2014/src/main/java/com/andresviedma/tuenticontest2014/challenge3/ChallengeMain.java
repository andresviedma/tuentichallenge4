package com.andresviedma.tuenticontest2014.challenge3;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;


public class ChallengeMain {

    public static void main(String[] args) throws Exception {
        
        // Prepare calculator
        GamblersCalculator calc = new GamblersCalculator();
        calc.loadData();
        
        // Process input
        LineNumberReader in = new LineNumberReader (new InputStreamReader (System.in, "UTF-8"));
        PrintWriter out = new PrintWriter (new OutputStreamWriter (System.out, "UTF-8"));

        // First line: n cases
        String linea = in.readLine();
        
        // Input cases
        linea = in.readLine();
        while ((linea != null) && (linea.length() > 0)) {
            List<String> pieces = Splitter.on(CharMatcher.WHITESPACE).trimResults().omitEmptyStrings().splitToList(linea);
            int x = Integer.parseInt(pieces.get(0));
            int y = Integer.parseInt(pieces.get(1));
            BigDecimal res = calc.calculate(x, y);
            out.println(res);
            
            linea = in.readLine();
        }
        out.close();
    }

}
