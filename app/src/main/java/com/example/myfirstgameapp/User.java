package com.example.myfirstgameapp;

public class User implements Comparable<User> {
    private String name;
    private int score;
    private String date;
    private double latitude;
    private double longitude;

    public User(String name, int score, String date, double latitude, double longitude) {
        this.name = name;
        this.score = score;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }


    @Override
    public String toString() {
        return String.format("Name: %-2s", name) + String.format(" Score: %-2s", score) + String.format(" Date: %-2s", date);
    }

    @Override
    public int compareTo(User user) {
        return user.getScore() - this.getScore();
    }

}
