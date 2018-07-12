package com.example.ebuddiess.vehiclerentalsystem.ManageCars;

public class Car {
    String carid;
    String carName;
    String car_image_url;
    String car_category;
    String fuelType;
    int seating_capcaity;
    String uploadedby;
    int pricing;

    public Car(String carid, String carName, String car_image_url, String car_category, String fuelType, int seating_capcaity, String uploadedby, int pricing) {
        this.carid = carid;
        this.carName = carName;
        this.car_image_url = car_image_url;
        this.car_category = car_category;
        this.fuelType = fuelType;
        this.seating_capcaity = seating_capcaity;
        this.uploadedby = uploadedby;
        this.pricing = pricing;
    }

    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCar_image_url() {
        return car_image_url;
    }

    public void setCar_image_url(String car_image_url) {
        this.car_image_url = car_image_url;
    }

    public String getCar_category() {
        return car_category;
    }

    public void setCar_category(String car_category) {
        this.car_category = car_category;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public int getSeating_capcaity() {
        return seating_capcaity;
    }

    public void setSeating_capcaity(int seating_capcaity) {
        this.seating_capcaity = seating_capcaity;
    }

    public String getUploadedby() {
        return uploadedby;
    }

    public void setUploadedby(String uploadedby) {
        this.uploadedby = uploadedby;
    }

    public int getPricing() {
        return pricing;
    }

    public void setPricing(int pricing) {
        this.pricing = pricing;
    }
}
