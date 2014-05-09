package com.andresviedma.tuenticontest2014.challenge11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FeedBackupRecovery {
    private final static Logger log = LoggerFactory.getLogger(FeedBackupRecovery.class);
    
    private FeedBackup feedBackup;
    private Map<String , String> friends = new HashMap<String, String>();
    
    
    public FeedBackupRecovery(FeedBackup feedBackup) {
        this.feedBackup = feedBackup;
    }

    public void addFriend(String user, String key) {
        this.friends.put(user, key);
    }
    
    public List<Event> getUserFeedEvents(int max) throws IOException {
        
        log.info("-> friends: " + friends.size());
        int i=1;
        
        // Get events of all friends
        List<Event> events = new ArrayList<Event>();
        Iterator<Entry<String, String>> itFriends = friends.entrySet().iterator();
        while (itFriends.hasNext()) {
            Entry<String, String> entryFriend = itFriends.next();
            
            log.info ("Decrypting " + entryFriend.getKey() + " (" + i + "/" + friends.size() + ")");
            i++;
            
            MagicDecrypter magic = new MagicDecrypter(this.feedBackup,
                    entryFriend.getKey().trim(), entryFriend.getValue().trim());
            List<Event> evsFriend = magic.getEvents(max);
            if(evsFriend == null) {
                log.info("Decryption failed!!!! " + entryFriend.getKey() + " - " + entryFriend.getValue());
            } else {
                events.addAll(evsFriend);
            }
        }
        
        // Sort them by timestamp
        Collections.sort(events, new Comparator<Event>() {
            public int compare(Event o1, Event o2) {
                return (int) Math.signum(o2.getTimestamp() - o1.getTimestamp());
            }
        });

        // Return max
        return (events.size() <= max? events : events.subList(0, max));
    }
    
}
