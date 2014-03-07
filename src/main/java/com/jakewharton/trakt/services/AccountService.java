package com.jakewharton.trakt.services;

import com.jakewharton.trakt.entities.NewAccount;
import com.jakewharton.trakt.entities.Response;
import com.jakewharton.trakt.entities.Settings;

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


}
