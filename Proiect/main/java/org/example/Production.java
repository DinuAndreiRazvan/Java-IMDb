import javax.swing.text.html.HTMLDocument;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public abstract class Production implements Comparable<Object>, ExperienceStrategy {
    String title;
    List<String> directors;
    List<String> actors;
    List<Genre> genres;
    List<Rating> ratings;
    String plot;
    double averageRating;

    public abstract void displayInfo();

    @Override public int compareTo(Object o) {
        // check the address
        if (this == o)
            return 0;
        if (o == null)
            return 1;
        // cast the object
        Production obj = (Production)o;

        return this.title.compareTo(obj.title);
    }

    public void calculateAverageRating() {
        double sum = 0;
        for (Rating rating : this.ratings) {
            sum += rating.evaluation;
        }
        this.averageRating = sum / this.ratings.size();
    }

    public void displayInfoProduction() {
        int i;
        System.out.println("Title : " + this.title);
        System.out.println("Description : " + this.plot);
        System.out.printf("Average Rating : %.2f\n", this.averageRating);
        System.out.print("Directors : ");
        for(i = 0; i < directors.size(); i++) {
            System.out.print(directors.get(i));
            if (i != directors.size() - 1)
                System.out.print("; ");
            else
                System.out.print("\n");
        }

        System.out.print("Actors : ");
        for(i = 0; i < actors.size(); i++) {
            System.out.print(actors.get(i));
            if (i != actors.size() - 1)
                System.out.print("; ");
            else
                System.out.print("\n");
        }

        System.out.print("Genre : ");
        for(i = 0; i < genres.size(); i++) {
            System.out.print(genres.get(i));
            if (i != genres.size() - 1)
                System.out.print("; ");
            else
                System.out.print("\n");
        }

        if (ratings.isEmpty()) {
            System.out.println("Ratings : None");
            return;
        }
        System.out.println("--- Ratings ---");
        for (Rating rating : ratings)
            rating.printRating();
    }

    //      Strategy Pattern
    @Override
    public int calculateEXP() {
        return 2;
    }
}
