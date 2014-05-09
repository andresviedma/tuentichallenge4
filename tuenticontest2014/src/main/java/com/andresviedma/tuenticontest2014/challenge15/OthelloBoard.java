
package com.andresviedma.tuenticontest2014.challenge15;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * An Othello board, with the current state of every cell. It's able to
 * know whether a move is valid or not, and also to update the state of
 * every affected cell when a disc is placed.
 * 
 * This class is immutable, creating new objects when trying to change the
 * state.
 */
public class OthelloBoard implements Cloneable {
    
    public class Position {
        private int x;
        private int y;

        private Position(int _x, int _y) {
            this.x = _x;
            this.y = _y;
        }

        public Position adjacentPosition(MoveDirection way) {
            return position(x + way.getXOffset(), y + way.getYOffset());
        }

        public Position adjacentCyclePosition(MoveDirection way) {
            int x2 = (width + x + way.getXOffset()) % width;
            int y2 = (width + y + way.getYOffset()) % width;
            return position(x2, y2);
        }

        public String toString() {
            char letra = (char) (((int) 'a') + y);
            return letra + Integer.toString(x + 1);
        }

        public boolean equals(Object o) {
            return (o != null) && (x == ((Position) o).x)
                    && (y == ((Position) o).y);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    private int width;

    private PlayerSide[][] cells = null;
    
    // Filled in the constructor, but not modifiable (it can't be final).
    // Shared by all clones (same size). It's been created for performance.
    private List<Position> positionList = null;

    
    public OthelloBoard() {
        this(8);
    }
    
    public OthelloBoard(int _width) {
        this.width = _width;
        this.fillCellsList();
        this.cells = new PlayerSide[width][width];

        // Initial game position
        int halfPos = (width / 2) - 1;
        this.cells[halfPos][halfPos] = PlayerSide.WHITE;
        this.cells[halfPos + 1][halfPos + 1] = PlayerSide.WHITE;
        this.cells[halfPos][halfPos + 1] = PlayerSide.BLACK;
        this.cells[halfPos + 1][halfPos] = PlayerSide.BLACK;
    }
    
    public OthelloBoard(PlayerSide[][] _cells) {
        this.width = _cells.length;
        this.fillCellsList();
        this.cells = new PlayerSide[width][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                this.cells[i][j] = _cells[i][j];
            }
        }
    }

    public OthelloBoard(List<String> _cells) {
        this.width = _cells.size();
        this.fillCellsList();
        this.cells = new PlayerSide[width][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                char c = _cells.get(i).charAt(j);
                this.cells[i][j] = (c == 'X'? PlayerSide.BLACK :
                                    c == 'O'? PlayerSide.WHITE :
                                    null);
            }
        }
    }

    public boolean isFinished() {
        boolean bExistsEmpty = false;
        boolean bExistsWhite = false;
        boolean bExistsBlack = false;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                bExistsEmpty = bExistsEmpty || (this.cells[i][j] == null);
                bExistsWhite = bExistsWhite
                        || (this.cells[i][j] == PlayerSide.WHITE);
                bExistsBlack = bExistsBlack
                        || (this.cells[i][j] == PlayerSide.BLACK);
            }
        }
        boolean bFinished = !bExistsEmpty || !bExistsBlack || !bExistsWhite;
        return bFinished;
    }

    private void setCellOwner(Position pos, PlayerSide owner) {
        cells[pos.x][pos.y] = owner;
    }

    public PlayerSide getCellOwner(Position pos) {
        if (pos == null)
            return null;
        else
            return cells[pos.x][pos.y];
    }

    public boolean isEmpty(Position pos) {
        return (cells[pos.x][pos.y] == null);
    }

    private boolean isValidPosition(int x, int y) {
        return ((x >= 0) && (x < width) && (y >= 0) && (y < width));
    }


    public Iterator<Position> allCells() {
        return positionList.iterator();
    }

    private void fillCellsList() {
        List<Position> positions = new ArrayList<Position>(width * width);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                positions.add(new Position(j, i));
            }
        }
        this.positionList = Collections.unmodifiableList(positions);
    }

    /**
     * Returns true when the player can place a disc in the given position.
     */
    public boolean isValidMove(PlayerSide t, Position pos) {
        return (getEatableCount(t, pos) > 0);
    }

    public int getEatableCount(PlayerSide t, Position pos) {
        return getEatablePositions(t, pos).size();
    }

    public List<Position> getEatablePositions(PlayerSide t, Position pos) {
        List<Position> res = new ArrayList<Position>();
        fillEatable(t, pos, res);
        return res;
    }

    private void fillEatable(PlayerSide t, Position pos, List<Position> res) {
        if (isEmpty(pos)) {
            // Collect all "eatable" for every direction
            for (int i = 0; i < MoveDirection.ALL.length; i++) {
                fillEatable(t, pos, MoveDirection.ALL[i], res);
            }
        }
    }

    private void fillEatable(PlayerSide t, Position pos, MoveDirection way,
            List<Position> res) {
        // Check that first adjacent is owned by the opposite player
        Position next = pos.adjacentPosition(way);
        PlayerSide opposite = this.getCellOwner(next);
        if ((opposite != null) && (opposite != t)) {
            // Iterate all the adjacent owned by the same team
            PlayerSide nextOwner = opposite;
            while (nextOwner == opposite) {
                next = next.adjacentPosition(way);
                nextOwner = this.getCellOwner(next);
            }

            // If next one is owned by t, ok; if not (empty), it can be eaten
            if (nextOwner == t) {
                next = pos.adjacentPosition(way);
                nextOwner = opposite;
                while (nextOwner == opposite) {
                    res.add(next);
                    next = next.adjacentPosition(way);
                    nextOwner = this.getCellOwner(next);
                }
            }
        }
    }

    public Position position(int x, int y) {
        if (!isValidPosition(x, y))
            return null;
        else
            return this.positionList.get((y * width) + x);
    }

    public Position position(String name) {
        if ((name == null) || (name.length() != 2)) {
            return null;
        } else {
            name = name.toLowerCase();
            int y = (name.charAt(0) - 'a');
            int x = (name.charAt(1) - '1');
            return position(x, y);
        }
    }


    /**
     * Places a disc in a cell and returns the resulting board, or null if
     * the move is not valid. This method doesn't affect the board object.
     */
    public OthelloBoard move(PlayerSide t, Position pos) {
        if (!isEmpty(pos)) {
            return null;
        } else {
            List<Position> eatable = this.getEatablePositions(t, pos);
            if (eatable.isEmpty()) {
                return null;
            } else {
                OthelloBoard res = (OthelloBoard) this.clone();
                for (int i = eatable.size() - 1; i >= 0; i--) {
                    Position pEat = eatable.get(i);
                    res.setCellOwner(pEat, t);
                }
                res.setCellOwner(pos, t);

                return res;
            }
        }
    }

    public boolean canMove(PlayerSide t) {
        boolean bOk = false;
        Iterator<Position> e = this.allCells();
        while (e.hasNext() && !bOk) {
            Position pos = e.next();
            bOk = this.isValidMove(t, pos);
        }
        return bOk;
    }

    public List<Position> getValidMoves(PlayerSide t) {
        List<Position> res = new ArrayList<Position>();
        Iterator<Position> e = this.allCells();
        while (e.hasNext()) {
            Position pos = e.next();
            if (this.isValidMove(t, pos))
                res.add(pos);
        }
        return res;
    }


    public Object clone() {
        try {
            OthelloBoard res = (OthelloBoard) super.clone();
            res.cells = new PlayerSide[width][width];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < width; j++) {
                    res.cells[i][j] = this.cells[i][j];
                }
            }
            return res;
            
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public int getWidth() {
        return width;
    }
    
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for(int i=0; i<width; i++) {
            for(int j=0; j<width; j++) {
                buf.append(cells[i][j] == PlayerSide.BLACK? 'X'
                            : cells[i][j] == PlayerSide.WHITE? 'O'
                            : '.');
            }
            buf.append('\n');
        }
        return buf.toString();
    }

    
    /**
     * Enumerated for players.
     */
    public enum PlayerSide {
        WHITE, BLACK;
        
        public PlayerSide getOpposite() {
            return (this == WHITE? BLACK : WHITE);
        }
    }
    

    /**
     * Enumerated to better handle directed exploration of the board.
     */
    public enum MoveDirection {
        UPLEFT(-1, -1),
        UP(0, -1),
        UPRIGHT(1, -1),
        LEFT(-1, 0),
        RIGHT(1, 0),
        DOWNLEFT(-1, 1),
        DOWN(0, 1),
        DOWNRIGHT(1, 1);
        
        public static final MoveDirection[] ALL = new MoveDirection[] {
            UPLEFT, UP, UPRIGHT, LEFT, RIGHT, DOWNLEFT, DOWN, DOWNRIGHT
        };
        
        public static final MoveDirection[] ALL_AXES = new MoveDirection[] {
            UPLEFT, UP, UPRIGHT, LEFT
        };
        
        private int xOffset;
        private int yOffset;
        
        private MoveDirection (int _xOffset, int _yOffset) {
            this.xOffset = _xOffset;
            this.yOffset = _yOffset;
        }

        public int getXOffset () {
            return xOffset;
        }

        public int getYOffset() {
            return yOffset;
        }
        
        public MoveDirection getOppositeDirection() {
            int currentPos = (xOffset + 1) + ((yOffset + 1) * 3);
            if (currentPos > 3)  currentPos--;
            int oppositePos = (7 - currentPos);
            return ALL [oppositePos];
        }
        
    }
    
}
