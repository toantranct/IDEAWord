package com.ideastudio.ideaword.model;

public class Room {
    String uidHost, uidGuest, roomID, userA, userB, currentWord, currentTurn;
    String isStart, stateGuest;

    public Room() {
    }

    public Room(String uidHost, String uidGuest, String roomID, String userA, String userB, String currentWord, String currentTurn, String isStart, String stateGuest) {
        this.uidHost = uidHost;
        this.uidGuest = uidGuest;
        this.roomID = roomID;
        this.userA = userA;
        this.userB = userB;
        this.currentWord = currentWord;
        this.currentTurn = currentTurn;
        this.isStart = isStart;
        this.stateGuest = stateGuest;
    }

    public String getIsStart() {
        return isStart;
    }

    public void setIsStart(String isStart) {
        this.isStart = isStart;
    }

    public String getStateGuest() {
        return stateGuest;
    }

    public void setStateGuest(String stateGuest) {
        this.stateGuest = stateGuest;
    }

    public String getUidGuest() {
        return uidGuest;
    }

    public void setUidGuest(String uidGuest) {
        this.uidGuest = uidGuest;
    }

    public String getUidHost() {
        return uidHost;
    }

    public void setUidHost(String uidHost) {
        this.uidHost = uidHost;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getUserA() {
        return userA;
    }

    public void setUserA(String userA) {
        this.userA = userA;
    }

    public String getUserB() {
        return userB;
    }

    public void setUserB(String userB) {
        this.userB = userB;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(String currentTurn) {
        this.currentTurn = currentTurn;
    }
}
