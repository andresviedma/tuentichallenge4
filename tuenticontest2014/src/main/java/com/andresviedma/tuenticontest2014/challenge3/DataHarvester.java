package com.andresviedma.tuenticontest2014.challenge3;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.common.io.CharStreams;


public class DataHarvester {
    public BigDecimal getSample (int x, int y) throws IOException {
        URL url = new URL("http://gamblers.contest.tuenti.net/index.php");
        String urlParameters = "x=" + x + "&y=" + y;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false); 
        connection.setRequestMethod("POST"); 
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
        connection.setUseCaches (false);
   
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        
        BufferedReader in = new BufferedReader(
           new InputStreamReader(connection.getInputStream()));
        String s = CharStreams.toString(in);
        connection.disconnect();
        
        String mark0 = "<input type=\"text\" name=\"result\" value=\"";
        String mark1 = "\"";
        int i = s.indexOf(mark0);
        if (i < 0)  throw new RuntimeException("Mark not found: " + mark0);
        s = s.substring(i + mark0.length());
        int j = s.indexOf(mark1);
        if (j < 0)  throw new RuntimeException("End Mark not found: " + mark1);
        s = s.substring(0, j);
        
        return new BigDecimal(s);
    }
    
    public void saveAll(PrintWriter out) throws IOException {
        for (int x=0; x<=30; x++) {
            for (int y=0; y<=30; y++) {
                System.out.print("*");
                BigDecimal res = this.getSample(x, y);
                out.println (x + ";" + y + ";" + res);
            }
            System.out.println();
        }
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("Harvesting...");
        DataHarvester harv = new DataHarvester();
        File fich = new File("src/main/resources/com/andresviedma/tuenticontest2014/challenge3/monkeydata.txt");
        PrintWriter out = new PrintWriter(fich);
        harv.saveAll(out);
        out.close();
        
        System.out.println("OK!");
    }
}
