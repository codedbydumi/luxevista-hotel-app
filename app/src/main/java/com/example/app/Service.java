package com.example.app;

public class Service {
    private String serviceId;
    private String serviceName;
    private int imageResourceId;
    private String serviceIcon;
    private String description;
    private double price;
    private boolean isBooked;

    // Constructor with image resource
    public Service(String serviceId, String serviceName, int imageResourceId) {
        this(serviceId, serviceName, imageResourceId, "Professional service with premium quality", 0.0);
    }

    // Constructor with image resource and details
    public Service(String serviceId, String serviceName, int imageResourceId, String description, double price) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.imageResourceId = imageResourceId;
        this.description = description;
        this.price = price;
        this.isBooked = false;
        this.serviceIcon = getDefaultIcon(serviceName);
    }

    // Constructor with icon (for booked services)
    public Service(String serviceId, String serviceName, String serviceIcon, String description, double price) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceIcon = serviceIcon;
        this.description = description;
        this.price = price;
        this.isBooked = false;
        this.imageResourceId = 0;
    }

    // Helper method to get default icon based on service name
    private String getDefaultIcon(String serviceName) {
        if (serviceName.toLowerCase().contains("clean")) return "🧹";
        if (serviceName.toLowerCase().contains("laundry")) return "👕";
        if (serviceName.toLowerCase().contains("spa")) return "💆";
        if (serviceName.toLowerCase().contains("transport")) return "🚗";
        if (serviceName.toLowerCase().contains("restaurant")) return "🍽️";
        return "🛎️"; // Default icon
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

    public String getServiceIcon() {
        return serviceIcon;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public boolean isBooked() {
        return isBooked;
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

    public void setServiceIcon(String serviceIcon) {
        this.serviceIcon = serviceIcon;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceId='" + serviceId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", imageResourceId=" + imageResourceId +
                ", serviceIcon='" + serviceIcon + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", isBooked=" + isBooked +
                '}';
    }
}