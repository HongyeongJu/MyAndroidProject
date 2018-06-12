package com.hfad.mymp3;

public class ListItem {

    final static int MP3 = 0;           //Mp3 파일
    final static int FOLDER = 1;        // 폴더 파일
    final static int OTHERS = 2;        // Mp3가 아닌 파일

    private int resId;
    private String name;
    private int filetype;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ListItem(int resId, String name, int filetype, String path) {
        this.resId = resId;
        this.name = name;
        this.filetype = filetype;
        this.path = path;
    }

    public int getFiletype() {
        return filetype;
    }

    public void setFiletype(int filetype) {
        this.filetype = filetype;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
