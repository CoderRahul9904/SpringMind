package com.springmind.intelligent.entity;


public class LoveEntity {
    public String family;
    public String friends;
    public String passion;
    public int gratitudePercentage;

    public LoveEntity(String family, String friends, String passion, int gratitudePercentage) {
        this.family = family;
        this.friends = friends;
        this.passion = passion;
        this.gratitudePercentage = gratitudePercentage;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public String getPassion() {
        return passion;
    }

    public void setPassion(String passion) {
        this.passion = passion;
    }

    public int getGratitudePercentage() {
        return gratitudePercentage;
    }

    public void setGratitudePercentage(int gratitudePercentage) {
        this.gratitudePercentage = gratitudePercentage;
    }
}
