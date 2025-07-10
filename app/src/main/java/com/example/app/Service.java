package com.example.app;

public class Service {
    private String serviceId;
    private String serviceName;
    private int imageResourceId;
    private String description;
    private double price;

    // Constructor matching your current usage (String, String, int)
    public Service(String serviceId, String serviceName, int imageResourceId) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.imageResourceId = imageResourceId;
        this.description = "";
        this.price = 0.0;
    }

    // Constructor with all fields
    public Service(String serviceId, String serviceName, int imageResourceId, String description, double price) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.imageResourceId = imageResourceId;
        this.description = description;
        this.price = price;
    }

    // Getters
    public String getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    // Setters
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceId='" + serviceId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", imageResourceId=" + imageResourceId +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}