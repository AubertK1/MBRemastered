package com.mygdx.project;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Stats {
    public enum Stat {
        STR, DEX, CON, INT, WIS, CHA,
        ACRO, ANIM, ARCA, ATHL, DECE, HIST, INSI, INTI, INVE, MEDI, NATU, PERC, PERF, PERS, RELI, SLEI, STEA, SURV,
        STRst, DEXst, CONst, INTst, WISst, CHAst,
        HP, THP, AC, BAC, SPD, INI,
        LVL, PRF, CLS, RCE,

        NAME, REM
    }
    public static final String[] stats = {
            "STR", "DEX", "CON", "INT", "WIS", "CHA", //6
            "ACRO", "ANIM", "ARCA", "ATHL", "DECE", "HIST", "INSI", "INTI", "INVE", //15
            "MEDI", "NATU", "PERC", "PERF", "PERS", "RELI", "SLEI", "STEA", "SURV", //24
            "STRst", "DEXst", "CONst", "INTst", "WISst", "CHAst", //30
            "HP", "THP", "AC", "BAC", "SPD", "INI", //36
            "LVL", "PRF", "CLS", "RCE", //40

            "NAME", "REM" //42
    };
    public static final List<String> allstats = Arrays.asList(stats);
    //region lists
    public static final String[] basestats = new String[]{"Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma"};
    public static final String[] skills = new String[]{"Acrobatics", "Animal Handling", "Arcana", "Athletics", "Deception", "History",
            "Insight", "Intimidation", "Investigation", "Medicine", "Nature", "Perception", "Performance", "Persuasion",
            "Religion", "Sleight of Hand", "Stealth", "Survival"};
    public static final String[] saves = new String[]{"Str Throw", "Dex Throw", "Con Throw", "Int Throw", "Wis Throw", "Cha Throw"};
    public static final String[] combatstats = new String[]{"HP", "Temp HP", "AC", "Bonus AC", "Speed", "Initiative"};
    public static final String[] charstats = new String[]{"Level", "Proficiency", "Class", "Race"};
    public static final String[] textMisc = new String[]{"Reminders"};
    //endregion

    private static int FILEIDs = 0;
    private final int FILEID = FILEIDs;
    HashMap<Stat, Value> statValues = new HashMap<>();

    public Stats() {
        FILEIDs++;

        boolean intsDone = false;
        for (Stat stat: Stat.values()) {
            if(!intsDone){
                statValues.put(stat, new Value(Value.StoreType.INT).setValue(0));
                if(stat == Stat.RCE) intsDone = true;
            }
            else statValues.put(stat, new Value(Value.StoreType.STRING).setValue(""));
        }
    }

    //region setting values
    public void setStat(Stat stat, Value v){
        statValues.put(stat, v);
    }
    public void setStat(Stat stat, String v){
        statValues.put(stat, statValues.get(stat).setValue(v));
    }
    public void setStat(Stat stat, int v){
        statValues.put(stat, statValues.get(stat).setValue(v));
    }

    public Object getValue(Stat stat){
        return statValues.containsKey(stat) ? statValues.get(stat).getValue() : 0;
    }
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

        String[][] arrayOfStatArrays = new String[][]{basestats, skills, saves, combatstats, charstats, textMisc};
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

    public void save(){
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("assets\\\\SaveFiles\\\\saves" + FILEID + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(statValues);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in assets\\\\SaveFiles\\\\saves" + FILEID + ".ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void load(){
        try {
            FileInputStream fileIn = new FileInputStream("assets\\SaveFiles\\saves" + FILEID + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            statValues = (HashMap<Stat, Value>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Loaded data from assets\\SaveFiles\\saves" + FILEID + ".ser");
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
        }
    }
}
