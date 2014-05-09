package com.andresviedma.tuenticontest2014.challenge11;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

/**
 * Decrypts backup events data with only the first 29 characters of the AES-ECB
 * key, guessing the remaining 3 thanks to the known value of th last timestamp.
 */
public class MagicDecrypter {
    private String user;
    private String key29;
    private String lastTimestamp = null;
    
    private FeedBackup feedBackup;

    public MagicDecrypter(FeedBackup feedBackup, String user, String key29) {
        this.feedBackup = feedBackup;
        this.user = user;
        this.key29 = key29;
    }
    
    public List<Event> getEvents(int max) throws IOException {
        String data = this.getDecryptedData();
        if (data == null)  return Collections.emptyList();
        
        LineNumberReader in = new LineNumberReader(new StringReader(data));
        List<Event> res = new ArrayList<Event>();
        String line = in.readLine();
        while ((line != null) && line.length() > 0 && Character.isDigit(line.charAt(0))
                && res.size() < max) {
            List<String> pieces = Splitter.on(CharMatcher.WHITESPACE).splitToList(line);
            Event event = new Event();
            event.setUserId(pieces.get(0));
            event.setTimestamp(Long.parseLong(pieces.get(1)));
            event.setEventId(pieces.get(2));
            res.add(event);
            
            line = in.readLine();
        }
        return res;
    }
    
    
    private String getDecryptedData() throws IOException {
        
        byte[] data = feedBackup.getEncryptedUserData(user);
        byte[] dataTry = data;
        if (data.length > 32)  dataTry = Arrays.copyOf(data, 32);
        
        this.fillLastTimestamp();
        
        byte[] keyTry = Arrays.copyOf(this.key29.getBytes("UTF-8"), 32);
        List<Integer> validLetters = new ArrayList<Integer>();
        validLetters.addAll(ContiguousSet.create(Range.closed((int) 'a', (int) 'z'), DiscreteDomain.integers()));
        validLetters.addAll(ContiguousSet.create(Range.closed((int) 'A', (int) 'Z'), DiscreteDomain.integers()));
        String res = null;
        for (int i=0; i<validLetters.size() && (res == null); i++) {
            for (int j=0; j<validLetters.size() && (res == null); j++) {
                for (int k=0; k<validLetters.size() && (res == null); k++) {
                    keyTry[29] = (byte) (int) validLetters.get(i);
                    keyTry[30] = (byte) (int) validLetters.get(j);
                    keyTry[31] = (byte) (int) validLetters.get(k);
                    
                    res = this.tryKey(dataTry, keyTry);
                }
            }
        }
        if (data != dataTry)   res = this.tryKey(data, keyTry);
        return res;
    }
    
    
    private String tryKey (byte[] data, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding", "SunJCE");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] bytesDecrypted = cipher.doFinal(data);
            String eventsUser = new String(bytesDecrypted, "UTF-8");
            if (eventsUser.contains(this.lastTimestamp))
                return eventsUser;
            else
                return null;
            
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    private void fillLastTimestamp() throws IOException {
        if (this.lastTimestamp == null) {
            this.lastTimestamp = this.feedBackup.getUserLastTimestamp(user);
        }
    }
    
}
