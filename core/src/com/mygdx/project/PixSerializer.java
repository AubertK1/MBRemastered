package com.mygdx.project;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class PixSerializer implements java.io.Serializable {
    private static final long serialVersionUID = -2831273345165209113L;

    private static int FILEIDs = 0;
    private final int FILEID = FILEIDs;
    private String file = "";
    private String pixFile = "";
    private String folder = "temp";
    private String pixFolder = "temp";

    HashMap<Integer, Value> statValues = new HashMap<>();
    private final int NAMEDSTATS = 9;
    private int TOTALSTATS = 9;

    // mark as transient so this is not serialized by default
    transient ByteBuffer data;

    public PixSerializer() {
        FILEIDs++;

        for (int i = 0; i < NAMEDSTATS; i++) {
            if(i == Stat.DMPOINTS) statValues.put(i, new Value(Value.StoreType.PLIST).setValue(new Point[0]));
            else if(i == Stat.BASEPTR) statValues.put(i, new Value(Value.StoreType.LONG).setValue(0));
            else statValues.put(i, new Value(Value.StoreType.INT).setValue(0));
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
            Main.getMainScreen().getSelectedScreen().getBoard().getBoard().syncFolders();
            Path path = Paths.get("assets\\SaveFiles\\ovalues\\" + this.folder);
            Files.createDirectories(path);
            FileOutputStream fileOut =
                    new FileOutputStream(file.equals("") ? file = "assets\\SaveFiles\\ovalues\\" + this.folder + "\\outline" + FILEID + ".ser" : file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            // write default properties
            out.writeObject(statValues);

            out.close();
            fileOut.close();
            if(this.data != null) {


                // write buffer capacity and data
                Path path2 = Paths.get("assets\\SaveFiles\\pixvalues\\" + this.pixFolder);
                Files.createDirectories(path2);
                FileOutputStream pixFileOut =
                        new FileOutputStream(pixFile.equals("") ? pixFile = "assets\\SaveFiles\\pixvalues\\" + this.pixFolder + "\\pixmap" + FILEID + ".ser" : pixFile);
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
                Path path = Paths.get(pixFile);

                FileChannel fileChannel =  FileChannel.open(path);
                ByteBuffer buffer = ByteBuffer.allocateDirect(3461200);
                int noOfBytesRead = fileChannel.read(buffer);

                while (noOfBytesRead != -1) {
                    buffer.flip();

                    while (buffer.hasRemaining()) {
                        buffer.get();
                    }

                    buffer.clear();
                    noOfBytesRead = fileChannel.read(buffer);
                }
                this.data = buffer;
                fileChannel.close();

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
    public void setFolders(@NotNull String file, @NotNull String pixFile) {
        this.folder = file;
        this.pixFolder = pixFile;
    }
    public String getFolder(){
        return folder;
    }
    public String getPixFolder(){
        return pixFolder;
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
        public static final int BASEPTR = 8;
        //endregion
    }
}