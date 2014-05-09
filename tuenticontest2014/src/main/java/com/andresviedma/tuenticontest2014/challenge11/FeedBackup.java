package com.andresviedma.tuenticontest2014.challenge11;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;


public class FeedBackup {
    
    public String getUserLastTimestamp(String user) throws IOException {
        String path = "feeds/last_times/"
                    + user.substring(user.length() - 2) + "/"
                    + user + ".timestamp";
        InputStream inLog0 = new FileInputStream(getFile(path));
        BufferedReader in = new BufferedReader(
                new InputStreamReader(inLog0));
        String s = CharStreams.toString(in);
        in.close();
        return s;
    }
    
    public byte[] getEncryptedUserData(String user) throws IOException {
        String path = "feeds/encrypted/"
                + user.substring(user.length() - 2) + "/"
                + user + ".feed";
        InputStream in = new FileInputStream(getFile(path));
        byte[] res = ByteStreams.toByteArray(in);
        in.close();
        return res;
    }
    
    private File getFile(String path) {
        return new File("src/main/data/challenge11/" + path);
    }

}
