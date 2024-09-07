import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ClassLoadingMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static sun.security.util.Password.readPassword;

public class IMDB {
    List<Regular> regulars;
    List<Contributor> contributors;
    List<Admin> admins;
    List<Actor> actors;
    List<Request> requests;
    List<Movie> movies;
    List<Episode> episodes;
    List<Series> series;

    // Make it Singleton
    private static IMDB instance = null;
    private IMDB(){
        regulars = new ArrayList<Regular>();
        contributors = new ArrayList<Contributor>();
        admins = new ArrayList<Admin>();
        actors = new ArrayList<Actor>();
        requests = new ArrayList<Request>();
        movies = new ArrayList<Movie>();
        episodes = new ArrayList<Episode>();
        series = new ArrayList<Series>();
    }
    public static IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }
        return instance;
    }

    public void run() {
        // Read data using BufferReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\nRunning");
        System.out.println("Loading Data...\n");

        // Get data form resources
        JsonSimpleRequest.readRequests("./resources/input/requests.json");
        JsonSimpleActors.readActor("./resources/input/actors.json");
        JsonSimpleProduction.readProduction("./resources/input/production.json");
        JsonSimpleUsers.readUser("./resources/input/accounts.json");

        // Login
        User user = Login(reader);
        // App running
        int ret;
        while (true) {
            displayOptions(user);
            if (getOption(user, reader) == 0) {
                ret = user.logOut(user, reader);
                if (ret == 1) {
                    // Exit
                    break;
                } else if (ret == 2) {
                    // Login again
                    user = Login(reader);;
                }
            }
        }
        System.out.println("\n      Until we meet again\n");
    }

    public User check_account (BufferedReader reader) {
        System.out.println("\nEnter your credentials");
        try {
            // read Credentials
            System.out.print("email : ");
            String email = reader.readLine();
            System.out.print("password : ");
            String password = reader.readLine();
            // check account
            for(Regular regular : this.regulars) {
                if(regular.information.credentials.getEmail().compareTo(email) == 0 &&
                        regular.information.credentials.getPassword().compareTo(password) == 0) {
                    System.out.println("\nWelcome back " + regular.information.name);
                    return regular;
                }
            }
            for(Contributor contributor : this.contributors) {
                if(contributor.information.credentials.getEmail().compareTo(email) == 0 &&
                        contributor.information.credentials.getPassword().compareTo(password) == 0) {
                    System.out.println("\nWelcome back " + contributor.information.name);
                    return contributor;
                }
            }
            for(Admin admin : this.admins) {
                if(admin.information.credentials.getEmail().compareTo(email) == 0 &&
                    admin.information.credentials.getPassword().compareTo(password) == 0) {
                    System.out.println("\nWelcome back " + admin.information.name);
                    return admin;
                }
            }
            return null;
        } catch (IOException e) {
            System.out.println("IO exception");
            return null;
        } catch (NumberFormatException e) {
            System.out.println("You wanna trick me? Put a number in there next time !\n");
            return null;
        }
    }

    public void displayOptions(User user) {
        System.out.println("\nChoose the index of your action :");
        System.out.println("    1) View Productions Details");
        System.out.println("    2) View Actors Details");
        System.out.println("    3) View Notifications");
        System.out.println("    4) Search for actor/movie/series");
        System.out.println("    5) Add/Delete/Show actor/movie/series from favorites");

        if (user.getUserType() == AccountType.Admin) {
            System.out.println("    6) Add/Delete actor/movie/series from system");
            System.out.println("    7) Update Movie/Series");
            System.out.println("    8) Update Actor");
            System.out.println("    9) Add/Delete user");
            System.out.println("   10) Solve request");
            System.out.println("   11) Logout");

        } else if (user.getUserType() == AccountType.Contributor) {
            System.out.println("    6) Add/Delete/Show Requests");
            System.out.println("    7) Add/Delete actor/movie/series from system");
            System.out.println("    8) Update Movie/Series");
            System.out.println("    9) Update Actor");
            System.out.println("   10) Solve request");
            System.out.println("   11) Logout");

        } else {
            System.out.println("    6) Add/Delete/Show Requests");
            System.out.println("    7) Add/Delete Rating");
            System.out.println("    8) Logout");
        }
        System.out.print("\nYour choice : ");
    }

    public int getOption(User user, BufferedReader reader) {
        int action;
        String string;
        try {
            // Get user option
            string = reader.readLine();
            action = Integer.parseInt(string);
            System.out.println();
            // Common Options for all users
            switch (action) {
                case 1 : /* Print Productions */
                    printProductions(reader);
                    return 1;
                case 2 : /* Print Actors */
                    printActors(reader);
                    return 1;
                case 3 : /* Print Notifications */
                    user.printNotifications();
                    return 1;
                case 4 : /* Search Actor/Movie/Series */
                    System.out.print("Enter the name/title : ");
                    String name = reader.readLine();
                    searchPrintItem(name);
                    return 1;
                case 5 : /* Add/Delete from Favorites */
                    AddDeletePrintFavorites(user, reader);
                    return 1;
            }

            if (user.getUserType() == AccountType.Admin) {
                Admin admin = ((Admin) user);
                switch (action) {
                    case 6 : /* Add/Delete actor/movie/series from system */
                        add_deleteActorProduction(admin, reader);
                        return 1;
                    case 7 : /* Update Movie/Series */
                        updateProduction(admin, reader);
                        return 1;
                    case 8 : /* Update Actor */
                        updateActor(admin, reader);
                        return 1;
                    case 9 : /* Add/Delete User */
                        add_deleteUser(admin, reader);
                        return 1;
                    case 10 : /* Solve request */
                        solveRequest(admin, reader);
                        return 1;
                    case 11 : /* Logout */
                        return 0;
                }

            } else if (user.getUserType() == AccountType.Contributor) {
                Contributor contributor = ((Contributor) user);
                switch (action) {
                    case 6 : /* Add/Delete/Print Request */
                        AddDeleteRequest(contributor, reader);
                        return 1;
                    case 7 : /* Add/Delete actor/movie/series from system */
                        add_deleteActorProduction(contributor, reader);
                        return 1;
                    case 8 : /* Update Movie/Series */
                        updateProduction(contributor, reader);
                        return 1;
                    case 9 : /* Update Actor */
                        updateActor(contributor, reader);
                        return 1;
                    case 10 : /* Solve request */
                        solveRequest(contributor, reader);
                        return 1;
                    case 11 : /* Logout */
                        return 0;
                }

            } else {
                Regular regular = ((Regular) user);
                switch (action) {
                    case 6 : /* Add/Delete/Print Request */
                        AddDeleteRequest(regular, reader);
                        return 1;
                    case 7 : /* Add/Delete Rating */
                        addRatingByUser(regular, reader);
                        return 1;
                    case 8 : /* Logout */
                        return 0;
                }
            }
        } catch (IOException e) {
            System.err.println("\nIO exception");
            return 1;
        } catch (NumberFormatException e) {
            System.out.println("\nYou wanna trick me? Put a good number in there next time !");
            return 1;
        } catch (IllegalArgumentException e) {
            System.err.println("\nItem does not exist");
            return 1;
        }
        System.out.println("\nPlease select one of the options !");
        return 1;
    }

    public User Login(BufferedReader reader) {
        User user = check_account(reader);
        while (user == null) {
            System.out.println("[ Account does not exist ]");
            user = check_account(reader);
        }
        return user;
    }

    // Print Things
    public void printProductions(BufferedReader reader) throws IOException, NumberFormatException, IllegalArgumentException {
        String string;
        int action, ok = 0;

        System.out.print("Filters :\n   1) None\n   2) By Genre\n   3) By Number of Ratings\nYour choice : ");
        string = reader.readLine();
        action = Integer.parseInt(string);

        if (action == 1) {
            // None
            System.out.println("-------------------------- Movies --------------------------");
            for (Movie movie : this.movies) {
                movie.displayInfoProduction();
                movie.displayInfo();
            }
            System.out.println("-------------------------- Series --------------------------");
            for (Series series_obj : this.series) {
                series_obj.displayInfoProduction();
                series_obj.displayInfo();
            }

        } else if (action == 2) {
            // by Genre
            System.out.print("Genre : ");
            string = reader.readLine();
            Genre genre = Genre.valueOf(string);

            System.out.println("-------------------------- Movies --------------------------");
            for (Movie movie : this.movies) {
                if(movie.genres.contains(genre)) {
                    movie.displayInfoProduction();
                    movie.displayInfo();
                    ok = 1;
                }
            }
            System.out.println("-------------------------- Series --------------------------");
            for (Series series_obj : this.series) {
                if(series_obj.genres.contains(genre)) {
                    series_obj.displayInfoProduction();
                    series_obj.displayInfo();
                    ok = 1;
                }
            }
            if (ok == 0) {
                System.out.println("\nProduction not found");
            }

        } else if (action == 3) {
            // by Number of Ratings
            System.out.print("Number : ");
            string = reader.readLine();
            int number = Integer.parseInt(string);

            System.out.println("-------------------------- Movies --------------------------");
            for (Movie movie : this.movies) {
                if(movie.ratings.size() == number) {
                    movie.displayInfoProduction();
                    movie.displayInfo();
                    ok = 1;
                }
            }
            System.out.println("-------------------------- Series --------------------------");
            for (Series series_obj : this.series) {
                if(series_obj.ratings.size() == number) {
                    series_obj.displayInfoProduction();
                    series_obj.displayInfo();
                    ok = 1;
                }
            }
            if (ok == 0) {
                System.out.println("\nProduction not found");
            }
        } else {
            System.out.println("\nInvalid Option");
        }
    }
    public void printActors(BufferedReader reader) throws IOException, NumberFormatException, IllegalArgumentException {
        int action;
        String string;

        System.out.print("Sorting Filters :\n   1) None\n   2) By name\nYour choice : ");
        string = reader.readLine();
        action = Integer.parseInt(string);

        if (action == 1) {
            // None
            System.out.println("-------------------------- Actors --------------------------");
            for (Actor actor : this.actors) {
                actor.printActor();
            }

        } else if (action == 2) {
            // By name
            SortedSet<Actor> sortedSet = new TreeSet<>(this.actors);
            System.out.println("-------------------------- Actors --------------------------");
            for (Actor actor : sortedSet) {
                actor.printActor();
            }

        } else {
            System.out.println("\nInvalid Option");
        }
    }

    // Favorites
    public void AddDeletePrintFavorites(User user, BufferedReader reader) throws IOException, NumberFormatException  {
        int option;
        String string;
        System.out.print("Options:\n    1) Add\n    2) Delete\n    3) Print Favorites\nYour choice: ");
        string = reader.readLine();
        option = Integer.parseInt(string);
        if (option == 3) {
            user.printFavorites();
        } else {
            System.out.print("Enter the name/title : ");
            string = reader.readLine();
            if (string.compareTo("") == 0) {/* Check input string */
                System.out.println("\nSeriously? Enter something next time :[");
                return;
            }

            if (option == 1) {
                user.addFavoriteProduction(string);
                user.addFavoriteActor(string);
            } else if (option == 2) {
                user.removeFavoriteProduction(string);
                user.removeFavoriteProduction(string);
            } else {
                System.out.println("Invalid option !");
            }
        }
    }

    // Requests
    public void AddDeleteRequest(User user, BufferedReader reader) throws IOException, NumberFormatException {
        int option;
        RequestTypes requestType;
        String string, to, description, date;
        Request request;

        System.out.print("Options:\n    1) Add\n    2) Delete\n    3) Print Requests\nYour choice: ");
        string = reader.readLine();
        option = Integer.parseInt(string);

        if (option == 3) {
            // Print Requests
            user.printRequestsSent();
        } else {
            if (option == 1) {
                // Add request
                System.out.print("To : ");
                to = reader.readLine();
                /* Check input string */
                if (to.compareTo("") == 0) {
                    System.out.println("\nSeriously? Enter something next time :[");
                    return;
                }

                System.out.print("Description : ");
                description = reader.readLine();
                /* Check input string */
                if (description.compareTo("") == 0) {
                    System.out.println("\nSeriously? Enter something next time :[");
                    return;
                }

                LocalDateTime Date = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                date = Date.format(formatter);

                System.out.print("Request Options:\n    1) Actor Issue\n    2) Movie Issue\n    3) Delete Account\n    4) Others\nYour choice: ");
                string = reader.readLine();
                option = Integer.parseInt(string);
                switch (option) {
                    case 1 :
                        requestType = RequestTypes.ACTOR_ISSUE;
                        System.out.print("Actor name : ");
                        string = reader.readLine();
                        /* Check input string */
                        if (string.compareTo("") == 0) {
                            System.out.println("\nSeriously? Enter something next time :[");
                            return;
                        }
                        // Check Actor name
                        if ( ! checkActor(string) ) {
                            System.out.print("\nInvalid Actor Name");
                            return;
                        }
                        request = new Request(requestType, date, user.getUsername(), string, to, description);
                        if (user.getUserType() == AccountType.Regular) {
                            ((Regular)user).addRequest(request);
                        } else {
                            ((Contributor)user).addRequest(request);
                        }
                        break;

                    case 2 :
                        requestType = RequestTypes.MOVIE_ISSUE;
                        System.out.print("Movie / Series title : ");
                        string = reader.readLine();
                        /* Check input string */
                        if (string.compareTo("") == 0) {
                            System.out.println("\nSeriously? Enter something next time :[");
                            return;
                        }
                        // Check title
                        if ( ! checkProduction(string) ) {
                            System.out.print("\nInvalid Movie / Series title");
                            return;
                        }

                        request = new Request(requestType, date, user.getUsername(), string, to, description);
                        if (user.getUserType() == AccountType.Regular) {
                            ((Regular)user).addRequest(request);
                        } else {
                            ((Contributor)user).addRequest(request);
                        }
                        break;

                    case 3 :
                        requestType = RequestTypes.DELETE_ACCOUNT;
                        request = new Request(requestType, date, user.getUsername(), to, description);
                        if (user.getUserType() == AccountType.Regular) {
                            ((Regular)user).addRequest(request);
                        } else {
                            ((Contributor)user).addRequest(request);
                        }
                        break;

                    case 4 :
                        requestType = RequestTypes.OTHERS;
                        request = new Request(requestType, date, user.getUsername(), to, description);
                        if (user.getUserType() == AccountType.Regular) {
                            ((Regular)user).addRequest(request);
                        } else {
                            ((Contributor)user).addRequest(request);
                        }
                        break;
                }

            } else if (option == 2) {
                // Remove Request
                int index;
                user.printRequestsSentIndex();
                System.out.print("Select the index of the Request : ");
                string = reader.readLine();
                /* Check input string */
                if (string.compareTo("") == 0) {
                    System.out.println("\nSeriously? Enter something next time :[");
                    return;
                }
                index = Integer.parseInt(string);
                if (user.getUserType() == AccountType.Regular) {
                    ((Regular)user).removeRequest(index);
                } else {
                    ((Contributor)user).removeRequest(index);
                }

            } else {
                System.out.println("\nInvalid option !");
            }
        }
    }
    void solveRequest(Staff staff, BufferedReader reader) throws IOException, NumberFormatException {
        int option;
        String string;

        System.out.print("Options:\n    1) Print Requests\n    2) Delete Request\nYour choice: ");
        string = reader.readLine();
        option = Integer.parseInt(string);

        if (option == 1) {
            // Print Request
            int i;
            for (i = 0; i < staff.requests.size(); i++) {
                staff.requests.get(i).printRequest(i + 1);
            }
            if (staff.getUserType() == AccountType.Admin) {
                RequestsHolder.printRequest(i + 1);
            } else if (i == 0) {
                System.out.println("\nNo Requests found");
            }
            return;

        } else if (option == 2) {
            // Delete Request
            int index;
            System.out.print("Index : ");
            string = reader.readLine();
            index = Integer.parseInt(string);
            index --;

            System.out.print("Request Status:\n    1) Resolved\n    2) Rejected !\nYour choice: ");
            string = reader.readLine();
            option = Integer.parseInt(string);
            RequestStatus requestStatus;
            if (option == 1) {
                requestStatus = RequestStatus.RESOLVED;
            } else if (option == 2) {
                requestStatus = RequestStatus.REJECTED;
            } else {
                System.out.println("\nInvalid Status");
                return;
            }

            if (staff.requests.size() <= index || index < 0) {
                index -= staff.requests.size();
                RequestsHolder.deleteRequest(index, requestStatus);
                return;
            }
            staff.requests.get(index).status = requestStatus;
            staff.requests.get(index).notifyObservers();
            staff.requests.remove(index);
            return;
        }
        System.out.println("\nInvalid Option");
    }

    // Ratings
    void addRatingByUser(User user, BufferedReader reader) throws IOException, NumberFormatException {
        Long evaluation;
        String title, comment, string;

        System.out.print("Movie / Series title : ");
        title = reader.readLine();

        System.out.print("Comment : ");
        comment = reader.readLine();

        System.out.print("Rating : ");
        string = reader.readLine();
        evaluation = Long.parseLong(string);

        Movie movie = searchMovie(title);
        Series series1 = searchSeries(title);
        if (movie != null) {
            ((Regular) user).addRating(movie, evaluation, comment);
        } else if (series1 != null) {
            ((Regular) user).addRating(series1, evaluation, comment);
        } else {
            System.out.println("\n  Nonexistent Production");
        }
    }

    // Add / Delete Item from System
    public void add_deleteActorProduction (Staff staff, BufferedReader reader)throws IOException, NumberFormatException {
        int option;
        String string;

        System.out.print("Options:\n    1) Add\n    2) Delete\nYour choice: ");
        string = reader.readLine();
        option = Integer.parseInt(string);

        if(option == 1) {
            addActorProduction(staff, reader);
        } else if (option == 2) {
            deleteActorProduction(staff, reader);
        } else {
            System.out.println("\nInvalid option");
        }
    }
    public void addActorProduction (Staff staff, BufferedReader reader) throws IOException, NumberFormatException{
        int option;
        String string;

        System.out.print("Item:\n    1) Actor\n    2) Movie\n   3) Series\nYour choice: ");
        string = reader.readLine();
        option = Integer.parseInt(string);

        if (option < 1 || option > 3) {
            System.out.println("\nInvalid Item");
            return;
        }

        if (option == 1) {
            /* ACTOR */
            String name, biography, title;
            Map<String, String> performances = new TreeMap<>();
            System.out.print("Actor name : ");
            name = reader.readLine();

            System.out.print("Actor biography : ");
            biography = reader.readLine();
            /* Check input string */
            if (biography.compareTo("") == 0 || name.compareTo("") == 0) {
                System.out.println("\nSeriously? Enter something next time :[");
                return;
            }

            //  Get performances
            while (true) {
                System.out.print("Performance title : ");
                title = reader.readLine();

                System.out.print("Performance type:\n    1) Movie\n    2) Series\nYour choice: ");
                string = reader.readLine();
                option = Integer.parseInt(string);

                /* Check input */
                if (string.compareTo("") == 0 || title.compareTo("") == 0 || (option != 1 && option != 2)) {
                    System.out.println("\nInvalid input");
                    return;
                } else if (option == 1) {
                    performances.put(title, "Movie");
                } else {
                    performances.put(title, "Series");
                }

                // Ask to continue
                System.out.print("Add more performances?\n    1) Yes\n    2) No\nYour choice: ");
                string = reader.readLine();
                option = Integer.parseInt(string);
                if (option == 2) {
                    break;
                } else if (option != 1) {
                    System.out.println("\nInvalid option");
                    return;
                }
            }
            // Build and add to System
            Actor actor = new Actor(name, biography, performances);
            staff.addActorSystem(actor);
            // Add to user contribution
            staff.addActorsContribution(actor);

        } else {
            /* PRODUCTION */
            String title;
            List<String> directors = new ArrayList<>();
            List<String> actors = new ArrayList<>();
            List<Genre> genres = new ArrayList<>();
            List<Rating> ratings = new ArrayList<>();
            String plot;
            double averageRating = 0;

            int value;

            System.out.print("Production title : ");
            title = reader.readLine();
            System.out.print("Description : ");
            plot = reader.readLine();
            /* Check input string */
            if (title.compareTo("") == 0 || plot.compareTo("") == 0) {
                System.out.println("\nSeriously? Enter something next time :[");
                return;
            }

            //  Get Directors
            while (true) {
                System.out.print("Director : ");
                string = reader.readLine();
                /* Check input */
                if (string.compareTo("") == 0) {
                    System.out.println("\nInvalid input");
                    return;
                } else {
                    directors.add(string);
                }
                // Ask to continue
                System.out.print("Add more?\n    1) Yes\n    2) No\nYour choice: ");
                string = reader.readLine();
                value = Integer.parseInt(string);
                if (value == 2) {
                    break;
                } else if (value != 1) {
                    System.out.println("\nInvalid option");
                    return;
                }
            }

            //  Get Actors
            while (true) {
                System.out.print("Actor : ");
                string = reader.readLine();
                /* Check input */
                if (string.compareTo("") == 0) {
                    System.out.println("\nInvalid input");
                    return;
                } else {
                    actors.add(string);
                }
                // Ask to continue
                System.out.print("Add more?\n    1) Yes\n    2) No\nYour choice: ");
                string = reader.readLine();
                value = Integer.parseInt(string);
                if (value == 2) {
                    break;
                } else if (value != 1) {
                    System.out.println("\nInvalid option");
                    return;
                }
            }

            //  Get Genres
            while (true) {
                System.out.print("Genres : ");
                string = reader.readLine();
                /* Check input */
                if (string.compareTo("") == 0) {
                    System.out.println("\nInvalid input");
                    return;
                } else {
                    try {
                        genres.add(Genre.valueOf(string));
                    } catch (IllegalArgumentException e) {
                        System.out.println("\nGender not suported");
                        return;
                    }
                }
                // Ask to continue
                System.out.print("Add more?\n    1) Yes\n    2) No\nYour choice: ");
                string = reader.readLine();
                value = Integer.parseInt(string);
                if (value == 2) {
                    break;
                } else if (value != 1) {
                    System.out.println("\nInvalid option");
                    return;
                }
            }

            // Release Year
            long releaseYear;
            System.out.print("Release Year : ");
            string = reader.readLine();
            releaseYear = Long.parseLong(string);

            if (option == 2) {
                /* MOVIE */
                String duration;
                System.out.print("Duration : ");
                duration = reader.readLine();

                if (duration.compareTo("") == 0 || (releaseYear < 1900 || releaseYear > 2024)) {
                    System.out.println("\nSeriously? Enter something next time :[");
                    return;
                }
                // Add to System
                Movie movie = new Movie(title, directors, actors, genres, ratings, plot, averageRating, duration, releaseYear);
                staff.addProductionSystem(movie);

            } else {
                /* SERIES */
                long numSeasons;
                Map<String, List<Episode>> seasons = new TreeMap<>();

                System.out.print("Number of seasons : ");
                string = reader.readLine();
                numSeasons = Long.parseLong(string);

                //  Get seasons
                for (int i = 1; i <= numSeasons; i++) {
                    System.out.println("----- Season " + i + " -----");
                    List<Episode> episodeList = new ArrayList<>();

                    // Get Episodes
                    while (true) {
                        String episodeName, duration;
                        System.out.print("Episode title : ");
                        episodeName = reader.readLine();

                        System.out.print("Episode duration : ");
                        duration = reader.readLine();

                        /* Check input */
                        if (duration.compareTo("") == 0 || episodeName.compareTo("") == 0) {
                            System.out.println("\nInvalid input");
                            return;
                        }
                        Episode episode = new Episode(episodeName, duration);
                        episodeList.add(episode);

                        // Ask to continue
                        System.out.print("Add more episodes?\n    1) Yes\n    2) No\nYour choice: ");
                        string = reader.readLine();
                        value = Integer.parseInt(string);
                        if (value == 2) {
                            break;
                        } else if (value != 1) {
                            System.out.println("\nInvalid option");
                            return;
                        }
                    }
                    seasons.put("Season " + i, episodeList);
                }
                // Add to System
                Series series1 = new Series(title, directors, actors, genres, ratings, plot, averageRating, releaseYear, numSeasons, seasons);
                staff.addProductionSystem(series1);
            }
        }
    }
    public void deleteActorProduction (Staff staff, BufferedReader reader) throws IOException, NumberFormatException{
        String name;

        System.out.print("Actor name OR Movie/Series title : ");
        name = reader.readLine();

        for(Actor actor : this.actors) {
            if (actor.name.compareTo(name) == 0) {
                staff.removeActorSystem(actor.name);
                return;
            }
        }
        staff.removeProductionSystem(name);
    }

    // Update Items from System
    public void updateProduction(Staff staff, BufferedReader reader) throws IOException, NumberFormatException {
        int option, update, operation;
        String string, title, aux;

        System.out.print("Production Title: ");
        title = reader.readLine();
        Production production = searchProduction(title);
        if (production == null) {
            System.out.println("No such Production");
            return;
        }
        if ( ! staff.checkPermissionProduction(title) ) {
            return;
        }


        if (searchMovie(title) != null) {
            option = 1;
        } else if (searchSeries(title) != null) {
            option = 2;
        } else {
            option = -1;
        }

        System.out.print("Update:\n    1) Directors\n    2) Actors\n    3) Genres\n    4) Plot\n    5) Release Year\n");
        if(option == 1) {
             System.out.print("    6) Duration\nYour choice: ");
        } else if (option == 2) {
            System.out.print("    6) Seasons\n    7) Episodes\nYour choice: ");
        } else {
            System.out.println("\nInvalid Production");
        }

        string = reader.readLine();
        update = Integer.parseInt(string);

        switch (update) {
            case 1: // Directors
                System.out.print("Operation:\n    1) Add\n    2) Delete\n    3) Modify\nYour choice: ");
                string = reader.readLine();
                operation = Integer.parseInt(string);

                switch (operation) {
                    case 1: // Add
                        System.out.print("New Director: ");
                        string = reader.readLine();
                        if(string.compareTo("") == 0) {
                            System.out.println("Invalid Input");
                            return;
                        }
                        production.directors.add(string);
                        return;

                    case 2: // Delete
                        System.out.print("Director: ");
                        string = reader.readLine();
                        if(string.compareTo("") == 0 || production.directors.contains(string)) {
                            System.out.println("Invalid Input");
                            return;
                        }
                        production.directors.remove(string);
                        return;

                    case 3: // Modify
                        System.out.print("Director: ");
                        aux = reader.readLine();

                        System.out.print("Modified Director: ");
                        string = reader.readLine();
                        if(string.compareTo("") == 0 || production.directors.contains(aux)) {
                            System.out.println("Invalid Input");
                            return;
                        }
                        production.directors.add(string);
                        production.directors.remove(aux);
                        return;
                }
                System.out.println("\nInvalid Operation");
                return;

            case 2: // Actors
                System.out.print("Operation:\n    1) Add\n    2) Delete\n    3) Modify\nYour choice: ");
                string = reader.readLine();
                operation = Integer.parseInt(string);

                switch (operation) {
                    case 1: // Add
                        System.out.print("New Actor: ");
                        string = reader.readLine();
                        if(string.compareTo("") == 0) {
                            System.out.println("Invalid Input");
                            return;
                        }
                        production.actors.add(string);
                        return;

                    case 2: // Delete
                        System.out.print("Actor: ");
                        string = reader.readLine();
                        if(string.compareTo("") == 0 || production.actors.contains(string)) {
                            System.out.println("Invalid Input");
                            return;
                        }
                        production.actors.remove(string);
                        return;

                    case 3: // Modify
                        System.out.print("Actor: ");
                        aux = reader.readLine();

                        System.out.print("Modified Actor: ");
                        string = reader.readLine();
                        if(string.compareTo("") == 0 || production.actors.contains(aux)) {
                            System.out.println("Invalid Input");
                            return;
                        }
                        production.actors.add(string);
                        production.actors.remove(aux);
                        return;
                }
                System.out.println("\nInvalid Operation");
                return;

            case 3 : // Genres
                System.out.print("Operation:\n    1) Add\n    2) Delete\n    3) Modify\nYour choice: ");
                string = reader.readLine();
                operation = Integer.parseInt(string);
                Genre genre;

                try {
                    switch (operation) {
                        case 1: // Add
                            System.out.print("New Genre: ");
                            string = reader.readLine();
                            genre = Genre.valueOf(string);

                            production.genres.add(genre);
                            return;

                        case 2: // Delete
                            System.out.print("Genre: ");
                            string = reader.readLine();
                            genre = Genre.valueOf(string);

                            production.genres.remove(genre);
                            return;

                        case 3: // Modify
                            System.out.print("Genre: ");
                            aux = reader.readLine();
                            genre = Genre.valueOf(string);

                            System.out.print("Modified Genre: ");
                            string = reader.readLine();
                            Genre genre2 = Genre.valueOf(string);

                            production.genres.remove(genre);
                            production.genres.add(genre2);
                            return;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Genre not supported");
                }
                System.out.println("\nInvalid Operation");
                return;

            case 4: // Plot
                System.out.print("Modified Plot: ");
                string = reader.readLine();
                if(string.compareTo("") == 0) {
                    System.out.println("Invalid Input");
                    return;
                }
                production.plot = string;

                return;

            case 5: // Release Year
                System.out.print("Modified Year: ");
                string = reader.readLine();
                long year = Long.parseLong(string);
                if(string.compareTo("") == 0) {
                    System.out.println("Invalid Input");
                    return;
                }

                if(option == 1) {
                    ((Movie) production).releaseYear = year;
                } else if (option == 2) {
                    ((Series) production).releaseYear = year;
                }
                return;
        }

        if(option == 1) {
            System.out.print("Modified Duration: ");
            string = reader.readLine();
            if(string.compareTo("") == 0) {
                System.out.println("Invalid Input");
                return;
            }
             ((Movie) production).duration = string;

        } else if (option == 2) {
            if (update == 6) { // Seasons
                int value;
                System.out.print("Operation:\n    1) Add\n    2) Delete\nYour choice: ");
                string = reader.readLine();
                operation = Integer.parseInt(string);

                switch (operation) {
                    case 1: // Add
                        System.out.println("----- Season " + ( ((Series) production).numSeasons + 1 ) + " -----");
                        List<Episode> episodeList = new ArrayList<>();

                        // Get Episodes
                        while (true) {
                            String episodeName, duration;
                            System.out.print("Episode title : ");
                            episodeName = reader.readLine();

                            System.out.print("Episode duration : ");
                            duration = reader.readLine();

                            /* Check input */
                            if (duration.compareTo("") == 0 || episodeName.compareTo("") == 0) {
                                System.out.println("\nInvalid input");
                                return;
                            }
                            Episode episode = new Episode(episodeName, duration);
                            episodeList.add(episode);

                            // Ask to continue
                            System.out.print("Add more episodes?\n    1) Yes\n    2) No\nYour choice: ");
                            string = reader.readLine();
                            value = Integer.parseInt(string);
                            if (value == 2) {
                                break;
                            } else if (value != 1) {
                                System.out.println("\nInvalid option");
                                return;
                            }
                        }
                        ((Series) production).numSeasons ++;
                        ((Series) production).addMap("Season " + ((Series)production).numSeasons, episodeList);
                        return;

                    case 2: // Delete
                        System.out.print("Actor: ");
                        string = reader.readLine();
                        if(string.compareTo("") == 0 || production.actors.contains(string)) {
                            System.out.println("Invalid Input");
                            return;
                        }
                        ((Series) production).numSeasons --;
                        ((Series) production).removeMap(string);
                        return;

                }
                System.out.println("\nInvalid Operation");

            } else { // Episode
                Series series1 = ((Series) production);
                String episodeName, duration;

                System.out.print("Operation:\n    1) Add\n    2) Delete\n    3) Modify\nYour choice: ");
                string = reader.readLine();
                operation = Integer.parseInt(string);

                System.out.print("Season (from 1 to " + series1.numSeasons + ") : ");
                string = reader.readLine();
                int value = Integer.parseInt(string);
                if (value < 1 || value > series1.numSeasons) {
                    System.out.println("Invalid Season");
                    return;
                }

                switch (operation) {
                    case 1: // Add
                        System.out.print("Episode title : ");
                        episodeName = reader.readLine();

                        System.out.print("Episode duration : ");
                        duration = reader.readLine();

                        /* Check input */
                        if (duration.compareTo("") == 0 || episodeName.compareTo("") == 0) {
                            System.out.println("\nInvalid input");
                            return;
                        }
                        Episode episode = new Episode(episodeName, duration);
                        series1.addEpisode("Season " + value, episode);
                        return;

                    case 2: // Delete
                        System.out.print("Episode title : ");
                        episodeName = reader.readLine();
                        /* Check input */
                        if (episodeName.compareTo("") == 0) {
                            System.out.println("\nInvalid input");
                            return;
                        }
                        series1.removeEpisode("Season " + value, episodeName);
                        return;

                    case 3: // Modify
                        System.out.print("Modify:\n    1) Title\n    2) Duration\nYour choice: ");
                        string = reader.readLine();
                        operation = Integer.parseInt(string);

                        if(operation == 1) {
                            // Title
                            System.out.print("Episode title : ");
                            episodeName = reader.readLine();

                            System.out.print("New Episode title : ");
                            string = reader.readLine();
                            /* Check input */
                            if (episodeName.compareTo("") == 0 || string.compareTo("") == 0) {
                                System.out.println("\nInvalid input");
                                return;
                            }
                            series1.modifyEpisodeName("Season " + value, episodeName, string);
                            return;

                        } else if (operation == 2) {
                            // Duration
                            System.out.print("Episode title : ");
                            episodeName = reader.readLine();

                            System.out.print("New Episode duration : ");
                            string = reader.readLine();
                            /* Check input */
                            if (episodeName.compareTo("") == 0 || string.compareTo("") == 0) {
                                System.out.println("\nInvalid input");
                                return;
                            }
                            series1.modifyEpisodeDuration("Season " + value, episodeName, string);
                            return;
                        }
                        System.out.println("\nInvalid Modification");
                        return;
                }
                System.out.println("\nInvalid Operation");
            }
        }
    }
    public void updateActor(Staff staff, BufferedReader reader) throws IOException, NumberFormatException {
        int update, operation;
        String string, name;

        System.out.print("Actor Name: ");
        name = reader.readLine();
        Actor actor = searchActor(name);
        if (actor == null) {
            System.out.println("No Actor with this name");
            return;
        }
        if ( ! staff.checkPermissionActor(name) ) {
            return;
        }

        System.out.print("Update:\n    1) Name\n    2) Biography\n    3) Performances\nYour choice: ");
        string = reader.readLine();
        update = Integer.parseInt(string);
        if (update == 1) {
            // Name
            System.out.print("Modified Name: ");
            string = reader.readLine();
            if(string.compareTo("") == 0) {
                System.out.println("Invalid Input");
                return;
            }
            actor.name = string;

        } else if (update == 2) {
            // Biography
            System.out.print("Modified Biography: ");
            string = reader.readLine();
            if(string.compareTo("") == 0) {
                System.out.println("Invalid Input");
                return;
            }
            actor.biography = string;

        } else if (update == 3) {
            // Performances
            int option;
            System.out.print("Operation:\n    1) Add\n    2) Delete\n    3) Modify\nYour choice: ");
            string = reader.readLine();
            operation = Integer.parseInt(string);

            System.out.print("Performance title: ");
            String title = reader.readLine();
            if(string.compareTo("") == 0 || ( !actor.performances.containsKey(title) && operation != 1 ) ) {
                System.out.println("Invalid Input");
                return;
            }

            switch (operation) {
                case 1: // Add
                    System.out.print("Performance type:\n    1) Movie\n    2) Series\nYour choice: ");
                    string = reader.readLine();
                    option = Integer.parseInt(string);

                    /* Check input */
                    if (string.compareTo("") == 0 || title.compareTo("") == 0 || (option != 1 && option != 2)) {
                        System.out.println("\nInvalid input");
                        return;
                    } else if (option == 1) {
                        actor.performances.put(title, "Movie");
                    } else {
                        actor.performances.put(title, "Series");
                    }
                    return;

                case 2: // Delete
                    actor.performances.remove(string);
                    return;

                case 3: // Modify

                    System.out.print("Modify:\n    1) Title\n    2) Type\nYour choice: ");
                    string = reader.readLine();
                    option = Integer.parseInt(string);

                    if (option == 1) {
                        // Title
                        System.out.print("New Performance title: ");
                        string = reader.readLine();
                        if(string.compareTo("") == 0) {
                            System.out.println("\nInvalid input");
                            return;
                        }
                        String type = actor.performances.get(title);
                        actor.performances.remove(title);
                        actor.performances.put(string, type);

                        return;

                    } else if (option == 2) {
                        // Type
                        if (actor.performances.get(title).compareTo("Movie") == 0) {
                            actor.performances.remove(title);
                            actor.performances.put(title, "Series");
                        } else {
                            actor.performances.remove(title);
                            actor.performances.put(title, "Movie");
                        }

                        return;

                    }

                    return;
            }
            System.out.println("\nInvalid Operation");
            return;

        }
    }

    // Add / Delete User
    public void add_deleteUser(Admin admin, BufferedReader reader) throws IOException, NumberFormatException {
        int option;
        String string;

        System.out.print("Options:\n    1) Add\n    2) Delete\nYour choice: ");
        string = reader.readLine();
        option = Integer.parseInt(string);

        if(option == 1) {
            addUser(admin, reader);
        } else if (option == 2) {
            deleteUser(admin, reader);
        } else {
            System.out.println("\nInvalid option");
        }
    }

    public void addUser(Admin admin, BufferedReader reader) throws IOException, NumberFormatException {
        int option;
        String username, string;
        AccountType userType;
        String name, password, email, country, gender, birthDate;
        long age;

        // Get Username
        System.out.print("Username : ");
        username = reader.readLine();
        User u = searchUser(username);
        while (username.compareTo("") == 0 || u != null) {
            System.out.print("Invalid Username\nUsername : ");
            username = reader.readLine();
            u = searchUser(username);
        }

        // Get Email
        System.out.print("Email : ");
        email = reader.readLine();
        if (email.compareTo("") == 0) {
            System.out.println("\nInvalid Input");
            return;
        }

        // Get Name
        System.out.print("Name : ");
        name = reader.readLine();
        if (name.compareTo("") == 0) {
            System.out.println("\nInvalid Input");
            return;
        }

        // Get Password
        System.out.print("Password : ");
        password = reader.readLine();
        if (password.compareTo("") == 0) {
            System.out.println("\nInvalid Input");
            return;
        }

        // Get Account Type
        System.out.print("Account Type:\n    1) Admin\n    2) Contributor\n    3) Regular\nYour choice: ");
        string = reader.readLine();
        option = Integer.parseInt(string);
        if (option == 1) {
            // Admin
            userType = AccountType.Admin;
        } else if (option == 2) {
            // Contributor
            userType = AccountType.Contributor;
        } else if (option == 3) {
            // Regular
            userType = AccountType.Regular;
        } else {
            System.out.println("\nInvalid option");
            return;
        }

        User user = UserFactory.Factory(userType);
        user.setUsername(username);
        user.setUserType(userType);

        // Get Account Type
        System.out.print("Add extra information?:\n    1) Yes\n    2) No\nYour choice: ");
        string = reader.readLine();
        option = Integer.parseInt(string);

        if (option == 1) {
            // Get Country
            System.out.print("Country : ");
            country = reader.readLine();
            if (country.compareTo("") == 0) {
                System.out.println("\nInvalid Input");
                return;
            }

            // Get Age
            System.out.print("Age(number) : ");
            string = reader.readLine();
            age = Long.parseLong(string);
            if(age < 10 || age > 100) {
                System.out.println("\nInvalid Number");
                return;
            }

            // Get Birth Date
            System.out.print("Birth Date : ");
            birthDate = reader.readLine();
            if (birthDate.compareTo("") == 0) {
                System.out.println("\nInvalid Input");
                return;
            }

            // Get Gender
            System.out.print("Gender : ");
            gender = reader.readLine();
            if (gender.compareTo("") == 0) {
                System.out.println("\nInvalid Input");
                return;
            }

            user.information = new User.Information.Builder(email, password, name)
                    .Country(country)
                    .Age(age)
                    .BirthDate(birthDate)
                    .Gender(gender)
                    .build();
        } else if (option == 2) {
            user.information = new User.Information.Builder(email, password, name)
                    .build();
        } else {
            System.out.println("\nInvalid choice");
            return;
        }

        admin.addUser(user);
    }

    public void deleteUser(Admin admin, BufferedReader reader) throws IOException, NumberFormatException {
        int option;
        String username;

        System.out.print("Username : ");
        username = reader.readLine();
        if (username.compareTo("") == 0) {
            System.out.println("\nInvalid Input");
            return;
        }
        User user = searchUser(username);
        if (user == null) {
            System.out.println("\nUser not Found");
            return;
        }
        if (user.getUserType() == AccountType.Regular) {
            admin.dellRegular(username);
        } else if (user.getUserType() == AccountType.Contributor) {
            admin.dellContributor(username);
        } else if (user.getUserType() == AccountType.Admin) {
            admin.dellAdmin(username);
        }
    }

    // Search Functions
    public User searchUser(String username) {
        for (Admin admin : admins) {
            if (admin.getUsername().compareTo(username) == 0) {
                return admin;
            }
        }
        for (Contributor contributor : contributors) {
            if (contributor.getUsername().compareTo(username) == 0) {
                return contributor;
            }
        }
        for (Regular regular : regulars) {
            if (regular.getUsername().compareTo(username) == 0) {
                return regular;
            }
        }
        return null;
    }
    public User searchStaff(String username) {
        for (Contributor contributor : contributors) {
            if (contributor.getUsername().compareTo(username) == 0) {
                return contributor;
            }
        }
        for (Admin admin : admins) {
            if (admin.getUsername().compareTo(username) == 0) {
                return admin;
            }
        }
        return null;
    }
    public void searchPrintItem(String name) {
        int ok = 0;
        for(Actor actor : this.actors) {
            if (actor.name.compareTo(name) == 0) {
                actor.printActor();
                ok = 1;
            }
        }
        for (Movie movie : this.movies) {
            if (movie.title.compareTo(name) == 0) {
                movie.displayInfo();
                movie.displayInfoProduction();
                ok = 1;
            }
        }
        for (Series series_obj : this.series) {
            if (series_obj.title.compareTo(name) == 0) {
                series_obj.displayInfoProduction();
                series_obj.displayInfo();
                ok = 1;
            }
        }
        if (ok == 0) {
            System.out.println("\nItem not found\n");
        }
    }
    public boolean checkActor (String name) {
        for(Actor actor : this.actors) {
            if (actor.name.compareTo(name) == 0) {
                return true;
            }
        }
        return false;
    }
    public boolean checkProduction(String title) {
        for (Movie movie : this.movies) {
            if (movie.title.compareTo(title) == 0) {
                return true;
            }
        }
        for (Series series_obj : this.series) {
            if (series_obj.title.compareTo(title) == 0) {
                return true;
            }
        }
        return false;
    }
    public Movie searchMovie (String title) {
        for (Movie movie : this.movies) {
            if (movie.title.compareTo(title) == 0) {
                return movie;
            }
        }
        return null;
    }
    public Series searchSeries (String title) {
        for (Series series_obj : this.series) {
            if (series_obj.title.compareTo(title) == 0) {
                return series_obj;
            }
        }
        return null;
    }
    public Production searchProduction(String title) {
        for (Movie movie : this.movies) {
            if (movie.title.compareTo(title) == 0) {
                return movie;
            }
        }
        for (Series series_obj : this.series) {
            if (series_obj.title.compareTo(title) == 0) {
                return series_obj;
            }
        }
        return null;
    }
    public Actor searchActor(String name) {
        for (Actor actor : this.actors) {
            if (actor.name.compareTo(name) == 0) {
                return actor;
            }
        }
        return null;
    }
}