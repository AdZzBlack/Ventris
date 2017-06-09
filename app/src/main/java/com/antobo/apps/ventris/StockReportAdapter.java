package com.antobo.apps.ventris;

/**
 * Created by antonnw on 22/06/2016.
 */
public class StockReportAdapter {

    private String kode;
    private String nama;
    private String gudang;
    private String stock;
    private String pending;
    private String satuan;
    private String info1;
    private String info2;
    private String shade;

    public StockReportAdapter(String kode, String nama, String gudang, String stock, String pending, String satuan, String info1, String info2, String shade) {
        this.setKode(kode);
        this.setNama(nama);
        this.setGudang(gudang);
        this.setStock(stock);
        this.setPending(pending);
        this.setSatuan(satuan);
        this.setInfo1(info1);
        this.setInfo2(info2);
        this.setShade(shade);
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getGudang() {
        return gudang;
    }

    public void setGudang(String gudang) {
        this.gudang = gudang;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getInfo1() {return info1;}

    public void setInfo1(String info1) {
        this.info1 = info1;
    }

    public String getInfo2() {
        return info2;
    }

    public void setInfo2(String info2) {
        this.info2 = info2;
    }

    public String getShade() {
        return shade;
    }

    public void setShade(String shade) {
        this.shade = shade;
    }
}
