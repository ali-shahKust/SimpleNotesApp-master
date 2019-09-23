package com.infusiblecoders.simplenotesapp;

/**
 * Created by SUDA on 08-09-2017.
 */

public class NoteModel {

    public String title;
    public long timestamp;
    public String content;
    public String iD;
    public NoteModel() {

    }

    public NoteModel(String title, long timestamp, String content, String iD) {
        this.title = title;
        this.timestamp = timestamp;
        this.content = content;
        this.iD = iD;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }
}
