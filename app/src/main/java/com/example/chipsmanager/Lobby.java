package com.example.chipsmanager;

import java.util.List;

public class Lobby {
    private List<Player> players;
    private int number_players;

    public Lobby() {
    }

    public Lobby(List<Player> players, int number_players) {
        this.players = players;
        this.number_players = number_players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getNumber_players() {
        return number_players;
    }

    public void setNumber_players(int number_players) {
        this.number_players = number_players;
    }
}
