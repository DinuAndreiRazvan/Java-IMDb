import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public abstract class User implements Comparable<User>, Observer{
    private String username;
    private int experience;
    private AccountType userType;
    private List<String> notifications;
    private SortedSet<Actor> favoriteActors;
    private SortedSet<Production> favoriteProductions;

    static class Information {
        Credentials credentials;
        String name;
        String country;
        Long age;
        String gender;
        String birthDate;
        private Information(Builder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.gender = builder.gender;
            this.birthDate = builder.birthDate;
        }

        public void printInformation () {
            System.out.println("Name : " + this.name);
            if (country != null)
                System.out.println("Country : " + this.country);
            if (age != null)
                System.out.println("Age : " + this.age);
            if (gender != null)
                System.out.println("Gender : " + this.gender);
            if (birthDate != null)
                System.out.println("BirthDate : " + this.birthDate);
        }
        public static class Builder {
            Credentials credentials;
            String name;
            String country;
            Long age;
            String gender;
            String birthDate;

            public Builder(String email, String password, String name) {
                this.credentials = new Credentials(email, password);
                this.name = name;
            }

            public Builder Country(String country) {
                this.country = country;
                return this;
            }
            public Builder Age(Long age) {
                this.age = age;
                return this;
            }
            public Builder Gender(String gender) {
                this.gender = gender;
                return this;
            }
            public Builder BirthDate(String birthDate) {
                this.birthDate = birthDate;
                return this;
            }

            public Information build() {
                return new Information(this);
            }
        }
    }
    Information information;

    public int logOut(User user, BufferedReader reader) {
        int option;
        String string;
        try {
            System.out.print("Options:\n    1) Exit\n    2) Login again\nYour choice : ");
            string = reader.readLine();
            option = Integer.parseInt(string);
            if (option == 1) {
                return 1;
            } else if (option == 2) {
                return 2;
            } else {
                System.out.println("Invalid option !");
                return 0;
            }
        } catch (IOException e) {
            System.err.println("Production - IO exception");
            return 0;
        }
    }

    public void user_set(String username, int experience, AccountType userType) {
        this.username = username;
        this.experience = experience;
        this.points = experience * 50;
        this.userType = userType;
    }

    public User() {
        this.points = 0;
        this.notifications = new ArrayList<String>();
        this.favoriteProductions = new TreeSet<Production>();
        this.favoriteActors = new TreeSet<Actor>();
        this.requestsSent = new TreeMap<>();
        this.ratingMap = new TreeMap<>();
    }

    public void printNotifications() {
        // if there is something
        if(!notifications.isEmpty()) {
            System.out.println("------------ Notifications ------------");
            for (String x : notifications) {
                System.out.println(x);
            }
            System.out.println();
        } else {
            System.out.println("No notifications available\n");
        }
    }
    public void printFavorites() {
        int ok = 0;
        // if there is something
        if(!favoriteActors.isEmpty()) {
            System.out.println("------------ Favorite Actors ------------");
            for (Actor actor : favoriteActors) {
                System.out.println("    " + actor.name);
                ok = 1;
            }
        }
        // if there is something
        if(!favoriteProductions.isEmpty()) {
            System.out.println("---------- Favorite Productions ----------");
            for (Production production : favoriteProductions) {
                System.out.println("    " + production.title);
                ok = 1;
            }
        }
        if (ok == 0) {
            System.out.println("\nNo favorite items");
        }
    }

    public void printUser() {
        System.out.printf("_____________________%s_____________________\n", this.username);
        System.out.println("Experience : " + this.experience);
        information.printInformation();
        System.out.println("User Type : " + this.userType);
        System.out.println();
        // if there is something
        if(!notifications.isEmpty()) {
            System.out.println("------------ Notifications ------------");
            for (String x : notifications) {
                System.out.println(x);
            }
            System.out.println();
        }
        // if there is something
        if(!favoriteActors.isEmpty()) {
            System.out.println("------------ Favorite Actors ------------");
            for (Actor actor : favoriteActors) {
                actor.printActor();
            }
        }
        // if there is something
        if(!favoriteProductions.isEmpty()) {
            System.out.println("---------- Favorite Productions ----------");
            for (Production production : favoriteProductions) {
                production.displayInfo();
                production.displayInfoProduction();
            }
        }
    }

    public void addFavoriteActor(String name) {
        if (name == null) {
            System.err.println("Null Argument");
            return;
        }
        IMDB imdb = IMDB.getInstance();
        for(Actor actor : imdb.actors) {
            if (actor.name.compareTo(name) == 0) {
                addFavoriteActor(actor);
            }
        }
    }
    public void addFavoriteActor(Actor a) {
        if (a == null) {
            System.err.println("Null Argument");
            return;
        }
        this.favoriteActors.add(a);
    }
    public void removeFavoriteActor(Actor a) {
        if(a == null) {
            System.err.println("Null Argument");
            return;
        }
        this.favoriteActors.remove(a);
    }
    public void removeFavoriteActor(String name) {
        if(name == null) {
            System.err.println("Null Argument");
            return;
        }
        for(Actor actor : favoriteActors) {
            if (actor.name.compareTo(name) == 0) {
                this.favoriteActors.remove(actor);
                return;
            }
        }
    }

    public void addFavoriteProduction(String title) {
        if (title == null) {
            System.err.println("Null Argument");
            return;
        }
        IMDB imdb = IMDB.getInstance();
        for(Movie movie : imdb.movies) {
            if (movie.title.compareTo(title) == 0) {
                addFavoriteProduction(movie);
            }
        }
        for(Series series : imdb.series) {
            if (series.title.compareTo(title) == 0) {
                addFavoriteProduction(series);
            }
        }
    }
    public void addFavoriteProduction(Production p) {
        if (p == null) {
            System.err.println("Null Argument");
            return;
        }
        this.favoriteProductions.add(p);
    }
    public void removeFavoriteProduction(Production p) {
        if (p == null) {
            System.err.println("Null Argument");
            return;
        }
        this.favoriteProductions.remove(p);
    }
    public void removeFavoriteProduction(String title) {
        if (title == null) {
            System.err.println("Null Argument");
            return;
        }
        for (Production production : favoriteProductions) {
            if (production.title.compareTo(title) == 0) {
                favoriteProductions.remove(production);
                return;
            }
        }
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public int getExperience() {
        return experience;
    }

    public AccountType getUserType() {
        return userType;
    }
    public void setUserType(AccountType userType) {
        this.userType = userType;
    }

    public List<String> getNotifications() {
        return notifications;
    }
    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }

    public SortedSet<Actor> getFavoriteActors() {
        return favoriteActors;
    }
    public void setFavoriteActors(SortedSet<Actor> favoriteActors) {
        this.favoriteActors = favoriteActors;
    }

    public SortedSet<Production> getFavoriteProductions() {
        return favoriteProductions;
    }

    public void setFavoriteProductions(SortedSet<Production> favoriteProductions) {
        this.favoriteProductions = favoriteProductions;
    }

    public int compareTo(User user) {
        if(user == null) {
            System.out.println("Null argument");
            return 1;
        }
        return this.username.compareTo((user.getUsername()));
    }

    // Observer Pattern - Observer
    Map<Integer, Request> requestsSent;
    Map<Integer, Rating> ratingMap; // placed ratings

    public void update(String message) {
        notifications.add(message);
    }

    public void printRequestsSent() {
        if(requestsSent.isEmpty()) {
            System.out.println("No Sent Requests available");
            return;
        }
        System.out.println("\n-------- Sent Requests --------");
        for (Map.Entry<Integer,  Request> entry : requestsSent.entrySet()) {
            Request request = entry.getValue();
            request.printRequest();
        }
    }
    public void printRequestsSentIndex() {
        if(requestsSent.isEmpty()) {
            System.out.println("No Sent Requests available");
            return;
        }
        for (Map.Entry<Integer,  Request> entry : requestsSent.entrySet()) {
            Request request = entry.getValue();
            request.printRequest();
        }
    }

    //      Strategy Pattern
    private int points;
    public void addExperiencePoints(int points) {
        if( this.experience != 10 && this.points < 500) {
            this.points += points;
            this.experience = this.points / 50;
        }
    }
}