package com.antobo.apps.ventris;

/**
 * Created by antonnw on 22/06/2016.
 */
public class ItemAdapter {

    private String nomor;
    private String nama;

    public ItemAdapter(String nomor, String nama) {
        this.setNomor(nomor);
        this.setNama(nama);
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getNama() {
//        return nama.substring(0,1).toUpperCase() + nama.substring(1).toLowerCase();
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

}
