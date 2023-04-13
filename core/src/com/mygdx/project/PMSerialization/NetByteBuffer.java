package com.mygdx.project.PMSerialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class NetByteBuffer implements java.io.Serializable {
    private static final long serialVersionUID = -2831273345165209113L;

    // mark as transient so this is not serialized by default
    transient ByteBuffer data;

    public NetByteBuffer(ByteBuffer data) {
        this.data = data;
    }

    public ByteBuffer getData() {
        return this.data;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        // write default properties
        out.defaultWriteObject();
        // write buffer capacity and data
        out.writeInt(data.capacity());
        out.write(data.array());

    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        //read default properties
        in.defaultReadObject();

        //read buffer data and wrap with ByteBuffer
        int bufferSize = in.readInt();
        byte[] buffer = new byte[bufferSize];
        in.read(buffer, 0, bufferSize);
        this.data = ByteBuffer.wrap(buffer, 0, bufferSize);
    }
}