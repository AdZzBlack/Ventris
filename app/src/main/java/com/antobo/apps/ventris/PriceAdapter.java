package com.antobo.apps.ventris;

/**
 * Created by antonnw on 22/06/2016.
 */
public class PriceAdapter {

    private String kode;
    private String namaBeli;
    private String namaJual;
    private String luas;
    private String idr1;
    private String idr2;
    private String idr3;
    private String usd1;
    private String usd2;
    private String usd3;
    private String rmb1;
    private String rmb2;
    private String rmb3;
    private String satuan1;
    private String satuan2;
    private String satuan3;
    private String konversi1;
    private String konversi2;
    private String konversi3;

    public PriceAdapter(String kode, String namaBeli, String namaJual, String luas, String idr1, String idr2, String idr3, String usd1, String usd2, String usd3, String rmb1, String rmb2, String rmb3, String satuan1, String satuan2, String satuan3, String konversi1, String konversi2, String konversi3) {
        this.setKode(kode);
        this.setNamaBeli(namaBeli);
        this.setNamaJual(namaJual);
        this.setLuas(luas);
        this.setIdr1(idr1);
        this.setIdr2(idr2);
        this.setIdr3(idr3);
        this.setUsd1(usd1);
        this.setUsd2(usd2);
        this.setUsd3(usd3);
        this.setRmb1(rmb1);
        this.setRmb2(rmb2);
        this.setRmb3(rmb3);
        this.setSatuan1(satuan1);
        this.setSatuan2(satuan2);
        this.setSatuan3(satuan3);
        this.setKonversi1(konversi1);
        this.setKonversi2(konversi2);
        this.setKonversi3(konversi3);
    }

    public String getKode() {
        return kode;
    }
    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNamaBeli() {
        return namaBeli;
    }
    public void setNamaBeli(String namaBeli) {
        this.namaBeli = namaBeli;
    }

    public String getNamaJual() {
        return namaJual;
    }
    public void setNamaJual(String namaJual) {
        this.namaJual = namaJual;
    }

    public String getLuas() { return luas; }
    public void setLuas(String luas) {
        this.luas = luas;
    }

    public String getIdr1() { return idr1; }
    public void setIdr1(String idr1) {
        this.idr1 = idr1;
    }

    public String getIdr2() { return idr2; }
    public void setIdr2(String idr2) {
        this.idr2 = idr2;
    }

    public String getIdr3() { return idr3; }
    public void setIdr3(String idr3) {
        this.idr3 = idr3;
    }

    public String getUsd1() { return usd1; }
    public void setUsd1(String usd1) {
        this.usd1 = usd1;
    }

    public String getUsd2() { return usd2; }
    public void setUsd2(String usd2) {
        this.usd2 = usd2;
    }

    public String getUsd3() { return usd3; }
    public void setUsd3(String usd3) {
        this.usd3 = usd3;
    }

    public String getRmb1() { return rmb1; }
    public void setRmb1(String rmb1) {
        this.rmb1 = rmb1;
    }

    public String getRmb2() { return rmb2; }
    public void setRmb2(String rmb2) {
        this.rmb2 = rmb2;
    }

    public String getRmb3() { return rmb3; }
    public void setRmb3(String rmb3) {
        this.rmb3 = rmb3;
    }

    public String getSatuan1() { return satuan1; }
    public void setSatuan1(String satuan1) {
        this.satuan1 = satuan1;
    }

    public String getSatuan2() {
        return satuan2;
    }
    public void setSatuan2(String satuan2) {
        this.satuan2 = satuan2;
    }

    public String getSatuan3() {
        return satuan3;
    }
    public void setSatuan3(String satuan3) {
        this.satuan3 = satuan3;
    }

    public String getKonversi1() { return konversi1; }
    public void setKonversi1(String konversi1) {
        this.konversi1 = konversi1;
    }

    public String getKonversi2() { return konversi2; }
    public void setKonversi2(String konversi2) {
        this.konversi2 = konversi2;
    }

    public String getKonversi3() { return konversi3; }
    public void setKonversi3(String konversi3) {
        this.konversi3 = konversi3;
    }
}
