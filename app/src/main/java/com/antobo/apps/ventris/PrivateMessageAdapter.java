package com.antobo.apps.ventris;

/**
 * Created by antonnw on 22/06/2016.
 */
public class PrivateMessageAdapter {

    private String nama;
    private String message;
    private String time;
    private String status;

    public PrivateMessageAdapter(String nama, String message, String time, String status) {
        this.setNama(nama);
        this.setMessage(message);
        this.setTime(time);
        this.setStatus(status);
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
