package com.jakewharton.trakt.services;

import java.util.List;
import com.jakewharton.trakt.entities.Movie;

import retrofit.http.GET;
import retrofit.http.Path;

public interface MoviesService {

    @GET("/movies/trending.json/{apikey}")
    List<Movie> trending();

    @GET("/movies/updated.json/{apikey}/{timestamp}")
    List<Movie> updated(
            @Path("timestamp") long timestamp
    );

}
