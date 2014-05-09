package com.andresviedma.tuenticontest2014.challenge11;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;


public class ChallengeMain {
    private final static Logger log = LoggerFactory.getLogger(ChallengeMain.class);
    
    
    public static void main(String[] args) throws Exception {
        // Allow smaller keys
        Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
        field.setAccessible(true);
        field.set(null, java.lang.Boolean.FALSE);
        
        // Process input
        LineNumberReader in = new LineNumberReader (new InputStreamReader (System.in, "UTF-8"));
        
        PrintWriter out = new PrintWriter (new OutputStreamWriter (System.out, "UTF-8"));
        
        FeedBackup backup = new FeedBackup();
        
        // Cases
        int n=1;
        String line = in.readLine();
        while ((line != null) && (line.length() > 0)) {
            List<String> pieces = Splitter.on(';').trimResults().splitToList(line);
            int maxEvents = Integer.parseInt(pieces.get(0));
            
            log.info("*** Case " + n++ + " - results: " + maxEvents);
            long ms1 = System.currentTimeMillis();
            
            FeedBackupRecovery recovery = new FeedBackupRecovery(backup);
            for (int i=1; i<pieces.size(); i++) {
                int comma = pieces.get(i).indexOf(',');
                String usr = pieces.get(i).substring(0, comma).trim();
                String key = pieces.get(i).substring(comma+1).trim();
                recovery.addFriend(usr, key);
            }
            
            List<Event> res = recovery.getUserFeedEvents(maxEvents);
            
            log.info("-> Results obtained: " + res.size());
            long ms2 = System.currentTimeMillis();
            log.info("Time: " + (ms2 - ms1));
            
            for (int i=0; i<res.size(); i++) {
                if(i != 0) out.print(" ");
                out.print(res.get(i).getEventId());
            }
            out.println();
            
            line = in.readLine();
        }
        out.close();
        in.close();
    }

}
