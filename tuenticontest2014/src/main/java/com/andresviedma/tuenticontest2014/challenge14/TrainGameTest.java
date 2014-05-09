package com.andresviedma.tuenticontest2014.challenge14;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andresviedma.tuenticontest2014.challenge14.TrainGame.Station;

public class TrainGameTest {
    private final static Logger log = LoggerFactory.getLogger(TrainGame.class);
    
    
//    public static void main(String[] args) {
//        testByAndrew();
//    }
//
//    public static void testByAndrew() {
//        long ms0 = System.currentTimeMillis();
//        
//        TrainGame g = new TrainGame(4);
//        g.addStation(new Station("A", 1,0, "E", 1));
//        g.addStation(new Station("B", 0,1, "D", 50));
//        g.addStation(new Station("C", 1,1, "B", 1));
//        g.addStation(new Station("D", 2,1, "A", 1));
//        g.addStation(new Station("E", 1,2, "A", 1));
//        g.addStation(new Station("F", 2,0, "D", 1));
//        g.addStation(new Station("G", 3,0, "D", 1));
//        g.addStation(new Station("H", 4,0, "D", 10));
//        
//        g.addRoute("H", Arrays.asList(new String[] {
//                "H-G", "G-F", "F-A", "A-C", "C-D"
//        }));
//        g.addRoute("B", Arrays.asList(new String[] {
//                "C-D", "B-C", "C-E"
//        }));
//
//        
////        GameState ini = new GameState();
////        for (int i=1; i<g.nextRouteId; i++) {
////            ini.trainsLocation.put(i, g.trainsStart.get(i));
////            ini.wastedFuel.put(i, 0.0d);
////        }
////        Iterator<Station> it = g.stations.values().iterator();
////        while (it.hasNext()) {
////            Station station = it.next();
////            ini.wagonsLocation.put(station.name, station.name);
////        }
////        
////        WagonChoices wc0 = new WagonChoices();
////        wc0.finalStations.put(2, "D");
////        wc0.firstTargetStation = "D";
////        wc0.firstTrain = 2;
////        wc0.lastTrain = 2;
////        wc0.path.addAll(Arrays.asList(new String[] {"B", "C", "D"}));
////        wc0.requiredIniStations.put(2,  "B");
////        wc0.wagonStation = "B";
////        GameState state = g.nextState(ini, wc0);
////        
////        WagonChoices wc = new WagonChoices();
////        wc.finalStations.put(1, "C");
////        wc.finalStations.put(2, "D");
////        wc.firstTargetStation = "C";
////        wc.firstTrain = 1;
////        wc.lastTrain = 2;
////        wc.path.addAll(Arrays.asList(new String[] {"H", "G", "F", "A", "C", "D"}));
////        wc.requiredIniStations.put(1,  "H");
////        wc.requiredIniStations.put(2,  "C");
////        wc.wagonStation = "H";
////        
////        state = g.nextState(state, wc);
//        
//        Iterator<Station> it = g.stations.values().iterator();
//        while (it.hasNext()) {
//            Station s = it.next();
//            String station = s.getName();
//            log.info("* Station: " + station);
//            List<WagonChoices> choices = g.getWagonChoices(station);
//            for (int i=0; i<choices.size(); i++) {
//                WagonChoices wc = choices.get(i);
//                log.info(wc.toString());
//            }
//        }
//        
//        int score = g.getMaxScore();
//        log.info("** Total Score: " + score);
//        
//        long ms1 = System.currentTimeMillis();
//        log.info("OK!: " + ((ms1 - ms0) / 60000f) + " minutos");
//    }
//    
//    public static void testSimple() {
//        TrainGame g = new TrainGame(3);
//        g.addStation(new Station("A", 0,0, "E", 5));
//        g.addStation(new Station("B", 0,1, "C", 10));
//        g.addStation(new Station("C", 0,2, "B", 5));
//        g.addStation(new Station("D", 1,1, "C", 10));
//        g.addStation(new Station("E", 1,2, "B", 2));
//        
//        g.addRoute("A", Arrays.asList(new String[] {
//                "A-B", "B-E"
//        }));
//        g.addRoute("D", Arrays.asList(new String[] {
//                "B-D", "B-C"
//        }));
//        
////        log.info("stations path: " + g.getStationsPath(1, "A", "E"));
////        log.info("path waste: " + g.getPathWaste(1, Arrays.asList(new String[] {"A","B","E"})));
////if(true)  return;        
//        
//        Iterator<Station> it = g.stations.values().iterator();
//        while (it.hasNext()) {
//            Station s = it.next();
//            String station = s.getName();
//            log.info("* Station: " + station);
//            List<WagonChoices> choices = g.getWagonChoices(station);
//            for (int i=0; i<choices.size(); i++) {
//                WagonChoices wc = choices.get(i);
//                log.info(wc.toString());
//            }
//        }
//        
//        int score = g.getMaxScore();
//        log.info("** Total Score: " + score);
//        
//        log.info("OK!");
//    }
//
//    public static void testCase3() {
//        TrainGame g = new TrainGame(5);
//        g.addStation(new Station("A", 0,0, "B", 3));
//        g.addStation(new Station("B", 0,1, "E", 8));
//        g.addStation(new Station("C", 0,2, "D", 18));
//        g.addStation(new Station("D", 1,1, "C", 20));
//        g.addStation(new Station("E", 1,2, "B", 4));
//        
//        g.addRoute("A", Arrays.asList(new String[] {
//                "A-B", "B-E"
//        }));
//        g.addRoute("D", Arrays.asList(new String[] {
//                "B-D", "B-C"
//        }));
//        
////        log.info("stations path: " + g.getStationsPath(1, "A", "E"));
////        log.info("path waste: " + g.getPathWaste(1, Arrays.asList(new String[] {"A","B","E"})));
////if(true)  return;        
//        
//        Iterator<Station> it = g.stations.values().iterator();
//        while (it.hasNext()) {
//            Station s = it.next();
//            String station = s.getName();
//            log.info("* Station: " + station);
//            List<WagonChoices> choices = g.getWagonChoices(station);
//            for (int i=0; i<choices.size(); i++) {
//                WagonChoices wc = choices.get(i);
//                log.info(wc.toString());
//            }
//        }
//        
//        int score = g.getMaxScore();
//        log.info("** Total Score: " + score);
//        
//        log.info("OK!");
//    }
//
}
