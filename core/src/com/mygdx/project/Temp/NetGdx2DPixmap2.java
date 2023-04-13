package com.mygdx.project.Temp;

import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.project.PMSerialization.NetByteBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class NetGdx2DPixmap2 implements Disposable {
    public static final int GDX2D_FORMAT_ALPHA = 1;
    public static final int GDX2D_FORMAT_LUMINANCE_ALPHA = 2;
    public static final int GDX2D_FORMAT_RGB888 = 3;
    public static final int GDX2D_FORMAT_RGBA8888 = 4;
    public static final int GDX2D_FORMAT_RGB565 = 5;
    public static final int GDX2D_FORMAT_RGBA4444 = 6;
    public static final int GDX2D_SCALE_NEAREST = 0;
    public static final int GDX2D_SCALE_LINEAR = 1;
    public static final int GDX2D_BLEND_NONE = 0;
    public static final int GDX2D_BLEND_SRC_OVER = 1;
    long basePtr;
    int width;
    int height;
    int format;
    NetByteBuffer pixelPtr;
    long[] nativeData = new long[4];

    public static int toGlFormat(int format) {
        switch (format) {
            case 1:
                return 6406;
            case 2:
                return 6410;
            case 3:
            case 5:
                return 6407;
            case 4:
            case 6:
                return 6408;
            default:
                throw new GdxRuntimeException("unknown format: " + format);
        }
    }

    public static int toGlType(int format) {
        switch (format) {
            case 1:
            case 2:
            case 3:
            case 4:
                return 5121;
            case 5:
                return 33635;
            case 6:
                return 32819;
            default:
                throw new GdxRuntimeException("unknown format: " + format);
        }
    }

/*
    public NetGdx2DPixmap(byte[] encodedData, int offset, int len, int requestedFormat) throws IOException {
        this.pixelPtr = new NetByteBuffer(load(this.nativeData, encodedData, offset, len));
        if (this.pixelPtr.getData() == null) {
            throw new IOException("Error loading pixmap: " + getFailureReason());
        } else {
            this.basePtr = this.nativeData[0];
            this.width = (int)this.nativeData[1];
            this.height = (int)this.nativeData[2];
            this.format = (int)this.nativeData[3];
            if (requestedFormat != 0 && requestedFormat != this.format) {
                this.convert(requestedFormat);
            }

        }
    }
*/

/*
    public NetGdx2DPixmap(ByteBuffer encodedData, int offset, int len, int requestedFormat) throws IOException {
        this.pixelPtr = new NetByteBuffer(loadByteBuffer(this.nativeData, encodedData, offset, len));
        if (this.pixelPtr.getData() == null) {
            throw new IOException("Error loading pixmap: " + getFailureReason());
        } else {
            this.basePtr = this.nativeData[0];
            this.width = (int)this.nativeData[1];
            this.height = (int)this.nativeData[2];
            this.format = (int)this.nativeData[3];
            if (requestedFormat != 0 && requestedFormat != this.format) {
                this.convert(requestedFormat);
            }

        }
    }
*/

    public NetGdx2DPixmap2(InputStream in, int requestedFormat) throws IOException {
        while(true){
            String s = "Crash Program if This Function is Important";
        }
/*
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int readBytes = false;

        int readBytes;
        while((readBytes = in.read(buffer)) != -1) {
            bytes.write(buffer, 0, readBytes);
        }

        buffer = bytes.toByteArray();
        this.pixelPtr = load(this.nativeData, buffer, 0, buffer.length);
        if (this.pixelPtr == null) {
            throw new IOException("Error loading pixmap: " + getFailureReason());
        } else {
            this.basePtr = this.nativeData[0];
            this.width = (int)this.nativeData[1];
            this.height = (int)this.nativeData[2];
            this.format = (int)this.nativeData[3];
            if (requestedFormat != 0 && requestedFormat != this.format) {
                this.convert(requestedFormat);
            }

        }
*/
    }

    public NetGdx2DPixmap2(int width, int height, int format) throws GdxRuntimeException {
        this.pixelPtr = new NetByteBuffer(newPixmap(this.nativeData, width, height, format));
        if (this.pixelPtr.getData() == null) {
            throw new GdxRuntimeException("Unable to allocate memory for pixmap: " + width + "x" + height + ", " + getFormatString(format));
        } else {
            this.basePtr = this.nativeData[0];
            this.width = (int)this.nativeData[1];
            this.height = (int)this.nativeData[2];
            this.format = (int)this.nativeData[3];
        }
    }

/*
    public NetGdx2DPixmap(ByteBuffer pixelPtr, long[] nativeData) {
        this.pixelPtr = new NetByteBuffer(pixelPtr);
        this.basePtr = nativeData[0];
        this.width = (int)nativeData[1];
        this.height = (int)nativeData[2];
        this.format = (int)nativeData[3];
    }
*/

    public Gdx2DPixmap toGdx2DPixmap(){
        return new Gdx2DPixmap(pixelPtr.getData(), nativeData);
    }

    private void convert(int requestedFormat) {
        NetGdx2DPixmap2 pixmap = new NetGdx2DPixmap2(this.width, this.height, requestedFormat);
        pixmap.setBlend(0);
        pixmap.drawPixmap(this, 0, 0, 0, 0, this.width, this.height);
        this.dispose();
        this.basePtr = pixmap.basePtr;
        this.format = pixmap.format;
        this.height = pixmap.height;
        this.nativeData = pixmap.nativeData;
        this.pixelPtr = pixmap.pixelPtr;
        this.width = pixmap.width;
    }

    public void dispose() {
        free(this.basePtr);
    }

    public void clear(int color) {
        clear(this.basePtr, color);
    }

    public void setPixel(int x, int y, int color) {
        setPixel(this.basePtr, x, y, color);
    }

    public int getPixel(int x, int y) {
        return getPixel(this.basePtr, x, y);
    }

    public void drawLine(int x, int y, int x2, int y2, int color) {
        drawLine(this.basePtr, x, y, x2, y2, color);
    }

    public void drawRect(int x, int y, int width, int height, int color) {
        drawRect(this.basePtr, x, y, width, height, color);
    }

    public void drawCircle(int x, int y, int radius, int color) {
        drawCircle(this.basePtr, x, y, radius, color);
    }

    public void fillRect(int x, int y, int width, int height, int color) {
        fillRect(this.basePtr, x, y, width, height, color);
    }

    public void fillCircle(int x, int y, int radius, int color) {
        fillCircle(this.basePtr, x, y, radius, color);
    }

    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int color) {
        fillTriangle(this.basePtr, x1, y1, x2, y2, x3, y3, color);
    }

    public void drawPixmap(NetGdx2DPixmap2 src, int srcX, int srcY, int dstX, int dstY, int width, int height) {
        drawPixmap(src.basePtr, this.basePtr, srcX, srcY, width, height, dstX, dstY, width, height);
    }

    public void drawPixmap(NetGdx2DPixmap2 src, int srcX, int srcY, int srcWidth, int srcHeight, int dstX, int dstY, int dstWidth, int dstHeight) {
        drawPixmap(src.basePtr, this.basePtr, srcX, srcY, srcWidth, srcHeight, dstX, dstY, dstWidth, dstHeight);
    }

    public void setBlend(int blend) {
        setBlend(this.basePtr, blend);
    }

    public void setScale(int scale) {
        setScale(this.basePtr, scale);
    }

    public static NetGdx2DPixmap2 newPixmap(InputStream in, int requestedFormat) {
        try {
            return new NetGdx2DPixmap2(in, requestedFormat);
        } catch (IOException var3) {
            return null;
        }
    }

    public static NetGdx2DPixmap2 newPixmap(int width, int height, int format) {
        try {
            return new NetGdx2DPixmap2(width, height, format);
        } catch (IllegalArgumentException var4) {
            return null;
        }
    }

    public ByteBuffer getPixels() {
        return this.pixelPtr.getData();
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getFormat() {
        return this.format;
    }

    public int getGLInternalFormat() {
        return toGlFormat(this.format);
    }

    public int getGLFormat() {
        return this.getGLInternalFormat();
    }

    public int getGLType() {
        return toGlType(this.format);
    }

    public String getFormatString() {
        return getFormatString(this.format);
    }

    private static String getFormatString(int format) {
        switch (format) {
            case 1:
                return "alpha";
            case 2:
                return "luminance alpha";
            case 3:
                return "rgb888";
            case 4:
                return "rgba8888";
            case 5:
                return "rgb565";
            case 6:
                return "rgba4444";
            default:
                return "unknown";
        }
    }

    // @off
	/*JNI
	#include <gdx2d/gdx2d.h>
	#include <stdlib.h>
	 */

    private static native ByteBuffer load (long[] nativeData, byte[] buffer, int offset, int len); /*MANUAL
		const unsigned char* p_buffer = (const unsigned char*)env->GetPrimitiveArrayCritical(buffer, 0);
		gdx2d_pixmap* pixmap = gdx2d_load(p_buffer + offset, len);
		env->ReleasePrimitiveArrayCritical(buffer, (char*)p_buffer, 0);

		if(pixmap==0)
			return 0;

		jobject pixel_buffer = env->NewDirectByteBuffer((void*)pixmap->pixels, pixmap->width * pixmap->height * gdx2d_bytes_per_pixel(pixmap->format));
		jlong* p_native_data = (jlong*)env->GetPrimitiveArrayCritical(nativeData, 0);
		p_native_data[0] = (jlong)pixmap;
		p_native_data[1] = pixmap->width;
		p_native_data[2] = pixmap->height;
		p_native_data[3] = pixmap->format;
		env->ReleasePrimitiveArrayCritical(nativeData, p_native_data, 0);

		return pixel_buffer;
	 */

    private static native ByteBuffer loadByteBuffer (long[] nativeData, ByteBuffer buffer, int offset, int len); /*MANUAL
		if(buffer==0)
			return 0;

		const unsigned char* p_buffer = (const unsigned char*)env->GetDirectBufferAddress(buffer);
		gdx2d_pixmap* pixmap = gdx2d_load(p_buffer + offset, len);

		if(pixmap==0)
			return 0;

		jobject pixel_buffer = env->NewDirectByteBuffer((void*)pixmap->pixels, pixmap->width * pixmap->height * gdx2d_bytes_per_pixel(pixmap->format));
		jlong* p_native_data = (jlong*)env->GetPrimitiveArrayCritical(nativeData, 0);
		p_native_data[0] = (jlong)pixmap;
		p_native_data[1] = pixmap->width;
		p_native_data[2] = pixmap->height;
		p_native_data[3] = pixmap->format;
		env->ReleasePrimitiveArrayCritical(nativeData, p_native_data, 0);

		return pixel_buffer;
	 */

    private static native ByteBuffer newPixmap (long[] nativeData, int width, int height, int format); /*MANUAL
		gdx2d_pixmap* pixmap = gdx2d_new(width, height, format);
		if(pixmap==0)
			return 0;

		jobject pixel_buffer = env->NewDirectByteBuffer((void*)pixmap->pixels, pixmap->width * pixmap->height * gdx2d_bytes_per_pixel(pixmap->format));
		jlong* p_native_data = (jlong*)env->GetPrimitiveArrayCritical(nativeData, 0);
		p_native_data[0] = (jlong)pixmap;
		p_native_data[1] = pixmap->width;
		p_native_data[2] = pixmap->height;
		p_native_data[3] = pixmap->format;
		env->ReleasePrimitiveArrayCritical(nativeData, p_native_data, 0);

		return pixel_buffer;
	 */

    private static native void free (long pixmap); /*
		gdx2d_free((gdx2d_pixmap*)pixmap);
	 */

    private static native void clear (long pixmap, int color); /*
		gdx2d_clear((gdx2d_pixmap*)pixmap, color);
	 */

    private static native void setPixel (long pixmap, int x, int y, int color); /*
		gdx2d_set_pixel((gdx2d_pixmap*)pixmap, x, y, color);
	 */

    private static native int getPixel (long pixmap, int x, int y); /*
		return gdx2d_get_pixel((gdx2d_pixmap*)pixmap, x, y);
	 */

    private static native void drawLine (long pixmap, int x, int y, int x2, int y2, int color); /*
		gdx2d_draw_line((gdx2d_pixmap*)pixmap, x, y, x2, y2, color);
	 */

    private static native void drawRect (long pixmap, int x, int y, int width, int height, int color); /*
		gdx2d_draw_rect((gdx2d_pixmap*)pixmap, x, y, width, height, color);
	 */

    private static native void drawCircle (long pixmap, int x, int y, int radius, int color); /*
		gdx2d_draw_circle((gdx2d_pixmap*)pixmap, x, y, radius, color);
	 */

    private static native void fillRect (long pixmap, int x, int y, int width, int height, int color); /*
		gdx2d_fill_rect((gdx2d_pixmap*)pixmap, x, y, width, height, color);
	 */

    private static native void fillCircle (long pixmap, int x, int y, int radius, int color); /*
		gdx2d_fill_circle((gdx2d_pixmap*)pixmap, x, y, radius, color);
	 */

    private static native void fillTriangle (long pixmap, int x1, int y1, int x2, int y2, int x3, int y3, int color); /*
		gdx2d_fill_triangle((gdx2d_pixmap*)pixmap, x1, y1, x2, y2, x3, y3, color);
	 */

    private static native void drawPixmap (long src, long dst, int srcX, int srcY, int srcWidth, int srcHeight, int dstX,
                                           int dstY, int dstWidth, int dstHeight); /*
		gdx2d_draw_pixmap((gdx2d_pixmap*)src, (gdx2d_pixmap*)dst, srcX, srcY, srcWidth, srcHeight, dstX, dstY, dstWidth, dstHeight);
		 */

    private static native void setBlend (long src, int blend); /*
		gdx2d_set_blend((gdx2d_pixmap*)src, blend);
	 */

    private static native void setScale (long src, int scale); /*
		gdx2d_set_scale((gdx2d_pixmap*)src, scale);
	 */

    public static native String getFailureReason (); /*
     return env->NewStringUTF(gdx2d_get_failure_reason());
	 */
}
