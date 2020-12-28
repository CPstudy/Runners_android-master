package com.guk2zzada.runnerswar;

public class RoomList {
    String entry1;
    String entry2;
    String entry1_dist;
    String entry2_dist;
    String entry1_score;
    String entry2_score;
    String updatedAt;
    String createdAt;
    String id;
    boolean end_game;

    public RoomList(String entry1, String entry2, String entry1_dist, String entry2_dist, String entry1_score, String entry2_score, String updatedAt, String createdAt, String id, boolean end_game) {
        this.entry1 = entry1;
        this.entry2 = entry2;
        this.entry1_dist = entry1_dist;
        this.entry2_dist = entry2_dist;
        this.entry1_score = entry1_score;
        this.entry2_score = entry2_score;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.id = id;
        this.end_game = end_game;
    }
}
