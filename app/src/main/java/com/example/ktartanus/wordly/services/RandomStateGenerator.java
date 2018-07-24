package com.example.ktartanus.wordly.services;

import com.example.ktartanus.wordly.model.Word;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomStateGenerator {
    final int min = 0;
    final int max = 6;
    static final Map<Word.WORD_STATE, Double> STATE_WAGES;
    static  Map<Word.WORD_STATE, Period> STATE_PERIOD;
    static {
        STATE_WAGES = new HashMap<Word.WORD_STATE, Double>();
        STATE_WAGES.put(Word.WORD_STATE.EMPTY, 0.9d);
        STATE_WAGES.put(Word.WORD_STATE.NOT_KNOWN, 0.9d);
        STATE_WAGES.put(Word.WORD_STATE.JUST_KNEW, 0.3);
        STATE_WAGES.put(Word.WORD_STATE.JUST_REMEMBERED, 0.2d);
        STATE_WAGES.put(Word.WORD_STATE.KNOWN, 0.1d);
        STATE_WAGES.put(Word.WORD_STATE.WELL_KNOWN, 0.05d);
        STATE_PERIOD = new HashMap<Word.WORD_STATE, Period>();
        initPeriods();
    }

    private static void initPeriods() {
        double sum = wagesSum();
        double previouNormalizeWage= 0;
        for(Word.WORD_STATE wageKey : STATE_WAGES.keySet()){
            double normalizeWage=STATE_WAGES.get(wageKey)/wagesSum();
            double startPeriod = previouNormalizeWage;
            double endPeriod = previouNormalizeWage + normalizeWage;
            previouNormalizeWage +=normalizeWage;
            STATE_PERIOD.put(wageKey, new Period(startPeriod, endPeriod));
        }
    }

    public static Word.WORD_STATE getRamdomState(){
        Random r = new Random();
        double zeroToOne = r.nextDouble();
        for(Word.WORD_STATE wageKey : STATE_PERIOD.keySet()) {
            if(STATE_PERIOD.get(wageKey).isInPeriod(zeroToOne)){
                return wageKey;
            }
        }
        return Word.WORD_STATE.EMPTY;
    }

    private static double wagesSum(){
        double sum = 0;
        for(Word.WORD_STATE wageKey : STATE_WAGES.keySet()){
                sum+=STATE_WAGES.get(wageKey);
        }
        return sum;
    }
    private static class Period{
        private double start;
        private double end;

        public Period(double start, double end) {
            this.start = start;
            this.end = end;
        }

        public boolean isInPeriod(double point){
            return (point>this.start && point<=this.end);
        }

        public double getStart() {
            return start;
        }

        public void setStart(double start) {
            this.start = start;
        }

        public double getEnd() {
            return end;
        }

        public void setEnd(double end) {
            this.end = end;
        }
    }
//    final int random = Random.nextInt((max - min) + 1) + min;
}
