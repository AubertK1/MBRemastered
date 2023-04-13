package com.mygdx.project.PMSerialization;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.project.Temp.NetGdx2DPixmap2;

import java.nio.ByteBuffer;

public class NetPixmap implements Disposable {
    private com.badlogic.gdx.graphics.Pixmap.Blending blending;
    private com.badlogic.gdx.graphics.Pixmap.Filter filter;
    final NetGdx2DPixmap2 pixmap;
    int color;
    private boolean disposed;

    public static com.badlogic.gdx.graphics.Pixmap createFromFrameBuffer(int x, int y, int w, int h) {
        Gdx.gl.glPixelStorei(3333, 1);
        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(w, h, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        ByteBuffer pixels = pixmap.getPixels();
        Gdx.gl.glReadPixels(x, y, w, h, 6408, 5121, pixels);
        return pixmap;
    }

    public void setBlending(com.badlogic.gdx.graphics.Pixmap.Blending blending) {
        this.blending = blending;
        this.pixmap.setBlend(blending == com.badlogic.gdx.graphics.Pixmap.Blending.None ? 0 : 1);
    }

    public void setFilter(com.badlogic.gdx.graphics.Pixmap.Filter filter) {
        this.filter = filter;
        this.pixmap.setScale(filter == com.badlogic.gdx.graphics.Pixmap.Filter.NearestNeighbour ? 0 : 1);
    }

    public NetPixmap(int width, int height, NetPixmap.Format format) {
        this.blending = com.badlogic.gdx.graphics.Pixmap.Blending.SourceOver;
        this.filter = com.badlogic.gdx.graphics.Pixmap.Filter.BiLinear;
        this.color = 0;
        this.pixmap = new NetGdx2DPixmap2(width, height, NetPixmap.Format.toGdx2DPixmapFormat(format));
        this.setColor(0.0F, 0.0F, 0.0F, 0.0F);
        this.fill();
    }

/*
    public NetPixmap(byte[] encodedData, int offset, int len) {
        this.blending = com.badlogic.gdx.graphics.Pixmap.Blending.SourceOver;
        this.filter = com.badlogic.gdx.graphics.Pixmap.Filter.BiLinear;
        this.color = 0;

        try {
            this.pixmap = new NetGdx2DPixmap(encodedData, offset, len, 0);
        } catch (IOException var5) {
            throw new GdxRuntimeException("Couldn't load pixmap from image data", var5);
        }
    }

    public NetPixmap(ByteBuffer encodedData, int offset, int len) {
        this.blending = com.badlogic.gdx.graphics.Pixmap.Blending.SourceOver;
        this.filter = com.badlogic.gdx.graphics.Pixmap.Filter.BiLinear;
        this.color = 0;
        if (!encodedData.isDirect()) {
            throw new GdxRuntimeException("Couldn't load pixmap from non-direct ByteBuffer");
        } else {
            try {
                this.pixmap = new NetGdx2DPixmap(encodedData, offset, len, 0);
            } catch (IOException var5) {
                throw new GdxRuntimeException("Couldn't load pixmap from image data", var5);
            }
        }
    }

    public NetPixmap(ByteBuffer encodedData) {
        this(encodedData, encodedData.position(), encodedData.remaining());
    }
*/

/*
    public NetPixmap(FileHandle file) {
        this.blending = com.badlogic.gdx.graphics.Pixmap.Blending.SourceOver;
        this.filter = com.badlogic.gdx.graphics.Pixmap.Filter.BiLinear;
        this.color = 0;

        try {
            byte[] bytes = file.readBytes();
            this.pixmap = new NetGdx2DPixmap(bytes, 0, bytes.length, 0);
        } catch (Exception var3) {
            throw new GdxRuntimeException("Couldn't load file: " + file, var3);
        }
    }

    public NetPixmap(NetGdx2DPixmap pixmap) {
        this.blending = com.badlogic.gdx.graphics.Pixmap.Blending.SourceOver;
        this.filter = com.badlogic.gdx.graphics.Pixmap.Filter.BiLinear;
        this.color = 0;
        this.pixmap = pixmap;
    }
*/

    public Pixmap toPixmap(){
        return new Pixmap(pixmap.toGdx2DPixmap());
    }

    public static void downloadFromUrl(String url, final com.badlogic.gdx.graphics.Pixmap.DownloadPixmapResponseListener responseListener) {
        Net.HttpRequest request = new Net.HttpRequest("GET");
        request.setUrl(url);
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                final byte[] result = httpResponse.getResult();
                Gdx.app.postRunnable(new Runnable() {
                    public void run() {
                        try {
                            com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(result, 0, result.length);
                            responseListener.downloadComplete(pixmap);
                        } catch (Throwable var2) {
                            failed(var2);
                        }

                    }
                });
            }

            public void failed(Throwable t) {
                responseListener.downloadFailed(t);
            }

            public void cancelled() {
            }
        });
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setColor(float r, float g, float b, float a) {
        this.color = Color.rgba8888(r, g, b, a);
    }

    public void setColor(Color color) {
        this.color = Color.rgba8888(color.r, color.g, color.b, color.a);
    }

    public void fill() {
        this.pixmap.clear(this.color);
    }

    public void drawLine(int x, int y, int x2, int y2) {
        this.pixmap.drawLine(x, y, x2, y2, this.color);
    }

    public void drawRectangle(int x, int y, int width, int height) {
        this.pixmap.drawRect(x, y, width, height, this.color);
    }

    public void drawPixmap(NetPixmap pixmap, int x, int y) {
        this.drawPixmap(pixmap, x, y, 0, 0, pixmap.getWidth(), pixmap.getHeight());
    }

    public void drawPixmap(NetPixmap pixmap, int x, int y, int srcx, int srcy, int srcWidth, int srcHeight) {
        this.pixmap.drawPixmap(pixmap.pixmap, srcx, srcy, x, y, srcWidth, srcHeight);
    }

    public void drawPixmap(NetPixmap pixmap, int srcx, int srcy, int srcWidth, int srcHeight, int dstx, int dsty, int dstWidth, int dstHeight) {
        this.pixmap.drawPixmap(pixmap.pixmap, srcx, srcy, srcWidth, srcHeight, dstx, dsty, dstWidth, dstHeight);
    }

    public void fillRectangle(int x, int y, int width, int height) {
        this.pixmap.fillRect(x, y, width, height, this.color);
    }

    public void drawCircle(int x, int y, int radius) {
        this.pixmap.drawCircle(x, y, radius, this.color);
    }

    public void fillCircle(int x, int y, int radius) {
        this.pixmap.fillCircle(x, y, radius, this.color);
    }

    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        this.pixmap.fillTriangle(x1, y1, x2, y2, x3, y3, this.color);
    }

    public int getPixel(int x, int y) {
        return this.pixmap.getPixel(x, y);
    }

    public int getWidth() {
        return this.pixmap.getWidth();
    }

    public int getHeight() {
        return this.pixmap.getHeight();
    }

    public void dispose() {
        if (this.disposed) {
            throw new GdxRuntimeException("Pixmap already disposed!");
        } else {
            this.pixmap.dispose();
            this.disposed = true;
        }
    }

    public boolean isDisposed() {
        return this.disposed;
    }

    public void drawPixel(int x, int y) {
        this.pixmap.setPixel(x, y, this.color);
    }

    public void drawPixel(int x, int y, int color) {
        this.pixmap.setPixel(x, y, color);
    }

    public int getGLFormat() {
        return this.pixmap.getGLFormat();
    }

    public int getGLInternalFormat() {
        return this.pixmap.getGLInternalFormat();
    }

    public int getGLType() {
        return this.pixmap.getGLType();
    }

    public ByteBuffer getPixels() {
        if (this.disposed) {
            throw new GdxRuntimeException("Pixmap already disposed");
        } else {
            return this.pixmap.getPixels();
        }
    }

    public void setPixels(ByteBuffer pixels) {
        ByteBuffer dst = this.pixmap.getPixels();
        BufferUtils.copy(pixels, dst, dst.limit());
    }

    public com.badlogic.gdx.graphics.Pixmap.Format getFormat() {
        return com.badlogic.gdx.graphics.Pixmap.Format.fromGdx2DPixmapFormat(this.pixmap.getFormat());
    }

    public com.badlogic.gdx.graphics.Pixmap.Blending getBlending() {
        return this.blending;
    }

    public com.badlogic.gdx.graphics.Pixmap.Filter getFilter() {
        return this.filter;
    }

    public interface DownloadPixmapResponseListener {
        void downloadComplete(com.badlogic.gdx.graphics.Pixmap var1);

        void downloadFailed(Throwable var1);
    }

    public static enum Filter {
        NearestNeighbour,
        BiLinear;

        private Filter() {
        }
    }

    public static enum Blending {
        None,
        SourceOver;

        private Blending() {
        }
    }

    public static enum Format {
        Alpha,
        Intensity,
        LuminanceAlpha,
        RGB565,
        RGBA4444,
        RGB888,
        RGBA8888;

        private Format() {
        }

        public static int toGdx2DPixmapFormat(NetPixmap.Format format) {
            if (format == Alpha) {
                return 1;
            } else if (format == Intensity) {
                return 1;
            } else if (format == LuminanceAlpha) {
                return 2;
            } else if (format == RGB565) {
                return 5;
            } else if (format == RGBA4444) {
                return 6;
            } else if (format == RGB888) {
                return 3;
            } else if (format == RGBA8888) {
                return 4;
            } else {
                throw new GdxRuntimeException("Unknown Format: " + format);
            }
        }

        public static NetPixmap.Format fromGdx2DPixmapFormat(int format) {
            if (format == 1) {
                return Alpha;
            } else if (format == 2) {
                return LuminanceAlpha;
            } else if (format == 5) {
                return RGB565;
            } else if (format == 6) {
                return RGBA4444;
            } else if (format == 3) {
                return RGB888;
            } else if (format == 4) {
                return RGBA8888;
            } else {
                throw new GdxRuntimeException("Unknown Gdx2DPixmap Format: " + format);
            }
        }

        public static int toGlFormat(NetPixmap.Format format) {
            return Gdx2DPixmap.toGlFormat(toGdx2DPixmapFormat(format));
        }

        public static int toGlType(NetPixmap.Format format) {
            return Gdx2DPixmap.toGlType(toGdx2DPixmapFormat(format));
        }
    }
}
