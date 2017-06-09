package com.antobo.apps.ventris;

/**
 * Created by antonnw on 22/06/2016.
 */
public class OmzetReportAdapter {

    private String customer;
    private String dpp;
    private String brand;
    private String area;

    public OmzetReportAdapter(String customer, String dpp, String brand, String area) {
        this.setCustomer(customer);
        this.setDpp(dpp);
        this.setBrand(brand);
        this.setArea(area);
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDpp() {
        return dpp;
    }

    public void setDpp(String dpp) {
        this.dpp = dpp;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

}
