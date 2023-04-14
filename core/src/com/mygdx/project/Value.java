package com.mygdx.project;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

/**
 * A Value is either a String or int depending on the StoreType. That's all it is. Just the ghetto version of python's var
 */
public class Value implements java.io.Serializable{
    private static final long serialVersionUID = -2248361846026583602L;
    StoreType s;
    String valStr = "";
    int valInt = 0;
    Point[] valPoints = new Point[0];

    public Value(StoreType s){
        this.s = s;
    }

    /**
     * Sets the correct variable's value based on whether the storetype is an int or string
     */
    public Value setValue(String v) {
        if(s == StoreType.INT) valInt = Stats.findNumber(v);
        else if(s == StoreType.STRING) valStr = v;
        else if(s == StoreType.PLIST) System.out.println("Cannot Save String as List");;

        return this;
    }
    /**
     * There are two functions for ease of access if using strings or ints to assign values,
     * but it will assign the value to the variable only based on the storetype and not the called function
     */
    public Value setValue(int v) {
        if(s == StoreType.INT) valInt = v;
        else if(s == StoreType.STRING) valStr = String.valueOf(v);
        else if(s == StoreType.PLIST) System.out.println("Cannot Save String as List");;

        return this;
    }
    public Value setValue(Point... v) {
        if(s == StoreType.INT) System.out.println("Cannot Save List as Int");
        else if(s == StoreType.STRING) System.out.println("Cannot Save List as Strinh");
        else if(s == StoreType.PLIST) valPoints = v;

        return this;
    }

    public Object getValue(){
        if(s == StoreType.INT) return valInt;
        else if(s == StoreType.STRING) return valStr;
        else if(s == StoreType.PLIST) return valPoints;
        else return 0;
    }

    public StoreType getStoreType(){
        return s;
    }

    public boolean equals(@NotNull Value v) {
        if(v.getStoreType() != s) return false;

        return v.getValue() == getValue();
    }

    public enum StoreType{
        INT, STRING, PLIST
    }
}
