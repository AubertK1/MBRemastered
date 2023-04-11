package com.mygdx.project;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Stats {
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

    String file = "";

    HashMap<Integer, Value> statValues = new HashMap<>();

    private int TOTALSTATS = 42;
    public Stats() {
        FILEIDs++;

        for (int i = 0; i < TOTALSTATS; i++) {
            if(i <= Stat.RCE){
                statValues.put(i, new Value(Value.StoreType.INT).setValue(0));
            }
            else statValues.put(i, new Value(Value.StoreType.STRING).setValue(""));
        }
    }

    //region setting values
    public void setStat(int stat, Value v){
        statValues.put(stat, v);
    }
    public void setStat(int stat, String v){
        statValues.put(stat, statValues.get(stat).setValue(v));
    }
    public void setStat(int stat, int v){
        statValues.put(stat, statValues.get(stat).setValue(v));
    }

    public Object getValue(int stat){
        return statValues.containsKey(stat) ? statValues.get(stat).getValue() : 0;
    }

    public int newStat(Value v){
        int index = TOTALSTATS;
        statValues.put(index, v);
        TOTALSTATS++;

        return index;
    }
    //endregion

    static public int statIndexToStat(String[] list, int index){
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

        return i;
    }
    static public String statToString(int stat){
        String[][] arrayOfStatArrays = new String[][]{basestats, skills, saves, combatstats, charstats, textMisc};
        int totalJ = 0;
        for (int i = 0; i < arrayOfStatArrays.length; i++) {
            for (int j = 0; j < arrayOfStatArrays[i].length; j++) {
                if(j == 0) {
                    totalJ += arrayOfStatArrays[i].length;
                    if (stat > totalJ) break;
                    else totalJ -= arrayOfStatArrays[i].length;
                }

                if(stat == totalJ) return arrayOfStatArrays[i][j];
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
                    new FileOutputStream(file.equals("") ? file = "assets\\SaveFiles\\player" + FILEID + ".ser" : file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(statValues);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in " + file);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void load(){
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            statValues = (HashMap<Integer, Value>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Loaded data from " + file);
        } catch (FileNotFoundException f){
            f.printStackTrace();
            save();
            load();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
        }
    }

    public void setToFile(@NotNull File file){
        this.file = file.getPath();
        load();
    }

    public void dispose(){
        File f = new File(file);
        if(f.delete()) {
            System.out.println("Deleted " + file);
        } else System.out.println("Failed to delete " + file);
    }

    public static class Stat{
        //region stats key
        public static final int STR = 0;
        public static final int DEX = 1;
        public static final int CON = 2;
        public static final int INT = 3;
        public static final int WIS = 4;
        public static final int CHA = 5;
        public static final int ACRO = 6;
        public static final int ANIM = 7;
        public static final int ARCA = 8;
        public static final int ATHL = 9;
        public static final int DECE = 10;
        public static final int HIST = 11;
        public static final int INSI = 12;
        public static final int INTI = 13;
        public static final int INVE = 14;
        public static final int MEDI = 15;
        public static final int NATU = 16;
        public static final int PERC = 17;
        public static final int PERF = 18;
        public static final int PERS = 19;
        public static final int RELI = 20;
        public static final int SLEI = 21;
        public static final int STEA = 22;
        public static final int SURV = 23;
        public static final int STRst = 24;
        public static final int DEXst = 25;
        public static final int CONst = 26;
        public static final int INTst = 27;
        public static final int WISst = 28;
        public static final int CHAst = 29;
        public static final int HP = 30;
        public static final int THP = 31;
        public static final int AC = 32;
        public static final int BAC = 33;
        public static final int SPD = 34;
        public static final int INI = 35;
        public static final int LVL = 36;
        public static final int PRF = 37;
        public static final int CLS = 38;
        public static final int RCE = 39;
        public static final int NAME = 40;
        public static final int REM = 41;
        public static final int IMGFILEPATH = 41;
        //endregion
    }
}
