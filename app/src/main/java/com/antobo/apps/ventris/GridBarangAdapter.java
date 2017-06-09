package com.antobo.apps.ventris;

/**
 * Created by antonnw on 22/06/2016.
 */
public class GridBarangAdapter {

    private String nomor;
    private String nama;
    private String nomorsatuanharga;
    private String nomorsatuanunit;
    private String jumlahharga;
    private String satuanharga;
    private String harga;
    private String jumlahunit;
    private String satuanunit;
    private String disc1;
    private String disc2;
    private String disc3;
    private String netto;
    private String total;
    private String colorshade;
    private String jumlahcolorshade;

    public GridBarangAdapter(String nomor, String nama, String nomorsatuanharga, String nomorsatuanunit, String jumlahharga, String satuanharga, String harga, String jumlahunit, String satuanunit, String disc1, String disc2, String disc3, String netto, String total, String colorshade, String jumlahcolorshade) {
        this.setNomor(nomor);
        this.setNama(nama);
        this.setNomorsatuanharga(nomorsatuanharga);
        this.setNomorsatuanunit(nomorsatuanunit);
        this.setJumlahharga(jumlahharga);
        this.setSatuanharga(satuanharga);
        this.setHarga(harga);
        this.setJumlahunit(jumlahunit);
        this.setSatuanunit(satuanunit);
        this.setDisc1(disc1);
        this.setDisc2(disc2);
        this.setDisc3(disc3);
        this.setNetto(netto);
        this.setTotal(total);
        this.setColorshade(colorshade);
        this.setJumlahcolorshade(jumlahcolorshade);
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

    public String getNomorsatuanharga() {
        return nomorsatuanharga;
    }

    public void setNomorsatuanharga(String nomorsatuanharga) {
        this.nomorsatuanharga = nomorsatuanharga;
    }

    public String getNomorsatuanunit() {
        return nomorsatuanunit;
    }

    public void setNomorsatuanunit(String nomorsatuanunit) {
        this.nomorsatuanunit = nomorsatuanunit;
    }

    public String getJumlahharga() {
        return jumlahharga;
    }

    public void setJumlahharga(String jumlahharga) {
        this.jumlahharga = jumlahharga;
    }

    public String getSatuanharga() {
        return satuanharga;
    }

    public void setSatuanharga(String satuanharga) {
        this.satuanharga = satuanharga;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getJumlahunit() {
        return jumlahunit;
    }

    public void setJumlahunit(String jumlahunit) {
        this.jumlahunit = jumlahunit;
    }

    public String getSatuanunit() {
        return satuanunit;
    }

    public void setSatuanunit(String satuanunit) {
        this.satuanunit = satuanunit;
    }

    public String getDisc1() {
        return disc1;
    }

    public void setDisc1(String disc1) {
        this.disc1 = disc1;
    }

    public String getDisc2() {
        return disc2;
    }

    public void setDisc2(String disc2) {
        this.disc2 = disc2;
    }

    public String getDisc3() {
        return disc3;
    }

    public void setDisc3(String disc3) {
        this.disc3 = disc3;
    }

    public String getNetto() {
        return netto;
    }

    public void setNetto(String netto) {
        this.netto = netto;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getColorshade() {
        return colorshade;
    }

    public void setColorshade(String colorshade) {
        this.colorshade = colorshade;
    }

    public String getJumlahcolorshade() {
        return jumlahcolorshade;
    }

    public void setJumlahcolorshade(String jumlahcolorshade) {
        this.jumlahcolorshade = jumlahcolorshade;
    }
}
