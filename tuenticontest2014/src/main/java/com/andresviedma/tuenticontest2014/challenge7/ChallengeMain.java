package com.andresviedma.tuenticontest2014.challenge7;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;


public class ChallengeMain {

    public static void main(String[] args) throws Exception {
        
        // Process input: Terrorists
        LineNumberReader in = new LineNumberReader (new InputStreamReader (System.in, "UTF-8"));
        int t1 = Integer.parseInt(in.readLine());
        int t2 = Integer.parseInt(in.readLine());
        in.close();

        // Create watcher
        PhoneCallsWatcher watcher = new PhoneCallsWatcher();
        watcher.setTerrorist1(t1);
        watcher.setTerrorist2(t2);
        
        // Process call log
        URL logUrl = ChallengeMain.class.getResource("phone_call.log");
        InputStream inLog0 = logUrl.openStream();
        LineNumberReader inLog = new LineNumberReader (new InputStreamReader (inLog0, "UTF-8"));
        int callId = -1;
        String line = inLog.readLine();
        while ((line != null) && (line.length() > 0) && (callId < 0)) {
            List<String> pieces = Splitter.on(CharMatcher.WHITESPACE).splitToList(line);
            int a = Integer.parseInt(pieces.get(0));
            int b = Integer.parseInt(pieces.get(1));
            callId = watcher.registerCall(a, b);
            
            line = inLog.readLine();
        }
        
        // Write result
        PrintWriter out = new PrintWriter (new OutputStreamWriter (System.out, "UTF-8"));
        if (callId < 0) {
            out.println("Not connected");
        } else {
            out.println("Connected at " + callId);
        }
        out.close();
    }

}
