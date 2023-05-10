package com.mygdx.project;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class PixSerializer implements java.io.Serializable {
    private static final long serialVersionUID = -2831273345165209113L;

//    private static int FILEIDs = 0;
    private final int FILEID;
    private static final ArrayList<Integer> fileIDPool = new ArrayList<>();
    private String file = "";
    private String pixFile = "";
    private String folder = "temp";
    private String pixFolder = "temp";

    HashMap<Integer, Value> statValues = new HashMap<>();
    private final int NAMEDSTATS = 9;
    private int TOTALSTATS = 9;

    // mark as transient so this is not serialized by default
    transient ByteBuffer pixData;

    public PixSerializer(String folder, String pixFolder) {
//        FILEIDs++;
        FILEID = generateFileID(folder);
        setFolders(folder, pixFolder);

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

    public void setPixData(ByteBuffer pixData) {
        this.pixData = pixData;
    }
    public ByteBuffer getPixData() {
        return this.pixData;
    }

    public void save() {
        try{
            Main.getMainScreen().getSelectedScreen().getBoard().getBoard().syncFolders(this);

            //saving outline data (position, size, etc.)
            Path path = Paths.get("assets\\SaveFiles\\ovalues\\" + this.folder);
            Files.createDirectories(path);
            FileOutputStream fileOut =
                    new FileOutputStream(file.equals("") ? file = "assets\\SaveFiles\\ovalues\\" +
                            this.folder + "\\outline" + FILEID + ".ser" : file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            // write default properties
            out.writeObject(statValues);

            out.close();
            fileOut.close();

            //saving pixmap data if a doodle
            if(this.pixData != null) {
                // write buffer capacity and data
                Path path2 = Paths.get("assets\\SaveFiles\\pixvalues\\" + this.pixFolder);
                Files.createDirectories(path2);
                FileOutputStream pixFileOut =
                        new FileOutputStream(pixFile.equals("") ? pixFile = "assets\\SaveFiles\\pixvalues\\" +
                                this.pixFolder + "\\pixmap" + FILEID + ".ser" : pixFile);
                FileChannel fileChannel = pixFileOut.getChannel();

                fileChannel.write(this.pixData);

                fileChannel.close();
                pixFileOut.close();

//                System.out.println("Pixmap data is saved in " + pixFile);
            }

//            System.out.println("Outline data is saved in " + file);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void load() {
        try{
            //pulling outline data (position, size, etc.)
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            //read default properties
            statValues = (HashMap<Integer, Value>) in.readObject();

            in.close();
            fileIn.close();

            //pulling pixmap data if a doodle
            if(this.pixData != null) {
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
                this.pixData = buffer;
                fileChannel.close();

//                System.out.println("Loaded data from " + pixFile);
            }
//            System.out.println("Loaded data from " + file);
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
    public String getFile(){
        return file;
    }
    public String getPixFile(){
        return pixFile;
    }
    public void setToFile(@NotNull File file, @NotNull File pixFile){
        this.file = file.getPath();
        this.pixFile = pixFile.getPath();
    }

    public static int generateFileID(String folderName){
        try {
            File folder = new File("assets\\SaveFiles\\ovalues\\" + folderName);
            File[] files = folder.listFiles();
            ArrayList<Integer> usedIDs = new ArrayList<>();

            if(files.length == 0) return 1;
            for (File file : files) {
                usedIDs.add(findFileID(file.getName()));
            }
            for (int i = 0; i < 10000; i++) {
                if(!usedIDs.contains(i + 1)) return i + 1;
            }
        } catch (NullPointerException n){
            n.printStackTrace();
        }
        return -1;
    }
    public static int findFileID(String fileName){
        if(fileName.contains(".")){ //getting rid of the .filetype
            fileName = fileName.substring(0, fileName.indexOf("."));
        }
        for (int i = fileName.length() - 1; i >= 0; i--) { //loops through file name from end to start
            try{
                Integer.parseInt(fileName.substring(i)); //tries to turn the substring to an int
            } catch (NumberFormatException n){ //will throw this if the substring hits a letter
                if(i + 1 == fileName.length()) return -1; //if no number at end, return -1
                return Integer.parseInt(fileName.substring(i + 1)); //returns the last successful call
            }
        }
        return -1;
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