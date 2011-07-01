package com.jakewharton.trakt.services;

import java.util.Calendar;
import java.util.List;
import com.jakewharton.trakt.BaseTestCase;
import com.jakewharton.trakt.entities.CalendarDate;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.entities.TvShowEpisode;
import com.jakewharton.trakt.entities.TvShowSeason;
import com.jakewharton.trakt.entities.CalendarDate.CalendarTvShowEpisode;

public class UserServiceTest extends BaseTestCase {
	public void test_calendarShows() {
		Calendar d_2002_09_22 = Calendar.getInstance();
		d_2002_09_22.set(2004, 8, 22, 0, 0, 0);
		
		List<CalendarDate> dates = getManager().userService().calendarShows("JakeWharton").date(d_2002_09_22.getTime()).days(1).fire();
		assertNotNull("Result list was null.", dates);
		assertFalse("Result list was empty.", dates.isEmpty());
		
		CalendarDate date = dates.get(0);
		assertNotNull("First result was null.", date);
		
		List<CalendarTvShowEpisode> dateEpisodes = date.getEpisodes();
		assertNotNull("Date episode list was null.", dateEpisodes);
		assertFalse("Date episode list was empty.", dateEpisodes.isEmpty());
		
		CalendarTvShowEpisode dateEpisode = dateEpisodes.get(0);
		assertNotNull("Date episode was null.", dateEpisode);
		
		TvShowEpisode showEpisode = dateEpisode.getEpisode();
		assertNotNull("Show episode was null.", showEpisode);
		assertNotNull("Show season was null.", showEpisode.getSeason());
		assertEquals("Show season does not match.", showEpisode.getSeason().intValue(), 1);
		assertNotNull("Show number was null.", showEpisode.getNumber());
		assertEquals("Show number does not match.", showEpisode.getNumber().intValue(), 1);
		assertNotNull("Show title was null.", showEpisode.getTitle());
		assertEquals("Show title does not match.", showEpisode.getTitle(), "Pilot (1)");
		
		TvShow show = dateEpisode.getShow();
		assertNotNull("Show was null.", show);
		assertNotNull("Show title was null.", show.getTitle());
		assertEquals("Show title does not match.", show.getTitle(), "Lost");
	}
	
	public void test_libraryShowsWatched() {
		List<TvShow> shows = getManager().userService().libraryShowsWatched("JakeWharton").fire();
		assertNotNull("Result list was null.", shows);
		assertFalse("Result list was empty.", shows.isEmpty());
		
		TvShow show = shows.get(0);
		assertNotNull("First result was null.", show);
		assertNotNull("Show title was null.", show.getTitle());
		assertEquals("Show title does not match.", show.getTitle(), "South Park");
		assertNotNull("Show year was null.", show.getYear());
		assertEquals("Show year does not match.", show.getYear().intValue(), 1997);
		assertNotNull("Show URL was null.", show.getUrl());
		assertNotNull("Show IMDB ID was null.", show.getImdbId());
		assertEquals("Show IMDB ID does not match.", show.getImdbId(), "tt0121955");
		assertNotNull("Show TVDB ID was null.", show.getTvdbId());
		assertEquals("Show TVDB ID does not match.", show.getTvdbId(), "75897");
		assertNotNull("Show TV Rage ID was null.", show.getTvRageId());
		assertEquals("Show TV Rage ID does not match.", show.getTvRageId(), "5266");
		assertNotNull("Show images was null.", show.getImages());
		assertNotNull("Show poster image was null.", show.getImages().getPoster());
		assertNotNull("Show fanart image was null.", show.getImages().getFanart());
		
		List<TvShowSeason> seasons = show.getSeasons();
		assertNotNull("Show seasons list was null.", seasons);
		assertFalse("Show seasons list was empty.", seasons.isEmpty());
		
		TvShowSeason season = seasons.get(0);
		assertNotNull("Show first season was null.", season);
		assertNotNull("Show season number was null.", season.getSeason());
		
		TvShowSeason.Episodes seasonEpisodes = season.getEpisodes();
		assertNotNull("Show episodes was null.", seasonEpisodes);
		assertNotNull("Show episodes list was null.", seasonEpisodes.getNumbers());
		assertFalse("Show episodes list was empty.", seasonEpisodes.getNumbers().isEmpty());
	}
}
