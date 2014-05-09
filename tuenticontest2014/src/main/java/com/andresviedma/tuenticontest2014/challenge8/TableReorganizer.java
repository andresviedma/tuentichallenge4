package com.andresviedma.tuenticontest2014.challenge8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TableReorganizer {
    private Table initialTable;
    private Table targetTable;
    private Map<Table, Integer> history = new HashMap<Table, Integer>(); 
    
    
    public TableReorganizer(Table initialTable, Table targetTable) {
        this.initialTable = initialTable;
        this.targetTable = targetTable;
    }

    public int calculateMinMoves() {
        return this.getMinMovesFrom(this.initialTable, 0, -1);
    }
    
    
    private int getMinMovesFrom(Table table, int acum, int bestAcum) {
        
        // Target succeeded
        if (table.equals(targetTable))  return 0;
        
        // Move tried before (heuristic)
        if (this.history.containsKey(table)) {
            int oldNum = this.history.get(table);
            
            // It didn't work, now neither if current accumulated number of moves is greater or equal
            if ((oldNum < 0) && (-oldNum <= acum))
                return oldNum;
            
            // It worked, return the previously calculated number of moves to the target
            else if (oldNum > 0)
                return oldNum;
        }
        
        // Too many iterations, or it doesn't improve our best try
        if ((acum > 2700) || ((bestAcum > 0) && (acum >= bestAcum))) {
            // Negative value for history: with this accumulated number of moves it doesn't worth
            return -acum;
        }

        // Obtain valid moves
        List<String> validMoves = new ArrayList<String>(12);
        for (int i=0; i<table.getTableSize(); i++) {
            for (int j=0; j<table.getTableSize(); j++) {
                char c1 = table.getCell(i, j);
                
                // Horizontal move
                if (j+1 < table.getTableSize()) {
                    char c2 = table.getCell(i, j+1);
                    if(getSwapPoints(table, c1, c2) <= 0)
                        validMoves.add(new String(new char[] {c1, c2}));
                }
            
                // Vertical move
                if (i+1 < table.getTableSize()) {
                    char c2 = table.getCell(i+1, j);
                    if(getSwapPoints(table, c1, c2) <= 0)
                        validMoves.add(new String(new char[] {c1, c2}));
                }
            }
        }
        
        // Heuristic: sort them by proximity to the target (it makes processing much faster)
        final Table finalTable = table;
        Collections.sort(validMoves, new Comparator<String>() {
            public int compare(String o1, String o2) {
                int points1 = getSwapPoints(finalTable, o1.charAt(0), o1.charAt(1));
                int points2 = getSwapPoints(finalTable, o2.charAt(0), o2.charAt(1));
                return points1 - points2;
            }
        });
        
        // Iterate over valid moves and calculate their required number of moves to get the target table
        int bestFinish = -1;
        history.put(table, -1);
        
        for (int i=0; i<validMoves.size(); i++) {
            String move = validMoves.get(i);
            int[] pos0 = table.getPosition(move.charAt(0));
            int[] pos1 = table.getPosition(move.charAt(1));
            bestFinish = this.testSwap(table, move.charAt(0), pos0[0],pos0[1], pos1[0], pos1[1], bestFinish, acum);
        }

        // Store the result in history, for performance
        history.put(table, bestFinish);
        
        return bestFinish;
    }
    
    private int testSwap(Table table, char c1, int i0, int j0, int i, int j, int bestFinish, int acum) {
        char c2 = table.getCell(i, j);
        if (((c1 == '#') && table.isCornerPosition(i, j))
                || (c2 == '#') && table.isCornerPosition(i0, j0)
                || ((c1 == targetTable.getCell(i0, j0)) && (c2 == targetTable.getCell(i, j)))
                || (table.equalsColumn(targetTable, j0) && (j != 1))
                || (table.equalsRow(targetTable, i0) && (i != 1))
                || this.isWorse(table, i0, j0, i, j)
                ) {
            return bestFinish;
        }
        
        Table table2 = table.swap(c1, c2);
        int res2 = this.getMinMovesFrom(table2, acum+1, (bestFinish >= 0? acum + bestFinish : -1));
        if (res2 >= 0) {
            if (bestFinish < 0)
                bestFinish = res2 + 1;
            else
                bestFinish = Math.min(bestFinish, res2 + 1);
            
        } else if ((res2 < 0) && (bestFinish < 0)) {
            bestFinish =  Math.min(bestFinish, res2 + 1);
        }
        return bestFinish;
    }
    
    private int getSwapPoints(Table table, char c0, char c1) {
        int [] pos0 = table.getPosition(c0);
        int [] pos1 = table.getPosition(c1);
        return getSwapPoints(table, pos0[0], pos0[1], pos1[0], pos1[1]);
    }

    private int getSwapPoints(Table table, int i0, int j0, int i, int j) {
        return swapOffset(table, i0, j0, i, j) + swapOffset(table, i, j, i0, j0);
    }

    private boolean isWorse(Table table, int i0, int j0, int i, int j) {
        return getSwapPoints(table, i0, j0, i, j) > 0;
    }
    private int swapOffset(Table table, int i0, int j0, int i, int j) {
        char c = table.getCell(i0, j0);
        if (c == '#')  return 0;
        int[] posTarget = targetTable.getPosition(c);
        int distance0 = Math.abs(posTarget[0] - i0) + Math.abs(posTarget[1] - j0);
        int distance1 = Math.abs(posTarget[0] - i) + Math.abs(posTarget[1] - j);
        return (distance1 - distance0);
    }
}
