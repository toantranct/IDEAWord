package com.ideastudio.ideaword.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class RoomV2 {
    Map<String, Boolean> ready = new HashMap<>();
    Map<String, Object> roomInfo = new HashMap<>();
    String word;
    String turn;
    String winUser;


    public RoomV2() {
    }

    public RoomV2(Map<String, Boolean> ready, Map<String, Object> roomInfo, String word, String turn, String winUser) {
        this.ready = ready;
        this.roomInfo = roomInfo;
        this.word = word;
        this.turn = turn;
        this.winUser = winUser;
    }

    public String getWinUser() {
        return winUser;
    }

    public void setWinUser(String winUser) {
        this.winUser = winUser;
    }

    public Map<String, Boolean> getReady() {
        return ready;
    }

    public void setReady(Map<String, Boolean> ready) {
        this.ready = ready;
    }


    public Map<String, Object> getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(Map<String, Object> roomInfo) {
        this.roomInfo = roomInfo;
    }

    public String getWord() {
        return !word.equals("") ? word : "";
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getRoomID() {
        return roomInfo.get("roomID").toString();
    }

    public String getPlayer1ID() {
        Map<String, String> playerIDs = (Map<String, String>) roomInfo.get("playerIDs");
        return playerIDs.get("player1ID") != "" ?  playerIDs.get("player1ID") : null;
    }

    public String getPlayer1User() {
        Map<String, String> playerIDs = (Map<String, String>) roomInfo.get("playerIDs");
        return playerIDs.get("player1User") != "" ?  playerIDs.get("player1User") : null;
    }

    public String getPlayer2ID() {
        Map<String, String> playerIDs = (Map<String, String>) roomInfo.get("playerIDs");
        return playerIDs.get("player2ID") != "" ?  playerIDs.get("player2ID") : null;
    }

    public String getPlayer2User() {
        Map<String, String> playerIDs = (Map<String, String>) roomInfo.get("playerIDs");
        return playerIDs.get("player2User") != "" ?  playerIDs.get("player2User") : null;
    }
    public void setPlayerIDs(Map<String, String> value) {
        roomInfo.replace("playerIDs", value);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> rs = new HashMap<>();
        rs.put("roomInfo", roomInfo);
        rs.put("ready", ready);
        rs.put("word", word);
        rs.put("turn", turn);
        rs.put("winUser", winUser);

        return rs;
    }

}
