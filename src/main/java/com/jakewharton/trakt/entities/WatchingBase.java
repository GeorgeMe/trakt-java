package com.jakewharton.trakt.entities;

import com.jakewharton.trakt.enumerations.ActivityAction;
import com.jakewharton.trakt.enumerations.ActivityType;

public class WatchingBase {

    public ActivityType type;
    public ActivityAction action;
    public Movie movie;
    public TvShow show;
    public TvShowEpisode episode;

}
