package com.andresviedma.tuenticontest2014.challenge7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PhoneCallsWatcher {
    Map<Integer, Set<Integer>> networks = new HashMap<Integer, Set<Integer>>(1000000);
    
    List<Set<Integer>> emptySetsPool = new ArrayList<Set<Integer>>();
    
    private int terrorist1;
    private int terrorist2;
    
    private int callNumber = -1;

    
    /**
     * Register a call a-b, and return the call number if terrorists are now
     * connected, or -1 if not.
     */
    public int registerCall(int a, int b) {
        
        this.callNumber++;
        
        // Find networks
        Set<Integer> net1 = this.networks.get(a);
        Set<Integer> net2 = this.networks.get(b);

        // Connected yet, no new info
        if ((net1 == net2) && (net1 != null))  return -1;
        
        // Both are new
        if ((net1 == null) && (net2 == null)) {
            Set<Integer> net;
            if (!this.emptySetsPool.isEmpty()) {
                int iLast = this.emptySetsPool.size() - 1;
                net = this.emptySetsPool.get(iLast);
                this.emptySetsPool.remove(iLast);
            } else {
                net = new HashSet<Integer>(200);
            }
            net.add(a);
            net.add(b);
            this.networks.put(a, net);
            this.networks.put(b, net);
            
        // One is new
        } else if (net1 == null) {
            net2.add(a);
            this.networks.put(a, net2);
        } else if (net2 == null) {
            net1.add(b);
            this.networks.put(b, net1);

        // Mix networks
        } else {
            // Iterate and delete the smaller, it's much faster
            Set<Integer> netMin = (net1.size() > net2.size()? net2 : net1);
            Set<Integer> netMax = (net1.size() > net2.size()? net1 : net2);
            
            Iterator<Integer> it2 = netMin.iterator();
            while (it2.hasNext()) {
                Integer n = it2.next();
                this.networks.put(n, netMax);
                netMax.add(n);
            }
            
            // Use a pool of empty sets to reuse, to avoid GCs
            netMin.clear();
            this.emptySetsPool.add(netMin);
        }
        
        Set<Integer> netT1 = this.networks.get(terrorist1);
        return (netT1 != null && netT1.contains(terrorist2)? callNumber : -1);
    }
    
    public int getNetworkCount() {
        return this.networks.size();
    }

    
    public int getTerrorist1() {
        return terrorist1;
    }
    public void setTerrorist1(int terrorist1) {
        this.terrorist1 = terrorist1;
    }

    public int getTerrorist2() {
        return terrorist2;
    }
    public void setTerrorist2(int terrorist2) {
        this.terrorist2 = terrorist2;
    }

}
