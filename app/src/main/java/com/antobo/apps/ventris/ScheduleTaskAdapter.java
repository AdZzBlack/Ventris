package com.antobo.apps.ventris;

/**
 * Created by antonnw on 22/06/2016.
 */
public class ScheduleTaskAdapter {

    private String nomor;
    private String managernomor;
    private String tipe;
    private String tanggal;
    private String jam;
    private String namacustomer;
    private String nama;
    private String jenisjadwal;
    private String keterangan;

    public ScheduleTaskAdapter(String nomor, String managernomor, String tipe, String tanggal, String jam, String namacustomer, String nama, String jenisjadwal, String keterangan) {
        this.setNomor(nomor);
        this.setManagernomor(managernomor);
        this.setTipe(tipe);
        this.setTanggal(tanggal);
        this.setJam(jam);
        this.setNamacustomer(namacustomer);
        this.setNama(nama);
        this.setJenisJadwal(jenisjadwal);
        this.setKeterangan(keterangan);
    }

    public String getNomor() { return nomor; }

    public void setNomor(String nomor) { this.nomor = nomor; }

    public String getManagernomor() {
        return managernomor;
    }

    public void setManagernomor(String managernomor) {
        this.managernomor = managernomor;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getNamacustomer() {
        return namacustomer;
    }

    public void setNamacustomer(String namacustomer) {
        this.namacustomer = namacustomer;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenisJadwal() {
        return jenisjadwal;
    }

    public void setJenisJadwal(String jenisjadwal) {
        this.jenisjadwal = jenisjadwal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }


}
