package com.andresviedma.tuenticontest2014.challenge5;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class ChallengeMain {

    public static void main(String[] args) throws Exception {
        
        // Process input
        LineNumberReader in = new LineNumberReader (new InputStreamReader (System.in, "UTF-8"));
        PrintWriter out = new PrintWriter (new OutputStreamWriter (System.out, "UTF-8"));

        List<String> lines = new ArrayList<String>();
        String line = in.readLine();
        while ((line != null) && (line.length() > 0)) {
            lines.add(line);
            line = in.readLine();
        }
        
        // Create the board and iterate
        GameOfLifeBoard board = new GameOfLifeBoard((String[]) lines.toArray(new String[0]));
        GameOfLifeLoopDetector detector = new GameOfLifeLoopDetector();
        detector.start(board);
        long iLoop = -1;
        while (iLoop < 0) {
            board = board.nextGeneration();
            iLoop = detector.iteration(board);
        }
        
        // Write results
        long loopSize = detector.getCurrentIteration() - iLoop;
        out.println(iLoop + " " + loopSize);
        out.close();
    }

}
