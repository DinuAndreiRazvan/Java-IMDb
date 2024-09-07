import java.util.*;

public class Series extends Production{
    Long releaseYear;
    Long numSeasons;
    private Map<String, List<Episode>> seasons;

    // constructor
    public Series(String title, List<String> directors, List<String> actors, List<Genre> genres,
                  List<Rating> ratings, String plot, Double averageRating, Long releaseYear,
                  Long numSeasons, Map<String, List<Episode>> seasons) {
        this.title = title;

        this.directors = new ArrayList<String>();
        this.directors.addAll(directors);

        this.actors = new ArrayList<String>();
        this.actors.addAll(actors);

        this.genres = new ArrayList<Genre>();
        this.genres.addAll(genres);

        this.ratings = new ArrayList<Rating>();
        this.ratings.addAll(ratings);

        this.plot = plot;
        this.averageRating = averageRating;
        this.releaseYear = releaseYear;
        this.numSeasons = numSeasons;

        this.seasons = new TreeMap<String, List<Episode>>();
        this.seasons.putAll(seasons);
    }

    public void addMap(String key, List<Episode> episodeList) {
        this.seasons.put(key, episodeList);
    }

    public void removeMap(String key) {
        this.seasons.remove(key);
    }

    public void addEpisode(String key, Episode episode) {
        this.seasons.get(key).add(episode);
    }
    public void removeEpisode(String key, String episodeName) {
        List<Episode> episodeList = this.seasons.get(key);
        for (Episode episode : episodeList) {
            if (episode.episodeName.compareTo(episodeName) == 0) {
                this.seasons.get(key).remove(episode);
                return;
            }
        }
    }

    public void modifyEpisodeName(String key, String episodeName, String newName) {
        List<Episode> episodeList = this.seasons.get(key);
        for (Episode episode : episodeList) {
            if (episode.episodeName.compareTo(episodeName) == 0) {
                episode.episodeName = newName;
                return;
            }
        }
    }
    public void modifyEpisodeDuration(String key, String episodeName, String newDuration) {
        List<Episode> episodeList = this.seasons.get(key);
        for (Episode episode : episodeList) {
            if (episode.episodeName.compareTo(episodeName) == 0) {
                episode.duration = newDuration;
                return;
            }
        }
    }

    public void displayInfo() {
        System.out.println("Launch : " + this.releaseYear);
        System.out.println("Seasons : " + this.numSeasons);

        for (Map.Entry<String, List<Episode>> s : seasons.entrySet()) {
            System.out.println(s.getKey());
            List<Episode> episodeList = s.getValue();

            for (Episode episode : episodeList) {
                episode.printInfo();
            }
        }
        System.out.println();
    }
}