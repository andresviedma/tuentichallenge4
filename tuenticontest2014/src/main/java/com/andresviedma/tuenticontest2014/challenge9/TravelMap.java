package com.andresviedma.tuenticontest2014.challenge9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Table;


public class TravelMap {
    public static final int NODE_ORIGIN = -1;
    public static final int NODE_TARGET = -2;
    
    private String name;
    private int normalSpeed;
    private int dirtySpeed;
    
    private int intersectionCount;
    
    private Table<Integer, Integer, Road> roads = HashBasedTable.create();
    
    private SetMultimap<Integer, Integer> connectionsByTarget = HashMultimap.create();
    
    
    public void addRoad(int node0, int node1, boolean isDirt, int lanes) {
        if ((node0 == NODE_TARGET) || (node1 == NODE_ORIGIN))  return;
        
        int speed = (isDirt? this.getDirtyRoadSpeed(lanes) : this.getNormalRoadSpeed(lanes));
        Road road = new Road(node0, node1, speed);
        this.roads.put(node0, node1, road);
        this.connectionsByTarget.put(node1, node0);
        
    }
    
    public int calculateGlobalSpeed() {
        Set<Integer> forbidden = new HashSet<Integer>();
        forbidden.add(NODE_TARGET);
        return this.globalSpeedTo(NODE_TARGET, forbidden);
    }
    
    private int globalSpeedTo(int targetNode, Set<Integer> forbiddenNodes) {
        Set<Integer> origins = this.connectionsByTarget.get(targetNode);
        int speed = 0;
        Iterator<Integer> itOrigins = origins.iterator();
        while (itOrigins.hasNext()) {
            int origin = itOrigins.next();
            if (!forbiddenNodes.contains(origin)) {
                int roadSpeed = this.roads.get(origin, targetNode).getSpeed();
                int speedBefore = Integer.MAX_VALUE;
                if (origin != NODE_ORIGIN) {
                    Set<Integer> forbidden2 = new HashSet<Integer>(forbiddenNodes);
                    forbidden2.add(targetNode);
                    speedBefore = this.globalSpeedTo(origin, forbidden2);
                }
                int globalRoadspeed = Math.min(roadSpeed, speedBefore);
                if (globalRoadspeed > 0)  speed += globalRoadspeed;
            }
        }
        return speed;
    }
    
    public int calculateCarsByHour() {
        int speed = this.calculateGlobalSpeed();
        return speed * 200;     // Speed / 5 meters by car
    }
    
    private int getNormalRoadSpeed(int lanes) {
        return normalSpeed * lanes;
    }
    private int getDirtyRoadSpeed(int lanes) {
        return dirtySpeed * lanes;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getNormalSpeed() {
        return normalSpeed;
    }
    public void setNormalSpeed(int normalSpeed) {
        this.normalSpeed = normalSpeed;
    }
    public int getDirtySpeed() {
        return dirtySpeed;
    }
    public void setDirtySpeed(int dirtySpeed) {
        this.dirtySpeed = dirtySpeed;
    }
    public int getIntermediateNodeCount() {
        return intersectionCount;
    }
    public void setIntersectionCount(int intersectionCount) {
        this.intersectionCount = intersectionCount;
    }
    
}
