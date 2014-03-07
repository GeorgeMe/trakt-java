package com.jakewharton.trakt.services;

import com.google.gson.annotations.SerializedName;

import com.jakewharton.trakt.entities.Response;

import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Endpoints for Account.
 */
public interface AccountService {

    /**
     * Create a new trakt account. Username and e-mail must be unique and not already exist in
     * trakt.
     */
    @POST("/account/create/{apikey}")
    Response create(
            @Body NewAccount account
    );

    /**
     * Returns all settings for the authenticated user. Use these settings to customize your app
     * based on the user's settings. For example, if they use advanced ratings show a 10 heart
     * scale. If they prefer simple ratings, show the binary scale. The social connections are also
     * useful to customize the checkin prompt.
     */
    @POST("/account/settings/{apikey}")
    Settings settings();

    /**
     * Test trakt credentials. This is useful for your configuration screen and is a simple way to
     * test someone's trakt account.
     */
    @POST("/account/test/{apikey}")
    Response test();

    public static class NewAccount {
        public String username;
        public String password;
        public String email;

        public NewAccount(String username, String passwordSha1Hash, String email) {
            this.username = username;
            this.password = passwordSha1Hash;
            this.email = email;
        }
    }

    static class Settings extends Response {
        public Profile profile;
        public Account account;
        public Viewing viewing;
        public Connections connections;
        public SharingText sharing_text;

        public  static class Profile {
            public String username;
            public String full_name;
            public String gender;
            public Integer age
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

            public Facebook facebook;

            public Twitter twitter;

            public Tumblr tumblr;

            public Path path;

            public Prowl prowl;

            public static class Facebook {

                public  boolean connected;
            }

            public static class Twitter {

                public boolean connected;
            }

            public static class Tumblr {

                public boolean connected;
            }

            public static class Path {

                public boolean connected;
            }

            public static class Prowl {

                public boolean connected;
            }
        }

        public static class SharingText {

            public String watching;

            public String watched;
        }
    }


}
