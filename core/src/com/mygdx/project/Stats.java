package com.mygdx.project;

import java.util.ArrayList;

public class Stats {
    //region key
    public enum Stat{
        STR, DEX, CON, INT, WIS, CHA,
        ACRO, ANIM, ARCA, ATHL, DECE, HIST, INSI, INTI, INVE, MEDI, NATU, PERC, PERF, PERS, RELI, SLEI, STEA, SURV,
        STRst, DEXst, CONst, INTst, WISst, CHAst,

        STATS, SKILLS, SAVES;

        enum stats{
            STR, DEX, CON, INT, WIS, CHA
        }
        enum skills{
            ACRO, ANIM, ARCA, ATHL, DECE, HIST, INSI, INTI, INVE, MEDI, NATU, PERC, PERF, PERS, RELI, SLEI, STEA, SURV
        }
        enum saves{
            STRst, DEXst, CONst, INTst, WISst, CHAst
        }
    }
    public enum temp{;
        enum s{d, f}
    }
    //endregion

    //region stats
    int STR = 0;
    int DEX = 0;
    int CON = 0;
    int INT = 0;
    int WIS = 0;
    int CHA = 0;
    //endregion
    //region skills
    int ACRO = 0;
    int ANIM = 0;
    int ARCA = 0;
    int ATHL = 0;
    int DECE = 0;
    int HIST = 0;
    int INSI = 0;
    int INTI = 0;
    int INVE = 0;
    int MEDI = 0;
    int NATU = 0;
    int PERC = 0;
    int PERF = 0;
    int PERS = 0;
    int RELI = 0;
    int SLEI = 0;
    int STEA = 0;
    int SURV = 0;
    //endregion
    //region saves
    int STRst = 0;
    int DEXst = 0;
    int CONst = 0;
    int INTst = 0;
    int WISst = 0;
    int CHAst = 0;
    //endregion
    //region attacks
    ArrayList<Integer> DMGDices = new ArrayList<>();
    ArrayList<Integer> ATKMods = new ArrayList<>();
    //endregion

    public Stats() {
    }

    public void setStat(Stat statGroup, int statIndex, int value){
        switch (statGroup){
            case STATS:
                Stat.stats[] s = Stat.stats.values();
                setStat(s[statIndex], value);
                break;
        }
    }

    public void setStat(Stat stat, int value){
        switch (stat){
            //region stats
            case STR:
                STR = value;
                break;
            case DEX:
                DEX = value;
                break;
            case CON:
                CON = value;
                break;
            case INT:
                INT = value;
                break;
            case WIS:
                WIS = value;
                break;
            case CHA:
                CHA = value;
                break;
            //endregion

            //region skills
            case ACRO:
                ACRO = value;
                break;
            case ANIM:
                ANIM = value;
                break;
            case ARCA:
                ARCA = value;
                break;
            case ATHL:
                ATHL = value;
                break;
            case DECE:
                DECE = value;
                break;
            case HIST:
                HIST = value;
                break;
            case INSI:
                INSI = value;
                break;
            case INTI:
                INTI = value;
                break;
            case INVE:
                INVE = value;
                break;
            case MEDI:
                MEDI = value;
                break;
            case NATU:
                NATU = value;
                break;
            case PERC:
                PERC = value;
                break;
            case PERF:
                PERF = value;
                break;
            case PERS:
                PERS = value;
                break;
            case RELI:
                RELI = value;
                break;
            case SLEI:
                SLEI = value;
                break;
            case STEA:
                STEA = value;
                break;
            case SURV:
                SURV = value;
                break;
            //endregion

            //region saves
            case STRst:
                STRst = value;
                break;
            case DEXst:
                DEXst = value;
                break;
            case CONst:
                CONst = value;
                break;
            case INTst:
                INTst = value;
                break;
            case WISst:
                WISst = value;
                break;
            case CHAst:
                CHAst = value;
                break;
            //endregion
        }
    }

    public void setStat(Stat stat, String value){
        setStat(stat, findNumber(value));
    }

    public int getStat(Stat stat){
        return 0;
    }

    //region stats
    //region set int
    public void setSTR(int STR) {
        this.STR = STR;
    }
    public void setDEX(int DEX) {
        this.DEX = DEX;
    }
    public void setCON(int CON) {
        this.CON = CON;
    }
    public void setINT(int INT) {
        this.INT = INT;
    }
    public void setWIS(int WIS) {
        this.WIS = WIS;
    }
    public void setCHA(int CHA) {
        this.CHA = CHA;
    }
    //endregion
    //region set string
    public void setSTR(String STR) {
        this.STR = findNumber(STR);
    }
    public void setDEX(String DEX) {
        this.DEX = findNumber(DEX);
    }
    public void setCON(String CON) {
        this.CON = findNumber(CON);
    }
    public void setINT(String INT) {
        this.INT = findNumber(INT);
    }
    public void setWIS(String WIS) {
        this.WIS = findNumber(WIS);
    }
    public void setCHA(String CHA) {
        this.CHA = findNumber(CHA);
    }
    //endregion
    //region get
    public int getSTR() {
        return STR;
    }
    public int getDEX() {
        return DEX;
    }
    public int getCON() {
        return CON;
    }
    public int getINT() {
        return INT;
    }
    public int getWIS() {
        return WIS;
    }
    public int getCHA() {
        return CHA;
    }
    //endregion
    //endregion

    //region skills
    public void setACRO(int ACRO) {
        this.ACRO = ACRO;
    }
    public void setANIM(int ANIM) {
        this.ANIM = ANIM;
    }
    public void setARCA(int ARCA) {
        this.ARCA = ARCA;
    }
    public void setATHL(int ATHL) {
        this.ATHL = ATHL;
    }
    public void setDECE(int DECE) {
        this.DECE = DECE;
    }
    public void setHIST(int HIST) {
        this.HIST = HIST;
    }
    public void setINSI(int INSI) {
        this.INSI = INSI;
    }
    public void setINTI(int INTI) {
        this.INTI = INTI;
    }
    public void setINVE(int INVE) {
        this.INVE = INVE;
    }
    public void setMEDI(int MEDI) {
        this.MEDI = MEDI;
    }
    public void setNATU(int NATU) {
        this.NATU = NATU;
    }
    public void setPERC(int PERC) {
        this.PERC = PERC;
    }
    public void setPERF(int PERF) {
        this.PERF = PERF;
    }
    public void setPERS(int PERS) {
        this.PERS = PERS;
    }
    public void setRELI(int RELI) {
        this.RELI = RELI;
    }
    public void setSLEI(int SLEI) {
        this.SLEI = SLEI;
    }
    public void setSTEA(int STEA) {
        this.STEA = STEA;
    }
    public void setSURV(int SURV) {
        this.SURV = SURV;
    }
    //endregion

    //region saves
    //region set int
    public void setSTRst(int STRst) {
        this.STRst = STRst;
    }
    public void setDEXst(int DEXst) {
        this.DEXst = DEXst;
    }
    public void setCONst(int CONst) {
        this.CONst = CONst;
    }
    public void setINTst(int INTst) {
        this.INTst = INTst;
    }
    public void setWISst(int WISst) {
        this.WISst = WISst;
    }
    public void setCHAst(int CHAst) {
        this.CHAst = CHAst;
    }
    //endregion
    //region set string
    public void setSTRst(String STRst) {
        this.STR = findNumber(STRst);
    }
    public void setDEXst(String DEXst) {
        this.DEX = findNumber(DEXst);
    }
    public void setCONst(String CONst) {
        this.CON = findNumber(CONst);
    }
    public void setINTst(String INTst) {
        this.INT = findNumber(INTst);
    }
    public void setWISst(String WISst) {
        this.WIS = findNumber(WISst);
    }
    public void setCHAst(String CHAst) {
        this.CHA = findNumber(CHAst);
    }
    //endregion
    //endregion

    //region attacks

    //endregion

    static public int findNumber(String text){
        try{
            return Integer.parseInt(text);
        } catch (NumberFormatException e){
            return 0;
        }
    }
}
