package com.andresviedma.tuenticontest2014.challenge4;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import com.google.common.base.Joiner;


public class ChallengeMain {

    public static void main(String[] args) throws Exception {
        
        // Process input
        LineNumberReader in = new LineNumberReader (new InputStreamReader (System.in, "UTF-8"));
        PrintWriter out = new PrintWriter (new OutputStreamWriter (System.out, "UTF-8"));

        // First lines: source-target
        String src = in.readLine();
        String target = in.readLine();
        
        // Safe states
        ShapeShifter ss = new ShapeShifter();
        String line = in.readLine();
        while ((line != null) && (line.length() > 0)) {
            ss.addSafeState(line);
            line = in.readLine();
        }
        in.close();

        // Solve
        List<String> path = ss.changeDna(src, target);
        if(path == null) {
            out.println("(null)");
        } else {
            String res = Joiner.on("->").join(path);
            out.println(res);
        }
        out.close();
    }

}
