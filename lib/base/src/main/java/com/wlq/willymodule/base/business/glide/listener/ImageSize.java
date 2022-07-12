package com.wlq.willymodule.base.business.glide.listener;

public class ImageSize {

    private final static String SEPARATOR = "x";

    private final int width;
    private final int height;

    public ImageSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return width + SEPARATOR + height;
    }
}
