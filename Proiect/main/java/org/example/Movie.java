import java.util.ArrayList;
import java.util.List;

public class Movie extends Production {
    String duration;
    Long releaseYear;

    // constructor
    public Movie(String title, List<String> directors, List<String> actors, List<Genre> genres,
                 List<Rating> ratings, String plot, Double averageRating, String duration, Long releaseYear) {
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
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    public void displayInfo() {
        System.out.println("Duration : " + this.duration);
        System.out.println("Launch : " + this.releaseYear);
        System.out.println();
    }
}