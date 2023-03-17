package com.mygdx.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Stats {
    //region key
    public enum Stat {
        STR, DEX, CON, INT, WIS, CHA,
        ACRO, ANIM, ARCA, ATHL, DECE, HIST, INSI, INTI, INVE, MEDI, NATU, PERC, PERF, PERS, RELI, SLEI, STEA, SURV,
        STRst, DEXst, CONst, INTst, WISst, CHAst,
        HP, THP, AC, BAC, SPD, INI,
        LVL, PRF, CLS, RCE
    }
    public static final String[] basestats = new String[]{"Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma"};
    public static final String[] skills = new String[]{"Acrobatics", "Animal Handling", "Arcana", "Athletics", "Deception", "History",
            "Insight", "Intimidation", "Investigation", "Medicine", "Nature", "Perception", "Performance", "Persuasion",
            "Religion", "Sleight of Hand", "Stealth", "Survival"};
    public static final String[] saves = new String[]{"Str Throw", "Dex Throw", "Con Throw", "Int Throw", "Wis Throw", "Cha Throw"};
    public static final String[] combatstats = new String[]{"HP", "Temp HP", "AC", "Bonus AC", "Speed", "Initiative"};
    public static final String[] charstats = new String[]{"Level", "Proficiency", "Class", "Race"};
    //endregion

    HashMap<Stat, Integer> stats = new HashMap<>();

    //region attacks
    ArrayList<Integer> DMGDices = new ArrayList<>();
    ArrayList<Integer> ATKMods = new ArrayList<>();
    //endregion

    public Stats() {
        for (Stat stat: Stat.values()) {
            stats.put(stat, 0);
        }
    }

    public void setStat(Stat stat, int value){
        stats.put(stat, value);
    }

    public void setStat(Stat stat, String value){
        setStat(stat, findNumber(value));
    }

    public int getStat(Stat stat){
        return stats.containsKey(stat) ? stats.get(stat) : 0;
    }

    //region attacks

    //endregion

    static public Stat statIndexToStat(String[] list, int index){
        int i = index;
        if (Arrays.equals(skills, list)) {
            i += basestats.length;
        } else if (Arrays.equals(saves, list)) {
            i += basestats.length + skills.length;
        } else if (Arrays.equals(combatstats, list)) {
            i += basestats.length + skills.length + saves.length;
        } else if (Arrays.equals(charstats, list)) {
            i += basestats.length + skills.length + saves.length + combatstats.length;
        }

        Stat[] stats1 = Stat.values();
        return stats1[i];
    }
    static public String statToString(Stat stat){
        Stat[] stats1 = Stat.values();
        int index = 0;
        for (int i = 0; i < stats1.length; i++) {
            if(stat == stats1[i]){
                index = i;
                break;
            }
        }

        String[][] arrayOfStatArrays = new String[][]{basestats, skills, saves, combatstats, charstats};
        int totalJ = 0;
        for (int i = 0; i < arrayOfStatArrays.length; i++) {
            for (int j = 0; j < arrayOfStatArrays[i].length; j++) {
                if(j == 0) {
                    totalJ += arrayOfStatArrays[i].length;
                    if (index > totalJ) break;
                    else totalJ -= arrayOfStatArrays[i].length;
                }

                if(index == totalJ) return arrayOfStatArrays[i][j];
                totalJ++;
            }
        }
        return "Not Found";
    }

    static public int findNumber(String text){
        try{
            return Integer.parseInt(text);
        } catch (NumberFormatException e){
            return 0;
        }
    }
}
