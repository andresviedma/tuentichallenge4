package com.andresviedma.tuenticontest2014.challenge4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ShapeShifter {
    
    private static enum DnaState { A, G, C, T };
    
    private Set<String> safeStates = new HashSet<String>();

    
    public void addSafeState(String state) {
        this.safeStates.add(state);
    }

    public List<String> changeDna(String dna, String target) {
        List<String> res = new ArrayList<String>(dna.length());
        res = this.addDnaTransitions(dna, target, 0, res,
                new HashMap<String, List<String>>());
        return res;
    }
    
    private List<String> addDnaTransitions(String dna, String target, int from,
                List<String> transitions,
                Map<String, List<String>> checkedStates) {
        
        if (!this.safeStates.contains(dna))  return null;
        
        if (from == 0)  transitions.add(dna);
        if (dna.equals(target))  return transitions;
        if (from >= dna.length())  return null;

        //System.out.println(dna + " : " + from);
        
        DnaState[] states = DnaState.values();
        
        char c0 = dna.charAt(from);
        List<String> bestStepTransitions = null;
        for (int j=0; j<states.length; j++) {
            char c1 = states[j].name().charAt(0);
            
            List<String> stepTransitions = null;
            String check = dna.substring(0, from) + c1 + dna.substring(from + 1);
            if (c1 == c0) {
                stepTransitions = new ArrayList<String>();
                stepTransitions = this.addDnaTransitions(dna, target, from+1, stepTransitions, checkedStates);
                if (stepTransitions != null) {
                    checkedStates.put(dna, stepTransitions);
                }
                
            } else if (checkedStates.containsKey(check)) {
                stepTransitions = checkedStates.get(check);
                
            } else {
                if (this.safeStates.contains(check)) {
                    if (!checkedStates.containsKey(dna)) {
                        checkedStates.put(dna, null);
                    }
                    checkedStates.put(check, null);
                    stepTransitions = new ArrayList<String>();
                    stepTransitions = this.addDnaTransitions(check, target, 0, stepTransitions, checkedStates);
                    checkedStates.put(check, stepTransitions);
                }
            }
            
            if ((stepTransitions != null) && ((bestStepTransitions == null) || stepTransitions.size() < bestStepTransitions.size())) {
                bestStepTransitions = stepTransitions;
            }
        }
        if (bestStepTransitions == null) {
            return null;
        } else {
            transitions.addAll(bestStepTransitions);
            return transitions;
        }
    }

    
    public static void main(String[] args) {
        ShapeShifter sh = new ShapeShifter();
        sh.addSafeState("AGC");
        sh.addSafeState("TTT");
        sh.addSafeState("CGC");
        sh.addSafeState("CGA");
        sh.addSafeState("CAA");
        sh.addSafeState("TGT");
        List<String> res = sh.changeDna("AGC", "CAA");
        System.out.println(res);
        
        sh = new ShapeShifter();
        sh.addSafeState("ATAT");
        sh.addSafeState("GCGT");
        sh.addSafeState("GCAT");
        sh.addSafeState("ACGT");
        sh.addSafeState("GTAT");
//sh.addSafeState("ATGT");
        res = sh.changeDna("ACGT", "ATAT");
        System.out.println(res);
        
    }
}
