package com.antobo.apps.ventris;

/**
 * Created by antonnw on 22/06/2016.
 */
public class GridBiayaInternalAdapter {

    private String nomor;
    private String nama;
    private String deskripsi;
    private String totalcust;
    private String ppn;
    private String total;
    private String totalidr;

    public GridBiayaInternalAdapter(String nomor, String nama, String deskripsi, String totalcust, String ppn, String total, String totalidr) {
        this.setNomor(nomor);
        this.setNama(nama);
        this.setDeskripsi(deskripsi);
        this.setTotalcust(totalcust);
        this.setPpn(ppn);
        this.setTotal(total);
        this.setTotalidr(totalidr);
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

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTotalcust() {
        return totalcust;
    }

    public void setTotalcust(String totalcust) {
        this.totalcust = totalcust;
    }

    public String getPpn() {
        return ppn;
    }

    public void setPpn(String ppn) {
        this.ppn = ppn;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotalidr() {
        return totalidr;
    }

    public void setTotalidr(String totalidr) {
        this.totalidr = totalidr;
    }

}
