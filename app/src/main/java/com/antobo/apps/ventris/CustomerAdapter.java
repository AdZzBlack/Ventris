package com.antobo.apps.ventris;

/**
 * Created by antonnw on 22/06/2016.
 */
public class CustomerAdapter {

    private String nomor;
    private String nama;
    private String alamat;
    private String hp;

    public CustomerAdapter(String nomor, String nama, String alamat, String hp) {
        this.setNomor(nomor);
        this.setNama(nama);
        this.setAlamat(alamat);
        this.setHp(hp);
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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }
}
