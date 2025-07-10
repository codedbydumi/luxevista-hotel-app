package com.example.app;

public class Room {
    private String roomNumber;
    private String oceanView;
    private String luxuryLevel;
    private int imageResource;  // New field for image resource ID

    public Room(String roomNumber, String oceanView, String luxuryLevel, int imageResource) {
        this.roomNumber = roomNumber;
        this.oceanView = oceanView;
        this.luxuryLevel = luxuryLevel;
        this.imageResource = imageResource;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getOceanView() {
        return oceanView;
    }

    public String getLuxuryLevel() {
        return luxuryLevel;
    }

    public int getImageResource() {
        return imageResource;
    }
}
