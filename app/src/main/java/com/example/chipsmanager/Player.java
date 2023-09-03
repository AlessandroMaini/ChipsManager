package com.example.chipsmanager;

public class Player {
    private int balance;
    private String name;
    private int bet;

    public Player() {
    }

    public Player(int balance, String name, int bet) {
        this.balance = balance;
        this.name = name;
        this.bet = bet;
    }

    public int getBalance() {
        return balance;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
