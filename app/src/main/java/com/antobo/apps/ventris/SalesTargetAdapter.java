package com.antobo.apps.ventris;

/**
 * Created by shirogane on 11/17/2016.
 */
public class SalesTargetAdapter {

    private String nomor;
    private String bex;
    private String bulan;
    private String tahun;
    private String target;

    public SalesTargetAdapter(String nomor, String bex, String bulan, String tahun, String target)
    {
        this.setNomor(nomor);
        this.setBex(bex);
        this.setBulan(bulan);
        this.setTahun(tahun);
        this.setTarget(target);
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getBex() {
        return bex;
    }

    public void setBex(String bex) {
        this.bex = bex;
    }

    public String getBulan() {
        return bulan;
    }

    public void setBulan(String bulan) {
        this.bulan = bulan;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
