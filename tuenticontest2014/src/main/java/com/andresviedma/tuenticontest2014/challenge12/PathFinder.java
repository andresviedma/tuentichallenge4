package com.andresviedma.tuenticontest2014.challenge12;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * NOTE
 * ===============
 * 
 * Dear Tuenti Engineer:
 * 
 * Oh, no, the same problem... again! This is almost the same as challenge 8!
 * I'm pretty tired of doing the same problem, and not finding a really GREAT
 * solution to avoid loops in a proper way and find good heuristics (I could
 * swear I've found it also other times in my past... in Google Code Jam, maybe?).
 * 
 * This time, I've decided to program a reusable solution, in the form of this
 * PathFinder class. It's a pity I can't use it for challenge 8 yet, but I'm
 * in the hope that I can find this same problem again. Call it a longshot if
 * you may.
 * 
 * I'm losing some precious minutes in this, but I'm sure you will appreciate
 * it. Enjoy it, my friend.
 * 
 * @author andres
 *
 */

public class PathFinder {
    private final static Logger log = LoggerFactory.getLogger(PathFinder.class);
    
    private final static int NOT_FOUND = Integer.MAX_VALUE;
    private final static int NOT_BETTER = Integer.MAX_VALUE - 1;
    private final static int LOOP = Integer.MAX_VALUE - 2;

    private Graph graph;
    
    
    public PathFinder(Graph graph) {
        this.graph = graph;
    }
    
    
    public int getShortestPath() {
        
        // First try avoid any kind of loops. It's faster and if there's a solution it finds it.
        Map<State, Integer> visited = new HashMap<PathFinder.State, Integer>();
        State position = graph.getStartState();
        int length = this.getShortestPathFrom(position, visited, "", 0, NOT_FOUND, true);
        
        // That solution doesn't always get the best choice, so we try again the complete solution only if we found one.
        if (this.isPathFound(length)) {
            visited.clear();
            int length2 = this.getShortestPathFrom(position, visited, "", 0, length, false);
            if (this.isPathFound(length2))  length = length2;
        }
        return (this.isPathFound(length)? length : -1);
    }

    private int getShortestPathFrom(State position, Map<State, Integer> visited, String path, int acum, int bestAcum, boolean dontRepeatLoops) {
        
        log.debug(path + " : " + position + " - best: " + bestAcum + " - visited: " + visited.size());
        
        if (acum + 1 >= bestAcum) {
            return NOT_BETTER;
        }
        
        if (visited.containsKey(position))  return visited.get(position);
        
        if (graph.isEndState(position)) {
            log.info("Encontrada solución (" + path.length() + ": " + path);
            return 0;
        }
        
        int length = NOT_FOUND;
        visited.put(position, LOOP);
        
        // Try the available transitions
        List<Transition> posTry = this.graph.getTargetTransitions(position);
        for (int i=0; i<posTry.size(); i++) {
            Transition trans = posTry.get(i);
            State pos2 = trans.getTargetState();
            if (this.checkPositionAllowed(pos2, visited)) {
                int lengthPath = this.getShortestPathFrom(pos2, visited, path + trans.getRepresentation(), acum+1, bestAcum, dontRepeatLoops);
                if (this.isPathFound(lengthPath)) {
                    length = Math.min (lengthPath + 1, length);
                    bestAcum = Math.min(acum + length, bestAcum);
                } else if ((lengthPath != NOT_FOUND) && !this.isPathFound(length)) {
                    length = lengthPath;
                }
            }
        }
        
        if ((length == NOT_BETTER) || ((length == LOOP) && !dontRepeatLoops))
            visited.remove(position);
        else
            visited.put(position, length);
        
        return length;
    }
    
    private boolean isPathFound(int v) {
        return (v != NOT_BETTER) && (v != NOT_FOUND) && (v != LOOP);
    }
    
    private boolean checkPositionAllowed(State position, Map<State, Integer> visited) {
        return (!visited.containsKey(position) || visited.get(position) != Integer.MAX_VALUE);
    }

    
    public static interface Graph {
        public State getStartState();
        public boolean isEndState(State state);
        
        public List<Transition> getTargetTransitions(State state);
    }
    
    public static class Transition {
        private char representation;
        private State targetState;

        public Transition(char id, State targetState) {
            this.representation = id;
            this.targetState = targetState;
        }
        public Transition(State targetState) {
            this.representation = '-';
            this.targetState = targetState;
        }
        
        public char getRepresentation() {
            return representation;
        }
        public State getTargetState() {
            return targetState;
        }
    }
    

    /** Empty interface, just for clarity reasons */
    public static interface State {
    }
    
}
