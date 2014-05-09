package com.andresviedma.tuenticontest2014.challenge12;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.andresviedma.tuenticontest2014.challenge12.PathFinder.Graph;
import com.andresviedma.tuenticontest2014.challenge12.PathFinder.State;
import com.andresviedma.tuenticontest2014.challenge12.PathFinder.Transition;
import com.google.common.base.Objects;


public class TaxiMap implements Graph {
    
    private boolean[][] streetMap;
    private int xStart, yStart;
    private int xEnd, yEnd;

    
    public TaxiMap(String[] lines) {
        int xSize = lines.length;
        int ySize = lines[0].length();
        streetMap = new boolean[xSize][ySize];
        for (int i=0; i<xSize; i++) {
            String s = lines[i];
            for (int j=0; j<ySize; j++) {
                char c = s.charAt(j);
                streetMap[i][j] = (c != '#');
                if (c == 'S') {
                    xStart = i;
                    yStart = j;
                }
                if (c == 'X') {
                    xEnd = i;
                    yEnd = j;
                }
            }
        }
    }

    public State getStartState() {
        return new MapPosition(xStart, yStart, TaxiPathDirection.RIGHT, true);
    }

    public boolean isEndState(State state) {
        MapPosition position = (MapPosition) state;
        return ((position.getX() == xEnd) && (position.getY() == yEnd));
    }
    
    public List<Transition> getTargetTransitions(State state) {
        MapPosition position = (MapPosition) state;
        
        // Two positions to try: advance and turn
        MapPosition posAdv = position.advance();
        MapPosition posTurn = position.turnAdvance();
        List<Transition> posTry = new ArrayList<Transition>(4);
        this.addTransition(posTry, new Transition('-', posAdv));
        this.addTransition(posTry, new Transition('R', posTurn));
        
        // If first move, you can also go left or up
        if (position.isFirst()) {
            this.addTransition(posTry,
                    new Transition('l', position.directionAdvance(TaxiPathDirection.LEFT)));
            this.addTransition(posTry,
                    new Transition('u', position.directionAdvance(TaxiPathDirection.UP)));
        }
        
        // Heuristic: try first the one that gets us closer
        Collections.sort(posTry, new Comparator<Transition>() {
            public int compare(Transition t1, Transition t2) {
                MapPosition o1 = (MapPosition) t1.getTargetState();
                MapPosition o2 = (MapPosition) t2.getTargetState();
                return o2.distanceTo(xEnd, yEnd) - o2.distanceTo(xEnd, yEnd);
            }
        });
        
        return posTry;
    }

    private void addTransition(List<Transition> list, Transition trans) {
        MapPosition position = (MapPosition) trans.getTargetState();
        if ((position.x >= 0) && (position.x < streetMap.length)
                && (position.y >= 0) && (position.y < streetMap[0].length)
                && streetMap[position.x][position.y]) {
            list.add(trans);
        }
    }
    
    
    public static class MapPosition implements State {
        private int x;
        private int y;
        private TaxiPathDirection direction;
        private boolean first = false;
        
        public MapPosition(int x, int y, TaxiPathDirection direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }
        
        public MapPosition(int x, int y, TaxiPathDirection direction, boolean first) {
            this(x, y, direction);
            this.first = first;
        }
        
        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
        public boolean isFirst() {
            return first;
        }

        public MapPosition advance() {
            return new MapPosition(x + direction.getxOffset(), y + direction.getyOffset(),
                        direction);
        }
        
        public MapPosition turnAdvance() {
            TaxiPathDirection dir2 = direction.turn();
            return this.directionAdvance(dir2);
        }
        
        public MapPosition directionAdvance(TaxiPathDirection dir) {
            return new MapPosition(x + dir.getxOffset(), y + dir.getyOffset(),
                    dir);
        }
        
        public int distanceTo(int x1, int y1) {
            return Math.abs(x-x1) + Math.abs(y-y1);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(x, y, direction.toString());
        }

        @Override
        public boolean equals(Object obj) {
            if((obj == null) || (obj.getClass() != this.getClass()))  return false;
            MapPosition p = (MapPosition) obj;
            return x == p.x && y == p.y && direction == p.direction;
        }

        @Override
        public String toString() {
            return "MapPosition [x=" + x + ", y=" + y + ", direction="
                    + direction + ", first=" + first + "]";
        }
        
    }
    
    public static enum TaxiPathDirection {
        RIGHT   (0, 1),
        LEFT    (0, -1),
        UP      (-1, 0),
        DOWN    (1, 0);
        
        private final int xOffset;
        private final int yOffset;
        
        TaxiPathDirection (int xOffset, int yOffset) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
        }

        public int getxOffset() {
            return xOffset;
        }
        public int getyOffset() {
            return yOffset;
        }

        public TaxiPathDirection turn() {
            switch(this) {
                case RIGHT: return DOWN;
                case LEFT: return UP;
                case UP: return RIGHT;
                case DOWN: return LEFT;
                
                default: return RIGHT;
            }
        }
        
    }
}
