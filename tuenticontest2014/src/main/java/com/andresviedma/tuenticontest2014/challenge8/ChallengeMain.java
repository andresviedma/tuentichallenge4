package com.andresviedma.tuenticontest2014.challenge8;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Splitter;


public class ChallengeMain {
    
    private static Table[] readCase(LineNumberReader in) throws IOException {
        Map<String, Character> names = new HashMap<String, Character>();
        Table[] tables = new Table[2];
        int lastId = 0;
        
        for (int i=0; i<2; i++) {
            // Empty line per table
            String line = in.readLine();
            
            // Cells, converted from a full name to a character 1-8
            char[][] tableData = new char[3][];
            for (int j=0; j<3; j++) {
                line = in.readLine();
                
                List<String> row = Splitter.on(',').trimResults().splitToList(line);
                char[] newRow = new char[row.size()];
                for (int k=0; k<row.size(); k++) {
                    String name = row.get(k);
                    if (name.length() == 0) {
                        newRow[k] = '#';
                    } else {
                        if (names.containsKey(name)) {
                            newRow[k] = names.get(name);
                        } else {
                            lastId++;
                            newRow[k] = Integer.toString(lastId).charAt(0);
                            names.put(name, newRow[k]);
                        }
                    }
                }
                tableData[j] = newRow;
            }
            
            tables[i] = new Table(tableData);
        }
        
        return tables;
    }

    public static void main(String[] args) throws Exception {
        
        // Process input
        LineNumberReader in = new LineNumberReader (new InputStreamReader (System.in, "UTF-8"));
        PrintWriter out = new PrintWriter (new OutputStreamWriter (System.out, "UTF-8"));
        
        // Line with cases count
        String line = in.readLine();
        int casesCount = Integer.parseInt(line);
        
        // Cases
        for (int i=0; i<casesCount; i++) {
            Table[] tables = readCase(in);
            TableReorganizer reorg = new TableReorganizer(tables[0], tables[1]);
            int res = reorg.calculateMinMoves();
            out.println(res);
        }
        out.close();
        in.close();
    }

}
