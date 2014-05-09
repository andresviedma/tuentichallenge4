package com.andresviedma.tuenticontest2014.challenge16;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andresviedma.tuenticontest2014.challenge16.PointObservations.Point;
import com.google.common.base.Stopwatch;

public class PointObservationsTest {
    private final static Logger log = LoggerFactory.getLogger(PointObservationsTest.class);
    
    public static void main(String[] args) throws Exception {
        testBigData();
    }
    
    public static void testExampleData() throws IOException {
        PointObservations obs = new PointObservations();
        obs.calculateCollisionDataCount(6, 4);
    }
    
    public static void testExample() {
        int[][] ps = new int[][] {
//                   {54791        ,  92148    ,   43},
//                   {35138        ,  75417    ,   94},
//                   {87668        ,  20721    ,  454},
//                   {64455        ,  33358    ,  291},
//                   {40423        ,  35057    ,   15},
                   { 2467        ,  41977    ,  784},
                   {87438        ,  28193    ,  198},
                   {20680        ,  76562    ,  278},
                   {20930        ,  75950    ,  428},
//                   {56698        ,  14029    ,  492},
//                   {58959        ,   3668    ,  270},
//                   {60306        ,  70806    ,  268}
        };
        
        List<Point> points = new ArrayList<Point>(ps.length);
        for (int i=0; i<ps.length; i++) {
            int[] p = ps[i];
            points.add (new Point(p[0], p[1], p[2]));
        }
        
        PointObservations obs = new PointObservations();
        long res = obs.countCollisions(points);
        System.out.println(res);
    }
    
    public static void testData() throws IOException {
        PointObservations obs = new PointObservations();
        obs.calculateCollisionDataCount(1777533, 20000);
    }
    
    public static void testBigData() throws IOException {
        Stopwatch chrono = Stopwatch.createStarted();
        PointObservations obs = new PointObservations();
        obs.calculateCollisionDataCount(1, 2000000);
        //3000000 max
        log.info("Time: " + chrono.toString());
    }
    
}
