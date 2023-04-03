package com.mygdx.project;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class Values {
    public enum Stat {
        STR, DEX, CON, INT, WIS, CHA,
        ACRO, ANIM, ARCA, ATHL, DECE, HIST, INSI, INTI, INVE, MEDI, NATU, PERC, PERF, PERS, RELI, SLEI, STEA, SURV,
        STRst, DEXst, CONst, INTst, WISst, CHAst,
        HP, THP, AC, BAC, SPD, INI,
        LVL, PRF, CLS, RCE,

        REM
    }
    //region lists
    public static final String[] basestats = new String[]{"Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma"};
    public static final String[] skills = new String[]{"Acrobatics", "Animal Handling", "Arcana", "Athletics", "Deception", "History",
            "Insight", "Intimidation", "Investigation", "Medicine", "Nature", "Perception", "Performance", "Persuasion",
            "Religion", "Sleight of Hand", "Stealth", "Survival"};
    public static final String[] saves = new String[]{"Str Throw", "Dex Throw", "Con Throw", "Int Throw", "Wis Throw", "Cha Throw"};
    public static final String[] combatstats = new String[]{"HP", "Temp HP", "AC", "Bonus AC", "Speed", "Initiative"};
    public static final String[] charstats = new String[]{"Level", "Proficiency", "Class", "Race"};
    //endregion

    HashMap<Stat, Value> stats = new HashMap<>();

    public Values() {
        boolean intsDone = false;
        for (Stat stat: Stat.values()) {
            if(!intsDone){
                stats.put(stat, new Value(Value.StoreType.INT).setValue(0));
                if(stat == Stat.RCE) intsDone = true;
            }
            else stats.put(stat, new Value(Value.StoreType.STRING).setValue(""));
        }
    }

    //region setting values
    public void setStat(Stat stat, Value v){
        stats.put(stat, v);
    }
    public void setStat(Stat stat, String v){
        stats.put(stat, stats.get(stat).setValue(v));
    }
    public void setStat(Stat stat, int v){
        stats.put(stat, stats.get(stat).setValue(v));
    }

    public Object getValue(Stat stat){
        return stats.containsKey(stat) ? stats.get(stat).getValue() : 0;
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

    public void save(){
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("assets\\SaveFile\\saves1.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(stats);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in assets\\SaveFile\\saves1.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void load(){
        try {
            FileInputStream fileIn = new FileInputStream("assets\\SaveFile\\saves1.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            stats = (HashMap<Stat, Value>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Loaded data from assets\\SaveFile\\saves1.ser");
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
        }
    }
}
