package com.antobo.apps.ventris;

/**
 * Created by antonnw on 22/06/2016.
 */
public class AddUserAdapter {

    private boolean centang;
    private String nomor;
    private String nama;
    private String jabatan;
    private String hp;
    private String lokasi;
    private String latitude;
    private String longitude;

    public AddUserAdapter(Boolean centang, String nomor, String nama, String jabatan, String hp, String lokasi, String latitude, String longitude) {
        this.setCentang(centang);
        this.setNomor(nomor);
        this.setNama(nama);
        this.setJabatan(jabatan);
        this.setHp(hp);
        this.setLokasi(lokasi);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }

    public Boolean getCentang() {
        return centang;
    }

    public void setCentang(Boolean centang) {
        this.centang = centang;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
