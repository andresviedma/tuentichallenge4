package com.andresviedma.tuenticontest2014.challenge2;

import java.io.PrintWriter;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;


public class Track {
    private Table<Integer, Integer, Character> table = HashBasedTable.create();
    private int minX = 0;
    private int maxX = 0;
    private int minY = 0;
    private int maxY = 0;
    private boolean rotated = false;
    private boolean mirrorY = false;
    private boolean mirrorX = false;

    
    public Track(String s) {
        this.parseTrackString(s);
    }
    
    public void print (PrintWriter out) {
        int iMin = this.minX;
        int iMax = this.maxX;
        int jMin = this.minY;
        int jMax = this.maxY;
        if (this.rotated) {
            iMin = this.minY;
            iMax = this.maxY;
            jMin = this.minX;
            jMax = this.maxX;
        }
        if (this.mirrorY) {
            int oldjMin = jMin;
            jMin = jMax;
            jMax = oldjMin;
        }
        if (this.mirrorX) {
            int oldiMin = iMin;
            iMin = iMax;
            iMax = oldiMin;
        }

        int i = iMin;
        while ((!this.mirrorX && i<=iMax) || (this.mirrorX && i>=iMax)) {
            int j = jMin;
            while ((!this.mirrorY && j<=jMax) || (this.mirrorY && j>=jMax)) {
                Character c;
                if (this.rotated) {
                    c = this.table.get(j, i);
                } else {
                    c = this.table.get(i, j);
                }
                
                if (c == null) {
                    out.print (' ');
                } else if ((c == '-') && this.rotated) {
                    out.print ('|');
                } else if ((c == '|') && this.rotated) {
                    out.print ('-');
                } else if ((c == '\\') && this.rotated && !this.mirrorY) {
                    out.print ('/');
                } else if ((c == '/') && this.rotated && !this.mirrorY) {
                    out.print ('\\');
                } else {
                    out.print (c);
                }
                
                if(!this.mirrorY) {
                    j++;
                } else {
                    j--;
                }
            }
            out.println();
            
            if(!this.mirrorX) {
                i++;
            } else {
                i--;
            }
        }
    }
    
    private void parseTrackString(String s) {
        int x = 0;
        int y = 0;
        TrackDirection direction = TrackDirection.RIGHT;
        for (int i=0; i<s.length(); i++) {
            char c0 = s.charAt(i);
            
            // Cell transform from plain text to graphic representation char
            char c = direction.plainCharToGraph(c0);
            
            // Update current cell
            this.table.put(x, y, c);
            
            // Detection of rotated track / mirror Y
            if (c == '#') {
                this.rotated = direction.isVertical();
                this.mirrorY = (direction == TrackDirection.LEFT) || (direction == TrackDirection.UP);
                this.mirrorX = (direction == TrackDirection.LEFT) || (direction == TrackDirection.DOWN) || (direction == TrackDirection.UP);
            }
            
            // Change sense
            direction = direction.rotateChar(c);
            
            // Change offset and update limits
            x += direction.getxOffset();
            y += direction.getyOffset();
            this.minX = Math.min(this.minX, x);
            this.minY = Math.min(this.minY, y);
            this.maxX = Math.max(this.maxX, x);
            this.maxY = Math.max(this.maxY, y);
        }
    }
    
    private static enum TrackDirection {
        RIGHT   (0, 1),
        LEFT    (0, -1),
        UP      (-1, 0),
        DOWN    (1, 0);
        
        private final int xOffset;
        private final int yOffset;
        
        TrackDirection (int xOffset, int yOffset) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
        }

        public int getxOffset() {
            return xOffset;
        }
        public int getyOffset() {
            return yOffset;
        }
        
        public TrackDirection rotateLeft() {
            switch(this) {
                case RIGHT: return UP;
                case LEFT: return DOWN;
                case UP: return LEFT;
                case DOWN: return RIGHT;
                
                default: return LEFT;
            }
        }
        
        public TrackDirection rotateRight() {
            switch(this) {
                case RIGHT: return DOWN;
                case LEFT: return UP;
                case UP: return RIGHT;
                case DOWN: return LEFT;
                
                default: return RIGHT;
            }
        }
        
        public boolean isVertical() {
            return (this.xOffset != 0);
        }
        
        public TrackDirection rotateChar(char c) {
            switch(c) {
                case '/':
                    return (isVertical()? rotateRight() : rotateLeft());
                case '\\':
                    return (isVertical()? rotateLeft() : rotateRight());
                default:
                    return this;
            }
        }
        
        public char plainCharToGraph(char c) {
            // Vertical cells are transformed
            if (this.isVertical() && (c == '-')) {
                return '|';
            } else {
                return c;
            }
        }
    }
}
