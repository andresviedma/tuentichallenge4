package com.andresviedma.tuenticontest2014.challenge5;

import java.util.HashMap;
import java.util.Map;

public class GameOfLifeLoopDetector {
    private long currentIteration = -1;
    private Map<GameOfLifeBoard, Long> boardStates = new HashMap<GameOfLifeBoard, Long>();
    
    public void start(GameOfLifeBoard b) {
        this.iteration(b);
    }
    
    /**
     * Register a new iteration. If a loop is detected, return the iteration of
     * the first generation in the loop. Else, return -1.
     */
    public long iteration(GameOfLifeBoard b) {
        this.currentIteration++;
        Long iLoop = this.boardStates.get(b);
        if (iLoop == null) {
            this.boardStates.put(b, this.currentIteration);
            return -1;
            
        } else {
            return iLoop.longValue();
        }
    }

    public long getCurrentIteration() {
        return currentIteration;
    }

    public static void main(String[] args) {
//        String[] data = {
//                "X------X",
//                "--------",
//                "---X----",
//                "---X----",
//                "---X----",
//                "--------",
//                "--------",
//                "X------X"
//            };
        String[] data = {
                "XX----XX",
                "XX----XX",
                "--------",
                "---XX---",
                "---XX---",
                "--------",
                "XX----XX",
                "XX----XX"
            };
        GameOfLifeBoard board = new GameOfLifeBoard(data);
        
        GameOfLifeLoopDetector detector = new GameOfLifeLoopDetector();
        detector.start(board);
        long iLoop = -1;
        while (iLoop < 0) {
            board = board.nextGeneration();
            iLoop = detector.iteration(board);
        }
        
        // Write results
        long loopSize = detector.getCurrentIteration() - iLoop;
        System.out.println(iLoop + " " + loopSize);
    }
}
