package com.antobo.apps.ventris;

/**
 * Created by antonnw on 22/06/2016.
 */
public class OmzetReportAdapter_new {

    private String sales;
    private String omzet;
    private String target;

    public OmzetReportAdapter_new(String sales, String omzet, String target) {
        this.setSales(sales);
        this.setOmzet(omzet);
        this.setTarget(target);
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getOmzet() {
        return omzet;
    }

    public void setOmzet(String omzet) {
        this.omzet = omzet;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
