import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class Staff extends User implements StaffInterface {
    List<Request> requests;
    SortedSet<Production> productionsContribution;
    SortedSet<Actor> actorsContribution;

    public Staff() {
        super();
        this.requests  = new ArrayList<Request>();
        this.productionsContribution = new TreeSet<Production>();
        this.actorsContribution = new TreeSet<Actor>();
    }

    public void printStaff() {
        if(!requests.isEmpty()) {
            System.out.println("-------------- Requests --------------");
            for (Request request : requests) {
                request.printRequest();
            }
        }

        if(!actorsContribution.isEmpty()) {
            System.out.println("------------- Actors Contribution --------------");
            for (Actor actor : actorsContribution) {
                actor.printActor();
            }
        }

        if(!productionsContribution.isEmpty()) {
            System.out.println("----------- Productions Contribution -----------");
            for (Production production : productionsContribution) {
                production.displayInfo();
                production.displayInfoProduction();
            }
        }
    }

    public void addProductionContribution(String title) {
        if (title == null) {
            System.err.println("Null Argument");
            return;
        }
        IMDB imdb = IMDB.getInstance();
        for(Movie movie : imdb.movies) {
            if (movie.title.compareTo(title) == 0) {
                addProductionContribution(movie);
            }
        }
        for(Series series : imdb.series) {
            if (series.title.compareTo(title) == 0) {
                addProductionContribution(series);
            }
        }
    }
    public void addProductionContribution(Production p) {
        if (p == null) {
            System.err.println("Null Argument");
            return;
        }
        this.productionsContribution.add(p);
    }

    public void addActorsContribution(String name) {
        if (name == null) {
            System.err.println("Null Argument");
            return;
        }
        IMDB imdb = IMDB.getInstance();
        for(Actor actor : imdb.actors) {
            if (actor.name.compareTo(name) == 0) {
                addActorsContribution(actor);
            }
        }
    }
    public void addActorsContribution(Actor a) {
        if (a == null) {
            System.err.println("Null Argument");
            return;
        }
        this.actorsContribution.add(a);
    }

    public void addProductionSystem(Production p) {
        if (p == null) {
            System.out.println("Null object !");
            return;
        }
        if (p.getClass() == Movie.class) {
            Movie movie = (Movie) p;
            // add for Staff
            productionsContribution.add(movie);
            // add in System
            IMDB.getInstance().movies.add(movie);
            // Get Production Experience
            this.addExperiencePoints(movie.calculateEXP());

        } else {
            Series series = (Series) p;
            // add for Staff
            productionsContribution.add(series);
            // add in System
            IMDB.getInstance().series.add(series);
            // Get Production Experience
            this.addExperiencePoints(series.calculateEXP());
        }
    }

    public void addActorSystem(Actor a) {
        if (a == null) {
            System.out.println("Null object !");
            return;
        }
        // add for Staff
        actorsContribution.add(a);
        // add in System
        IMDB.getInstance().actors.add(a);
        // Get Production Experience
        this.addExperiencePoints(a.calculateEXP());
    }

    public void removeProductionSystem(String name) {
        // check permission
        if ( ! checkPermissionProduction(name) )
            return;
        // remove Production
        IMDB imdb = IMDB.getInstance();
        for(int i = 0; i < imdb.movies.size(); i++) {
            if(imdb.movies.get(i).title.compareTo(name) == 0) {
                // remove for Staff
                productionsContribution.remove(imdb.movies.get(i));
                // remove from System
                imdb.movies.remove(i);
                return;
            }
        }
        for(int i = 0; i < imdb.series.size(); i++) {
            if(imdb.series.get(i).title.compareTo(name) == 0) {
                // remove for Staff
                productionsContribution.remove(imdb.series.get(i));
                // remove from System
                imdb.series.remove(i);
                return;
            }
        }
        System.out.println("Item not found");
    }
    public void updateProduction(Production p) {
        if (p == null) {
            System.err.println("Null Argument");
            return;
        }
        // check permission
        if ( ! checkPermissionProduction(p.title) )
            return;
        // update Production
        IMDB imdb = IMDB.getInstance();
        for(int i = 0; i < imdb.movies.size(); i++) {
            if(imdb.movies.get(i).title.compareTo(p.title) == 0) {
                Movie movie = (Movie) p;
                // update for Staff
                productionsContribution.remove(imdb.movies.get(i));
                productionsContribution.add(movie);
                // update in System
                imdb.movies.remove(i);
                imdb.movies.add(movie);

                return;
            }
        }
        for(int i = 0; i < imdb.series.size(); i++) {
            if(imdb.series.get(i).title.compareTo(p.title) == 0) {
                Series series = (Series) p;
                // update for Staff
                productionsContribution.remove(imdb.movies.get(i));
                productionsContribution.add(series);
                // update in System
                imdb.series.remove(i);
                imdb.series.add(series);
                return;
            }
        }
        System.out.println("Production not found");
    }

    public void removeActorSystem(String name) {
        // check permission
        if ( ! checkPermissionActor(name) )
            return;
        // remove actor
        IMDB imdb = IMDB.getInstance();
        for(int i = 0; i < imdb.actors.size(); i++) {
            if(imdb.actors.get(i).name.compareTo(name) == 0) {
                // remove for Staff
                actorsContribution.remove(imdb.actors.get(i));
                // remove from System
                imdb.actors.remove(i);
                return;
            }
        }
        System.out.println("Actor not found");
    }
    public void updateActor(Actor a) {
        // check permission
        if ( a == null || !checkPermissionActor(a.name) )
            return;
        // update the actor
        IMDB imdb = IMDB.getInstance();
        for(int i = 0; i < imdb.actors.size(); i++) {
            if (imdb.actors.get(i).name.compareTo(a.name) == 0) {
                // update for Staff
                actorsContribution.remove(imdb.actors.get(i));
                actorsContribution.add(a);
                // update in System
                imdb.actors.remove(i);
                imdb.actors.add(a);
                return;
            }
        }
        System.out.println("Actor not found");
    }
    public abstract boolean checkPermissionProduction(String name);
    public abstract boolean checkPermissionActor(String name);
}