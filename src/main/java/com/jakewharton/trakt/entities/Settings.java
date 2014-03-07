package com.jakewharton.trakt.entities;

import com.google.gson.annotations.SerializedName;

import com.jakewharton.trakt.services.AccountService;

public class Settings extends Response {

    public Profile profile;
    public Account account;
    public Viewing viewing;
    public Connections connections;
    public SharingText sharing_text;

    public static class Profile {
        public String username;
        public String full_name;
        public String gender;
        public Integer age;
        public String location;
        public String about;
        public int joined;
        public int last_login;
        public String avatar;
        public String url;
        public boolean vip;
    }

    public static class Account {

        /**
         * <a href="http://go.trakt.tv/HX7SfQ">Info about timezone descriptors.</a>
         */
        public String timezone;

        public boolean use_24hr;

        @SerializedName("protected")
        public boolean _protected;
    }

    public static class Viewing {

        public Ratings ratings;

        public Shouts shouts;

        public static class Ratings {

            /**
             * simple, advanced
             */
            public String mode;
        }

        public static class Shouts {

            public boolean show_badges;

            public boolean show_spoilers;
        }
    }

    public static class Connections {
        public SocialConnection facebook;
        public SocialConnection twitter;
        public SocialConnection tumblr;
        public Connection path;
        public Connection prowl;

        public static class Connection {
            public boolean connected;
        }

        public static class SocialConnection extends Connection {
            public boolean share_scrobbles_start;
            public boolean share_scrobbles_end;
            public boolean share_tv;
            public boolean share_movies;
            public boolean share_ratings;
            public boolean share_checkins;
        }
    }

    public static class SharingText {

        public String watching;

        public String watched;
    }
}
