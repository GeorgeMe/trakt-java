
package com.jakewharton.trakt.services;

import com.jakewharton.trakt.entities.CheckinResponse;
import com.jakewharton.trakt.entities.Comment;
import com.jakewharton.trakt.entities.Response;
import com.jakewharton.trakt.entities.Share;
import com.jakewharton.trakt.entities.Stats;

import java.util.ArrayList;
import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Endpoints for Movie.
 */
public interface MovieService {

    /**
     * Notify trakt that a user wants to cancel their current check in. <br/> <br/>
     * <em>Warning</em>: This method requires a developer API key.
     */
    @POST("/movie/cancelcheckin/{apikey}")
    Response cancelcheckin();

    /**
     * Notify Trakt that a user has stopped watching a movie. <br/> <br/> <em>Warning</em>: This
     * method requires a developer API key.
     */
    @POST("/movie/cancelwatching/{apikey}")
    Response cancelwatching();

    /**
     * Check into a movie on trakt. Think of this method as in between a seen and a scrobble. After
     * checking in, the trakt will automatically display it as watching then switch over to watched
     * status once the duration has elapsed.
     */
    @POST("/movie/checkin/{apikey}")
    CheckinResponse checkin(
            @Body MovieCheckin checkin
    );

    /**
     * Returns all comments (shouts and reviews) for a movie. Most recent comments returned first.
     */
    @GET("/movie/comments.json/{apikey}/{title}")
    List<Comment> comments(
            @Path("title") int tmdbId
    );

    /**
     * Returns all comments (shouts and reviews) for a movie. Most recent comments returned first.
     *
     * @param type Set to all (default), shouts, or reviews.
     */
    @GET("/movie/comments.json/{apikey}/{title}/{type}")
    List<Comment> comments(
            @Path("title") int tmdbId,
            @Path("type") String type
    );

    /**
     * Returns all comments (shouts and reviews) for a movie. Most recent comments returned first.
     */
    @GET("/movie/comments.json/{apikey}/{title}")
    List<Comment> comments(
            @Path("title") String imdbIdOrSlug
    );

    /**
     * Returns all comments (shouts and reviews) for a movie. Most recent comments returned first.
     *
     * @param type Set to all (default), shouts, or reviews.
     */
    @GET("/movie/comments.json/{apikey}/{title}/{type}")
    List<Comment> comments(
            @Path("title") String imdbIdOrSlug,
            @Path("type") String type
    );

    @POST("/movie/seen/{apikey}")
    void seen(
            @Body Movies movies
    );

    @POST("/movie/library/{apikey}")
    void library(
            @Body Movies movies
    );

    @GET("/movie/stats.json/{apikey}/{title}")
    Stats stats(
            @Path("title") int tmdbId
    );

    @GET("/movie/stats.json/{apikey}/{title}")
    Stats stats(
            @Path("title") String imdbIdOrslug
    );

    @GET("/movie/summary.json/{apikey}/{title}")
    com.jakewharton.trakt.entities.Movie summary(
            @Path("title") String imdbIdOrSlug
    );

    @GET("/movie/summary.json/{apikey}/{title}")
    com.jakewharton.trakt.entities.Movie summary(
            @Path("title") int tmdbId
    );

    public static class MovieCheckin extends Movie {

        public Integer duration;

        public Integer venue_id;

        public String venue_name;

        public Share share;

        public String message;

        public String app_version;

        public String app_date;

        /**
         * @param appVersion Version number of the app, be as specific as you can including nightly
         *                   build number, etc. Used to help debug.
         * @param appDate    Build date of the media center. Used to help debug.
         */
        public MovieCheckin(String imdbId, String appVersion, String appDate) {
            super(imdbId);
            this.app_version = appVersion;
            this.app_date = appDate;
        }

        public MovieCheckin(String imdbId, String message, String appVersion, String appDate) {
            this(imdbId, appVersion, appDate);
            this.message = message;
        }
    }

    public static class Movie {

        public String imdb_id;

        public Integer tmdb_id;

        public String title;

        public Integer year;

        public Movie(String imdbId) {
            this.imdb_id = imdbId;
        }

        public Movie(int tmdbId) {
            this.tmdb_id = tmdbId;
        }

        public Movie(String title, int year) {
            this.title = title;
            this.year = year;
        }
    }

    public static class SeenMovie extends Movie {

        public String last_played;


        public SeenMovie(String imdbId) {
            super(imdbId);
        }

        public SeenMovie(int tmdbId) {
            super(tmdbId);
        }
    }

    public static class Movies {

        public List<SeenMovie> movies;

        public Movies(List<SeenMovie> seenMovies) {
            movies = seenMovies;
        }

        public Movies(SeenMovie seenMovie) {
            movies = new ArrayList<SeenMovie>();
            movies.add(seenMovie);
        }
    }


}
