package com.antobo.apps.ventris;

/**
 * Created by antonnw on 22/06/2016.
 */
public class GridSalesAdapter {

    private String nomor;
    private String nama;
    private String target;
    private String bulan;
    private String tahun;

    public GridSalesAdapter(String nomor, String nama, String target, String bulan, String tahun) {
        this.setNomor(nomor);
        this.setNama(nama);
        this.setTarget(target);
        this.setBulan(bulan);
        this.setTahun(tahun);
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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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
}
