package com.antobo.apps.ventris;

/**
 * Created by antonnw on 22/06/2016.
 */
public class MessageAdapter {

    private String nama;
    private String message;
    private String time;

    public MessageAdapter(String nama, String message, String time) {
        this.setNama(nama);
        this.setMessage(message);
        this.setTime(time);
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
