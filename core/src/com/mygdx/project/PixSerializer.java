package com.mygdx.project;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class PixSerializer implements java.io.Serializable {
    private static final long serialVersionUID = -2831273345165209113L;

    private static int FILEIDs = 0;
    private final int FILEID = FILEIDs;
    private String file = "";
    private String pixFile = "";

    HashMap<Integer, Value> statValues = new HashMap<>();
    private final int NAMEDSTATS = 43;
    private int TOTALSTATS = 43;

    // mark as transient so this is not serialized by default
    transient ByteBuffer data;

    public PixSerializer() {
        FILEIDs++;

        for (int i = 0; i < NAMEDSTATS; i++) {
            if(i == Stat.DMPOINTS) statValues.put(i, new Value(Value.StoreType.PLIST).setValue(new Point[0]));
            statValues.put(i, new Value(Value.StoreType.INT).setValue(0));
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

    public Integer newStat(Value v){
        int index = NAMEDSTATS;
        while (statValues.containsKey(index)){
            index++;
        }
        TOTALSTATS++;
        statValues.put(index, v);

        return index;
    }
    //endregion

    public void setData(ByteBuffer data) {
        this.data = data;
    }
    public ByteBuffer getData() {
        return this.data;
    }

    public void save() {
        try{
            FileOutputStream fileOut =
                    new FileOutputStream(file.equals("") ? file = "assets\\ovalues\\outline" + FILEID + ".ser" : file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            // write default properties
            out.writeObject(statValues);

            out.close();
            fileOut.close();
            if(this.data != null) {
                // write buffer capacity and data
                FileOutputStream pixFileOut =
                        new FileOutputStream(pixFile.equals("") ? pixFile = "assets\\pixvalues\\pixmap" + FILEID + ".ser" : pixFile);
                FileChannel fileChannel = pixFileOut.getChannel();

                fileChannel.write(this.data);

                fileChannel.close();
                pixFileOut.close();

                System.out.println("Pixmap data is saved in " + pixFile);
            }

            System.out.println("Outline data is saved in " + file);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void load() {
        try{
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            //read default properties
            statValues = (HashMap<Integer, Value>) in.readObject();

            in.close();
            fileIn.close();
            if(this.data != null) {
                //read buffer data and wrap with ByteBuffer
//                FileInputStream pixFileIn = new FileInputStream(pixFile);
//                ObjectInputStream pixIn = new ObjectInputStream(pixFileIn);

/*
                int bufferSize = pixIn.readInt();
                byte[] buffer = new byte[bufferSize];
                pixIn.read(buffer, 0, bufferSize);
                this.data = ByteBuffer.wrap(buffer, 0, bufferSize).duplicate();
                this.data = ByteBuffer.allocateDirect(1024*10);
*/

                Path path = Paths.get(pixFile);

                FileChannel fileChannel =  FileChannel.open(path);
                ByteBuffer buffer = ByteBuffer.allocateDirect(1024*10);
                int noOfBytesRead = fileChannel.read(buffer);

                while (noOfBytesRead != -1) {
//                    System.out.println("Number of bytes read: " + noOfBytesRead);
                    buffer.flip();
//                    System.out.print("Buffer contents: ");

                    while (buffer.hasRemaining()) {
                        buffer.get();
                    }

//                    System.out.println(" ");
                    buffer.clear();
                    noOfBytesRead = fileChannel.read(buffer);
                }
                this.data = buffer;
                fileChannel.close();

//                pixIn.close();
//                pixFileIn.close();
                System.out.println("Loaded data from " + pixFile);
            }
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

    public void setToFile(@NotNull File file, @NotNull File pixFile){
        this.file = file.getPath();
        this.pixFile = pixFile.getPath();
        load();
    }

    public static class Stat{
        //region stats key
        public static final int XPOS = 0;
        public static final int YPOS = 1;
        public static final int WIDTH = 2;
        public static final int HEIGHT = 3;
        public static final int XOFFSET = 4;
        public static final int YOFFSET = 5;
        public static final int DMPOINTS = 6;
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
        public static final int IMGFILEPATH = 42;
        //endregion
    }
}