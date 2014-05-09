package com.andresviedma.tuenticontest2014.challenge15;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andresviedma.tuenticontest2014.challenge15.OthelloBoard.PlayerSide;
import com.andresviedma.tuenticontest2014.challenge15.OthelloBoard.Position;
import com.google.common.base.Strings;

/**
 * Strategy able to check if a player is able to conquer a corner in exactly N
 * moves for sure, giving the first move needed for that.
 * 
 * Dear Tuenti Engineer:
 * I've programmed a solution a bit more elegant this time, thinking that this one
 * was going to be the last. I'm not 100% sure, although it's almost 6:00 in the
 * morning and I think this week has been exhausting enough.
 * Thanks for your work and please keep doing things like this.
 * I need a bed. Now!
 * Good night!
 *  
 */
public class CornerConqueror {
    private final static Logger log = LoggerFactory.getLogger(CornerConqueror.class);
    
    private OthelloBoard board;
    private PlayerSide conqueror;
    private PlayerSide movePlayer;
    private int maxMoves;
    
    
    public CornerConqueror(OthelloBoard board, PlayerSide conqueror,
                int maxMoves) {
        this.board = board;
        this.conqueror = conqueror;
        this.movePlayer = conqueror;
        this.maxMoves = maxMoves;
    }

    public CornerConqueror(OthelloBoard board, PlayerSide conqueror,
                PlayerSide movePlayer, int maxMoves) {
        this.board = board;
        this.conqueror = conqueror;
        this.movePlayer = movePlayer;
        this.maxMoves = maxMoves;
    }

    
    public Position getMoveToWinCorner() {
        // If no corner remains empty, end
        if (!this.isCornerEmpty())  return null;
        
        // For every valid move, try it recursively and check if it wins
        List<Position> moves = this.getMovesOrPass();
        if (moves == null)  return null;
        for (int i=0; i<moves.size(); i++) {
            Position move = moves.get(i);
            log.info(getLogPrefix() + "try " + move);
            Position res = this.checkMove(move);
            if (res != null)  return res;
        }
        
        log.info(getLogPrefix() + "end");
        return null;
    }
    
    
    public boolean isCornerSureWon() {
        // If no corner remains empty, end
        if (!this.isCornerEmpty())  return false;
        
        // For every valid move, try it recursively if all of them end well
        List<Position> moves = this.getMovesOrPass();
        if (moves == null)  return false;
        boolean bOk = true;
        for (int i=0; i<moves.size() && bOk; i++) {
            Position move = moves.get(i);
            log.info(getLogPrefix() + "follow " + move);
            Position res = this.checkMove(move);
            bOk = (res != null);
        }
        
        return bOk;
        
    }

    
    private List<Position> getMovesOrPass() {
        List<Position> moves = this.board.getValidMoves(this.movePlayer);
        if (moves.isEmpty()) {
            if (this.board.isFinished()
                    || !this.board.canMove(this.movePlayer.getOpposite())) {
                return null;
            }
            moves.add(null);
        }
        return moves;
    }
    
    private Position checkMove(Position move) {
        OthelloBoard board2;
        if (move != null) { 
            board2 = this.board.move(this.movePlayer, move);
        } else {
            board2 = board;
        }
        
        // Any corner won? congratulations
        if (this.isCornerWon(board2)) {
            log.debug("\n" + this.board.toString());
            log.info(getLogPrefix() + "OK!");
            return move;
            
        } else {
            // Check if you'll sure win one, if there are moves remaining
            int maxMoves2 = maxMoves;
            if (this.conqueror == this.movePlayer)  maxMoves2--;
            if (maxMoves2 > 0) {
                CornerConqueror conq2 = new CornerConqueror(board2,
                        conqueror, movePlayer.getOpposite(), maxMoves2);
                if (this.conqueror == this.movePlayer) {
                    if (conq2.isCornerSureWon())
                        return move;
                    else
                        return null;
                    
                } else {
                    Position pos2 = conq2.getMoveToWinCorner();
                    return pos2;
                }
                
            // No moves remaining, 
            } else {
                log.info(getLogPrefix() + "end");
                return null;
            }
        }
    }
    
    private boolean isCornerWon(OthelloBoard board2) {
        if (this.conqueror != this.movePlayer)  return false;
        
        boolean bWon = this.board.isEmpty(board.position("a1"))
                && board2.getCellOwner(board2.position("a1")) == this.conqueror;
        bWon = bWon || this.board.isEmpty(board.position("h1"))
                && board2.getCellOwner(board2.position("h1")) == this.conqueror;
        bWon = bWon || this.board.isEmpty(board.position("a8"))
                && board2.getCellOwner(board2.position("a8")) == this.conqueror;
        bWon = bWon || this.board.isEmpty(board.position("h8"))
                && board2.getCellOwner(board2.position("h8")) == this.conqueror;
        return bWon;
    }
    
    private String getLogPrefix() {
        int indent = 4 * (5 - maxMoves) * 2;
        if (this.conqueror != this.movePlayer) indent -= 4;
        return Strings.padEnd("", indent, ' ') + "[" + this.maxMoves + "," + this.movePlayer + "]: ";
    }
    
    private boolean isCornerEmpty() {
        log.debug("\n" + this.board.toString());
        
        boolean bEmpty = this.board.isEmpty(board.position("a1"));
        bEmpty = bEmpty || this.board.isEmpty(board.position("h1"));
        bEmpty = bEmpty || this.board.isEmpty(board.position("a8"));
        bEmpty = bEmpty || this.board.isEmpty(board.position("h8"));
        return bEmpty;
    }
    
}
