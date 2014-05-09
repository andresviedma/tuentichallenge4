package com.andresviedma.tuenticontest2014.challenge15;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andresviedma.tuenticontest2014.challenge15.OthelloBoard.PlayerSide;
import com.andresviedma.tuenticontest2014.challenge15.OthelloBoard.Position;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;


public class ChallengeMain {
    private final static Logger log = LoggerFactory.getLogger(ChallengeMain.class);
    
    private final static int BOARD_WIDTH = 8;
    
    
    public static void main(String[] args) throws Exception {
        // Process input
        InputStream in0 = System.in;
        LineNumberReader in = new LineNumberReader (new InputStreamReader (in0, "UTF-8"));
        PrintWriter out = new PrintWriter (new OutputStreamWriter (System.out, "UTF-8"));
        
        // Cases
        String line = in.readLine();
        int nCases = Integer.parseInt(line);
        
        for (int i=1; i<=nCases; i++) {
            log.info("*** Case #" + i);
            
            List<String> pieces = Splitter.on(CharMatcher.WHITESPACE).trimResults().splitToList(in.readLine());
            PlayerSide player = PlayerSide.valueOf(pieces.get(0).toUpperCase());
            int m = Integer.parseInt(pieces.get(2));
            
            List<String> boardLines = new ArrayList<String>(BOARD_WIDTH);
            for (int j=0; j<BOARD_WIDTH; j++) {
                boardLines.add(in.readLine());
            }
            
            OthelloBoard board = new OthelloBoard(boardLines);
            CornerConqueror conq = new CornerConqueror(board, player, m);
            
            Position res = conq.getMoveToWinCorner();
            out.println(res == null? "Impossible" : res);
        }
        
        out.close();
        in.close();
    }

}
