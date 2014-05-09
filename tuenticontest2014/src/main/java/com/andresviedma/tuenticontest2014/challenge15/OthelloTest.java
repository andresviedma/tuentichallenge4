package com.andresviedma.tuenticontest2014.challenge15;

import java.util.Arrays;

import com.andresviedma.tuenticontest2014.challenge15.OthelloBoard.PlayerSide;
import com.andresviedma.tuenticontest2014.challenge15.OthelloBoard.Position;

public class OthelloTest {
    public static void main(String[] args) {
        test8();
    }
    public static void testInitial() {
        OthelloBoard board = new OthelloBoard(Arrays.asList(new String[] {
                ".XXXXXX.",
                "..XXOX.O",
                "..OOOXOO",
                "XOOOOOOO",
                "XXXXOOOO",
                "XXXXOOOO",
                "X...XX.O",
                ".....XX."
        }));
        CornerConqueror conq = new CornerConqueror(board, PlayerSide.WHITE, 2);
        Position pos = conq.getMoveToWinCorner();
        System.out.println(pos);
    }
    
    public static void test8() {
        OthelloBoard board = new OthelloBoard(Arrays.asList(new String[] {
                "OO..OOO.",
                ".OOOXO.O",
                ".OOXXXOO",
                ".OOOOXOO",
                ".OXXXXOO",
                "OOXXXXOO",
                "O.OOOOOO",
                ".OO..OOO"
        }));
        CornerConqueror conq = new CornerConqueror(board, PlayerSide.BLACK, 3);
        Position pos = conq.getMoveToWinCorner();
        System.out.println(pos);
        
        
    }
}
