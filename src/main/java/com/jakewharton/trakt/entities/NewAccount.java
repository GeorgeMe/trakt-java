package com.jakewharton.trakt.entities;

public class NewAccount {
    public String username;
    public String password;
    public String email;

    public NewAccount(String username, String passwordSha1Hash, String email) {
        this.username = username;
        this.password = passwordSha1Hash;
        this.email = email;
    }
}
