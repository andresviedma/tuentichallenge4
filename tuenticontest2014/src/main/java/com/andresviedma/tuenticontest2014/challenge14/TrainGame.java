package com.andresviedma.tuenticontest2014.challenge14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ListMultimap;


public class TrainGame {
    private final static Logger log = LoggerFactory.getLogger(TrainGame.class);
    
    private int trainsFuel;
    
    private ListMultimap<String, Connection> connections = ArrayListMultimap.create();
    
    private Map<Integer, String> trainsStart = new HashMap<Integer, String>();
    
    private Map<String, Station> stations = new HashMap<String, TrainGame.Station>();
    private int maxScore = 0;
    
    private int nextRouteId = 1;
    
    
    public TrainGame(int trainsFuel) {
        this.trainsFuel = trainsFuel;
    }
    
    public void addStation (Station station) {
        this.stations.put(station.getName(), station);
        this.maxScore += station.getWagonScore();
    }
    
    public void addRoute(String routeStart, List<String> sConnections) {
        
        int routeId = this.nextRouteId;
        this.nextRouteId++;
        
        this.trainsStart.put(routeId, routeStart);
        
        for (int i=0; i<sConnections.size(); i++) {
            String sConn = sConnections.get(i);
            int iSep = sConn.indexOf('-');
            String s1 = sConn.substring(0, iSep).trim();
            String s2 = sConn.substring(iSep+1).trim();
            this.addConnection(routeId, s1, s2);
            this.addConnection(routeId, s2, s1);
        }
    }
    private void addConnection(int train, String station1, String station2) {
        Connection conn = new Connection();
        conn.train = train;
        
        conn.sourceStation = station1;
        conn.targetStation = station2;

        // Calculate fuel
        Station s1 = this.stations.get(conn.sourceStation);
        Station s2 = this.stations.get(conn.targetStation);
        conn.fuelWaste = s1.distanceTo(s2);
        
        this.connections.put(station1, conn);
    }
    
    private List<WagonChoices> getWagonChoices(String wagonStation) {
        WagonChoices ini = new WagonChoices();
        ini.wagonStation = wagonStation;
        ini.path.add(wagonStation);
        
        Station station = stations.get(wagonStation);
        String targetStation = station.getWagonDestination();
        
        List<WagonChoices> res = new ArrayList<TrainGame.WagonChoices>();
        this.addWagonChoices(ini, targetStation, res);
        
        return res;
    }
    
    private void addWagonChoices(WagonChoices state, String targetStation, List<WagonChoices> res) {
        
        // Ok?
        String current = state.getLastStation();
        if (current.equals(targetStation)) {
            res.add(state);
            return;
        }
        
        // Get connections from current station
        List<Connection> stationConns = this.connections.get(current);
        
        for (int i=0; i<stationConns.size(); i++) {
            Connection connTry = stationConns.get(i);

            WagonChoices state2 = state.addConnection(connTry, this.trainsFuel);
            if (state2 != null) {
                this.addWagonChoices(state2, targetStation, res);
            }
        }
    }
    
    public int getMaxScore() {
        
        // Heuristic: Get stations sorted by score
        List<Station> stationsList = new ArrayList<Station>(this.stations.values());
        Collections.sort(stationsList, new Comparator<Station>() {
            public int compare(Station o1, Station o2) {
                return o2.getWagonScore() - o1.getWagonScore();
            }
        });
        
        // Get choices for every wagon to succeed
        List<List<WagonChoices>> choices = new ArrayList<List<WagonChoices>>(this.stations.size());
        Iterator<Station> it = stationsList.iterator();
        log.info("*** Choices per station");
        while (it.hasNext()) {
            Station station = it.next();
            log.info("* Station: " + station.getName());
            List<WagonChoices> stationChoices = this.getWagonChoices(station.getName());
            if (!stationChoices.isEmpty()) {
                choices.add(stationChoices);
                
                if (log.isInfoEnabled()) {
                    for (int i=0; i<stationChoices.size(); i++) {
                        WagonChoices wc = stationChoices.get(i);
                        log.info(wc.toString());
                    }
                }
            }
        }
        
        // Try every permutation, changing the order of the stations (it matters)
        int score = this.permute(choices, new ArrayList<Integer>(choices.size()), 0);
        return score;
    }
    
    private int permute(List<List<WagonChoices>> data, List<Integer> permutation, int bestScore) {
        
        // Ok, try permutation if complete
        int permutationIndex = permutation.size();
        if (permutationIndex >= data.size()) {
            log.debug("Permute: " + permutation);
            
            List<List<WagonChoices>> choicesPerStation = new ArrayList<List<WagonChoices>>(data.size());
            for(int i=0; i<data.size(); i++) {
                int pos = permutation.get(i);
                choicesPerStation.add(data.get(pos));
            }
            return this.tryPermutation(new ArrayList<WagonChoices>(data.size()), choicesPerStation, bestScore);
        }
        
        // Permute
        for (int i=0; i<data.size(); i++) {
            if (!permutation.contains(i)) {
                permutation.add(i);
                int v = this.permute(data, permutation, bestScore);
                permutation.remove(permutationIndex);
                bestScore = Math.max(v, bestScore);
            }
        }
        return bestScore;
    }
    
    
    private int tryPermutation(List<WagonChoices> current, List<List<WagonChoices>> choicesPerStation, int bestScore) {
        int numStations = choicesPerStation.size();
        
        if (current.size() == numStations) {
            int maxScoreTry = 0;
            for (int i=0; i<current.size(); i++) {
                maxScoreTry += this.stations.get(current.get(i).wagonStation).wagonScore;
            }
            if (maxScoreTry <= bestScore) {
                return 0;
            } else {
                return this.tryChoices(current, bestScore);
            }
            
        } else {
            int score = 0;
            
            int posTry = current.size();
            List<WagonChoices> choicesAdd = choicesPerStation.get(current.size());
            for (int i=0; i<choicesAdd.size(); i++) {
                WagonChoices choices = choicesAdd.get(i);
                current.add(choices);
                int v = this.tryPermutation(current, choicesPerStation, bestScore);
                current.remove(posTry);
                score = Math.max(v, score);
            }
            return score;
        }
    }
    
    
    private int tryChoices(List<WagonChoices> choices, int bestScore) {
        
        // Initial state
        GameState ini = new GameState();
        for (int i=1; i<this.nextRouteId; i++) {
            ini.trainsLocation.put(i, this.trainsStart.get(i));
            ini.wastedFuel.put(i, 0.0d);
        }
        
        // Try to do each choice
        GameState state = ini;
        int maxPossibleScore = 0;
        for (int i=0; i<choices.size(); i++) {
            WagonChoices wc = choices.get(i);
            maxPossibleScore += this.stations.get(wc.wagonStation).wagonScore;
            state = this.nextState(state, wc);
            if (maxPossibleScore - state.score >= this.maxScore - bestScore) {
                return bestScore;
            }
        }
        
        log.debug("Score: " + state.score);
        return state.score;
    }
    
    private GameState nextState(GameState state, WagonChoices wc) {
        
        GameState res = (GameState) state.clone();
        
        Map<Integer, Double> fuelWastes = new HashMap<Integer, Double>(wc.fuelWaste);
        
        // Find best possible initial wagon station, add fill first train waste according to that
        Set<String> possibleWagonIntermediates = state.possibleInitStationsWagon.get(wc.wagonStation);
        if ((possibleWagonIntermediates == null) || possibleWagonIntermediates.isEmpty()) {
            possibleWagonIntermediates = Collections.singleton(wc.wagonStation);
        }
        
        int firstTrain = wc.firstTrain;
        
        // Find station closer to first target
        String firstTargetStation = wc.getFirstTargetStation();
        int iTarget = wc.path.indexOf(firstTargetStation);
        int iMax = 0;
        Iterator<String> itTryWagon = possibleWagonIntermediates.iterator();
        while (itTryWagon.hasNext()) {
            String tryWagonStation = itTryWagon.next();
            int iTry = wc.path.indexOf(tryWagonStation);
            if (iTry <= iTarget)
                iMax = Math.max(iMax, iTry);
        }
        
        // Get path and calculate waste
        List<String> firstTrainPath = wc.path.subList(iMax, iTarget + 1);
        double firstWaste = this.getPathWaste(firstTrain, firstTrainPath);
        wc.requiredIniStations.put(firstTrain, firstTrainPath.get(0));

        // Check waste ok, add to the map
        fuelWastes.put(firstTrain, firstWaste);
        
        // Calculate trains state and waste, see if possible
        Iterator<Entry<Integer, Double>> itWastes = fuelWastes.entrySet().iterator();
        while (itWastes.hasNext()) {
            Entry<Integer, Double> entry = itWastes.next();
            int train = entry.getKey();
            double waste = entry.getValue();
            
            // Add waste to reach the initial location
            String iniStat = wc.requiredIniStations.get(train);
            if (!iniStat.equals(res.trainsLocation.get(train))) {
                String endStat = res.trainsLocation.get(train);
                
                // Calculate waste
                Station s1 = this.stations.get(iniStat);
                Station s0 = this.stations.get(endStat);
                waste += s1.distanceTo(s0);
                
                // Register intermediate positions for a wagon (train can leave them in any station),
                // they can be used later as a start if it reduces fuel waste
                if (waste + res.wastedFuel.get(train) <= this.trainsFuel) {
                    Set<String> stations = this.getStationsPath(train, iniStat, endStat);
                    res.possibleInitStationsWagon.putAll(this.trainsStart.get(train), stations);
                }
            }
            
            // Add to waste until now
            double waste0 = res.wastedFuel.get(train);
            waste += waste0;
            if (waste > this.trainsFuel)  return res;
            res.wastedFuel.put(train, waste);
            
            // Calculate train final location
            String finalStat = wc.finalStations.get(train);
            res.trainsLocation.put(train, finalStat);
        }
        
        // Add score
        String wagonName = wc.wagonStation;
        Station station = this.stations.get(wagonName);
        res.score += station.getWagonScore();
        log.debug(wc.path + ": " + res.score + " - wasted fuel: " + res.wastedFuel);
        
        return res;
    }

    private Set<String> getStationsPath(int train, String iniStat, String endStat) {
        Set<String> path = new HashSet<String>();
        path.add(iniStat);
        this.addStationsPath(train, iniStat, endStat, null, path);
        return path;
    }
    
    private boolean addStationsPath(int train, String iniStat, String endStat, String avoid, Set<String> path) {
        
        if (iniStat.equals(endStat))  return true;
        
        // Find this train connection
        List<Connection> conns = this.connections.get(iniStat);
        for (int i=0; i<conns.size(); i++) {
            if (conns.get(i).train == train) {
                String iniStat2 = conns.get(i).targetStation;
                if (!iniStat2.equals(avoid)) {
                    path.add(iniStat2);
                    boolean bOk = this.addStationsPath(train, iniStat2, endStat, iniStat, path);
                    if (bOk) {
                        return true;
                    } else {
                        path.remove(iniStat2);
                    }
                }
            }
        }
        return false;
    }

    
    private double getPathWaste(int train, List<String> stats) {
        
        if (stats.size() < 2)  return 0.0;
        
        double res = 0.0;
        for (int i=0; i<stats.size()-1; i++) {
            String iniStat = stats.get(i);
            String endStat = stats.get(i+1);
            
            // Find this connection
            boolean bOk = false;
            List<Connection> conns = this.connections.get(iniStat);
            for (int j=0; j<conns.size() && !bOk; j++) {
                Connection conn = conns.get(j);
                if ((conn.train == train) && conn.targetStation.equals(endStat)) {
                    res += conn.fuelWaste;
                    bOk = true;
                }
            }
        }
        
        return res;
    }

    
    private static class Connection {
        public String sourceStation;
        public String targetStation;
        
        public int train;
        public double fuelWaste;
        
    }
    
    public static class Station {
        private String name;
        private int x, y;
        private String wagonDestination;
        private int wagonScore;
        
        
        public Station() {
        }
        
        public Station(String name, int x, int y, String wagonDestination,
                int wagonScore) {
            super();
            this.name = name;
            this.x = x;
            this.y = y;
            this.wagonDestination = wagonDestination;
            this.wagonScore = wagonScore;
        }

        public double distanceTo(Station s) {
            int xDiff = Math.abs(x - s.x);
            int yDiff = Math.abs(y - s.y);
            double res = Math.sqrt(xDiff*xDiff + yDiff*yDiff);
            return res;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getX() {
            return x;
        }
        public void setX(int x) {
            this.x = x;
        }
        public int getY() {
            return y;
        }
        public void setY(int y) {
            this.y = y;
        }
        public String getWagonDestination() {
            return wagonDestination;
        }
        public void setWagonDestination(String wagonDestination) {
            this.wagonDestination = wagonDestination;
        }
        public int getWagonScore() {
            return wagonScore;
        }
        public void setWagonScore(int wagonScore) {
            this.wagonScore = wagonScore;
        }
    }
    

    private static class WagonChoices implements Cloneable {
        public String wagonStation;
        
        public Map<Integer, String> requiredIniStations = new HashMap<Integer, String>();
        public Map<Integer, String> finalStations = new HashMap<Integer, String>();
        
        public int firstTrain = -1;
        public int lastTrain = -1;
        public String firstTargetStation;
        
        public List<String> path = new ArrayList<String>();

        // Train -> fuel waste (positive)
        public Map<Integer, Double> fuelWaste = new HashMap<Integer, Double>();

        
        public String getLastStation() {
            if (path.isEmpty()) {
                return wagonStation;
            } else {
                return path.get(path.size() - 1);
            }
        }
        
        public String getFirstTargetStation() {
            return firstTargetStation;
        }
        
        public Object clone() {
            try {
                WagonChoices o = (WagonChoices) super.clone();
                o.path = new ArrayList<String>(path);
                o.fuelWaste = new HashMap<Integer, Double>(fuelWaste);
                o.requiredIniStations = new HashMap<Integer, String>(requiredIniStations);
                o.finalStations = new HashMap<Integer, String>(finalStations);
                return o;
                
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
        
        public WagonChoices addConnection(Connection connTry, int maxFuel) {
            // Don't do loops
            if (this.path.contains(connTry.targetStation)) {
                return null;
            }
            
            // Don't change to a train previously used
            if (connTry.train != this.lastTrain && this.requiredIniStations.containsKey(connTry.train)) {
                return null;
            }
            
            // Let's try...
            WagonChoices state2 = (WagonChoices) this.clone();
            
            // Enough fuel? don't take into account first train waste
            if (state2.firstTrain != -1 && state2.firstTrain != connTry.train) {
                boolean bOkFuel = state2.addWastedFuel(
                        connTry.train, connTry.fuelWaste, maxFuel);
                if (!bOkFuel)  return null;
            }

            // Fill new data from the connection
            if (!state2.requiredIniStations.containsKey(connTry.train)) {
                state2.requiredIniStations.put(connTry.train, connTry.sourceStation);
            }
            if (state2.firstTrain == -1) {
                state2.firstTrain = connTry.train;
                state2.firstTargetStation = connTry.targetStation;
            } else if (state2.firstTrain == connTry.train) {
                state2.firstTargetStation = connTry.targetStation;
            }
            state2.lastTrain = connTry.train;
            state2.path.add(connTry.targetStation);
            state2.finalStations.put(connTry.train, connTry.targetStation);
            return state2;
        }
        
        
        public boolean addWastedFuel(int train, double waste, int maxFuel) {
            
            double totalWaste = waste;
            Double currentWaste = this.fuelWaste.get(train);
            if (currentWaste != null) {
                totalWaste += currentWaste.doubleValue();
            }
            if (totalWaste > maxFuel) {
                return false;
            } else {
                fuelWaste.put(train, totalWaste);
                return true;
            }
        }

        @Override
        public String toString() {
            return "WagonChoices [wagonStation=" + wagonStation
                    + ", path=" + path 
                    + ", lastTrain=" + lastTrain + ", firstTrain=" + firstTrain
                    + ", requiredIniStations=" + requiredIniStations
                    + ", fuelWaste=" + fuelWaste + "]";
        }
        
    }

    
    private static class GameState implements Cloneable {
        public int score;
        public Map<Integer, Double> wastedFuel = new HashMap<Integer, Double>();
        public Map<Integer, String> trainsLocation = new HashMap<Integer, String>();
        
        public HashMultimap<String, String> possibleInitStationsWagon = HashMultimap.create();
        
        
        public Object clone() {
            try {
                GameState o = (GameState) super.clone();
                o.wastedFuel = new HashMap<Integer, Double>(wastedFuel);
                o.trainsLocation = new HashMap<Integer, String>(trainsLocation);
                o.possibleInitStationsWagon = HashMultimap.create (possibleInitStationsWagon);
                return o;
                
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
}
