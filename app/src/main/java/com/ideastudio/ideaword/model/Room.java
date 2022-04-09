package com.ideastudio.ideaword.model;

public class Room {
    String roomID, userA, userB, currentWord, currentTurn;

    public Room() {
    }

    public Room(String roomID, String userA, String userB, String currentWord, String currentTurn) {
        this.roomID = roomID;
        this.userA = userA;
        this.userB = userB;
        this.currentWord = currentWord;
        this.currentTurn = currentTurn;
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
