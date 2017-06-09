package com.antobo.apps.ventris;

/**
 * Created by shirogane on 11/17/2016.
 */
public class SalesOrderAdapter {

    private String nomor;
    private String kode;
    private String tanggal;
    private String tanggalkirim;
    private String gudang;
    private String customer;

    public SalesOrderAdapter(String nomor, String kode, String tanggal, String tanggalkirim, String gudang, String customer) {
        this.setNomor(nomor);
        this.setKode(kode);
        this.setTanggal(tanggal);
        this.setTanggalkirim(tanggalkirim);
        this.setGudang(gudang);
        this.setCustomer(customer);
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTanggalkirim() {
        return tanggalkirim;
    }

    public void setTanggalkirim(String tanggalkirim) {
        this.tanggalkirim = tanggalkirim;
    }

    public String getGudang() {
        return gudang;
    }

    public void setGudang(String gudang) {
        this.gudang = gudang;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

}
