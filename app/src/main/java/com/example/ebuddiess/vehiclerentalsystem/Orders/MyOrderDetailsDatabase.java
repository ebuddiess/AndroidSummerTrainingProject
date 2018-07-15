package com.example.ebuddiess.vehiclerentalsystem.Orders;

public class MyOrderDetailsDatabase {
      MyOrderDetailsDatabase(){}
      String orderid;
     String carid;
     String carname;
     String carcategory;
     String userid;
     String startdate;
     String starttime;
     String enddate;
    String endttime;
    int noofdays;
    int totalprice;
    int carperdayprice;
    Integer locationprice;
    int doorpickingprice;
    String selectedcity;

    public MyOrderDetailsDatabase(String orderid, String carid, String carname, String carcategory, String userid, String startdate, String starttime, String enddate, String endttime, int noofdays, int totalprice, int carperdayprice, Integer locationprice, int doorpickingprice, String selectedcity) {
        this.orderid = orderid;
        this.carid = carid;
        this.carname = carname;
        this.carcategory = carcategory;
        this.userid = userid;
        this.startdate = startdate;
        this.starttime = starttime;
        this.enddate = enddate;
        this.endttime = endttime;
        this.noofdays = noofdays;
        this.totalprice = totalprice;
        this.carperdayprice = carperdayprice;
        this.locationprice = locationprice;
        this.doorpickingprice = doorpickingprice;
        this.selectedcity = selectedcity;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }

    public String getCarname() {
        return carname;
    }

    public void setCarname(String carname) {
        this.carname = carname;
    }

    public String getCarcategory() {
        return carcategory;
    }

    public void setCarcategory(String carcategory) {
        this.carcategory = carcategory;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getEndttime() {
        return endttime;
    }

    public void setEndttime(String endttime) {
        this.endttime = endttime;
    }

    public int getNoofdays() {
        return noofdays;
    }

    public void setNoofdays(int noofdays) {
        this.noofdays = noofdays;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public int getCarperdayprice() {
        return carperdayprice;
    }

    public void setCarperdayprice(int carperdayprice) {
        this.carperdayprice = carperdayprice;
    }

    public Integer getLocationprice() {
        return locationprice;
    }

    public void setLocationprice(Integer locationprice) {
        this.locationprice = locationprice;
    }

    public int getDoorpickingprice() {
        return doorpickingprice;
    }

    public void setDoorpickingprice(int doorpickingprice) {
        this.doorpickingprice = doorpickingprice;
    }

    public String getSelectedcity() {
        return selectedcity;
    }

    public void setSelectedcity(String selectedcity) {
        this.selectedcity = selectedcity;
    }
}
