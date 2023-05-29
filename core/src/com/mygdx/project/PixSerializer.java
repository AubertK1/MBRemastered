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

    private final int FILEID;

    private String file = "";
    private String pixFile = "";
    private String folder = "temp";
    private String pixFolder = "temp";

    HashMap<Integer, Value> statValues = new HashMap<>();
    private final int NAMEDSTATS = 9;
    private int TOTALSTATS = 9;

    char identifier = 'N'; //identifies which type of outline this saves

    // mark as transient so this is not serialized by default
    transient ByteBuffer pixData;

    public PixSerializer(String folder, String pixFolder) {
//        FILEIDs++;
        FILEID = generateFileID(folder);
        setFolders(folder, pixFolder);

        for (int i = 0; i < NAMEDSTATS; i++) {
            if(i == Stat.DMPOINTS) statValues.put(i, new Value(Value.StoreType.PLIST).setValue(new Point[0]));
            else if(i == Stat.BASEPTR) statValues.put(i, new Value(Value.StoreType.LONG).setValue(0));
            else if(i == Stat.TEXT) statValues.put(i, new Value(Value.StoreType.STRING).setValue(0));
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
            //syncs the folders to make sure the file names match
            Main.getMainScreen().getSelectedScreen().getMBBoard().getBoard().syncFolders(this);

            //creates the directory if needed
            Path path = Paths.get("assets\\SaveFiles\\ovalues\\" + this.folder);
            Files.createDirectories(path);
            //sets up the output streams and potentially the file
            FileOutputStream fileOut =
                    new FileOutputStream(file.equals("") ? file = "assets\\SaveFiles\\ovalues\\" +
                            this.folder + "\\outline" + identifier + FILEID + ".ser" : file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            //writes outline data (position, size, etc.)
            out.writeObject(statValues);
            //closes the output streams
            out.close();
            fileOut.close();

            //saves pixmap data if it's the outline is a drawing
            if(this.pixData != null) {
                //creates the directory if needed
                Path path2 = Paths.get("assets\\SaveFiles\\pixvalues\\" + this.pixFolder);
                Files.createDirectories(path2);
                //sets up the output stream and file channel
                FileOutputStream pixFileOut =
                        new FileOutputStream(pixFile.equals("") ? pixFile = "assets\\SaveFiles\\pixvalues\\" +
                                this.pixFolder + "\\pixmap" + FILEID + ".ser" : pixFile);
                FileChannel fileChannel = pixFileOut.getChannel();
                //writes the buffer data of the pixmap
                fileChannel.write(this.pixData);
                //closes the output stream and file channel
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

    public void setIdentifier(char identifier){
        this.identifier = identifier;
    }
    public char getIdentifier(){
        return identifier;
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
    public void setToFile(@NotNull File file, File pixFile){
        this.file = file.getPath();
        if(pixFile != null)
            this.pixFile = pixFile.getPath();
    }

    public static int generateFileID(String folderName){
        try {
            Path path2 = Paths.get("assets\\SaveFiles\\ovalues\\" + folderName);
            Files.createDirectories(path2);

            File folder = new File(String.valueOf(path2));
            File[] files = folder.listFiles();
            ArrayList<Integer> usedIDs = new ArrayList<>();

            if(files.length == 0) return 1;
            for (File file : files) {
                usedIDs.add(findFileID(file.getName()));
            }
            for (int i = 0; i < 10000; i++) {
                if(!usedIDs.contains(i + 1)) return i + 1;
            }
        } catch (NullPointerException | IOException n){
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
    public static char findFileIdentifier(String fileName){
        int identifierIndex = 7; //the last index of the word "outline" plus one
        char c = fileName.charAt(identifierIndex);

        if(c == 'D' || c == 'S' || c == 'T') return c;
        else return 'N'; //'N' stands for null
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
        public static final int BASEPTR = 7;
        public static final int TEXT = 8;
        //endregion
    }
}