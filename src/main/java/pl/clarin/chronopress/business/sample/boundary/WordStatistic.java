/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.business.sample.boundary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WordStatistic {
    
    private Map<Integer, Long> wordCounts = new HashMap<>();

    private double totalAvarage;
    
    public WordStatistic(List<WordYearlyStatisticItem> list) {
        list.forEach(i-> wordCounts.put(i.getYear(), i.getCount()));
        totalAvarage = calculateTotalAvarage();
    }

    private double calculateTotalAvarage(){
        return wordCounts.values().stream().mapToLong(i -> i).sum()/ wordCounts.size();
    }
    
    public long normalizeYear(int year, long count){
        double factor = totalAvarage/wordCounts.get(year);
        return Math.round(count*(factor));
    }
    
    public long normalizeMonth(int year, long count){
        double factor = (totalAvarage/12)/(wordCounts.get(year)/12);
        return Math.round(count*(factor));
    }
}
