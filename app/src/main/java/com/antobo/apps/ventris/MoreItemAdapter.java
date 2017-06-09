package com.antobo.apps.ventris;

/**
 * Created by antonnw on 22/06/2016.
 */
public class MoreItemAdapter {

    private String nomor;
    private String nama;
    private String data;

    public MoreItemAdapter(String nomor, String nama, String data) {
        this.setNomor(nomor);
        this.setNama(nama);
        this.setData(data);
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getNama() {
        return nama.substring(0,1).toUpperCase() + nama.substring(1).toLowerCase();
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
