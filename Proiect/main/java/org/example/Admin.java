import java.util.Iterator;

public class Admin extends Staff{

    public Admin() {
        super();
    }

    public void addUser(User user) {
        if(user == null) {
            System.out.println("Null argument");
            return;
        }
        IMDB imdb = IMDB.getInstance();
        switch(user.getUserType()) {
            case Admin -> imdb.admins.add((Admin) user);
            case Contributor -> imdb.contributors.add((Contributor) user);
            case Regular -> imdb.regulars.add((Regular) user);
        }
    }

    public void dellAdmin(String username) {
        IMDB imdb = IMDB.getInstance();
        // ADMIN
        for(int i = 0; i < imdb.admins.size(); i++) {
            if(imdb.admins.get(i).getUsername().compareTo(username) == 0) {
                imdb.requests.addAll(imdb.admins.get(i).requests);
                imdb.admins.remove(i);
                return;
            }
        }
    }

    public void dellContributor(String username) {
        IMDB imdb = IMDB.getInstance();
        // CONTRIBUTOR
        for(int i = 0; i < imdb.contributors.size(); i++) {
            if(imdb.contributors.get(i).getUsername().compareTo(username) == 0) {
                removeMarks(imdb.contributors.get(i).getUsername());
                imdb.contributors.remove(i);
                return;
            }
        }
    }

    public void dellRegular(String username) {
        IMDB imdb = IMDB.getInstance();
        // REGULAR
        for(int i = 0; i < imdb.regulars.size(); i++) {
            if(imdb.regulars.get(i).getUsername().compareTo(username) == 0) {
                removeMarks(imdb.regulars.get(i).getUsername());
                imdb.regulars.remove(i);
                return;
            }
        }
    }

    private void removeMarks(String username) {
        IMDB imdb = IMDB.getInstance();
        // remove movies ratings
        for(int i = 0; i < imdb.movies.size(); i++) {
            Movie movie = imdb.movies.get(i);
            for(int j = 0; j < movie.ratings.size(); j++) {
                if(movie.ratings.get(j).username.compareTo(username) == 0) {
                    imdb.movies.get(i).ratings.remove(j);
                    j = movie.ratings.size();
                }
            }
        }
        // remove series ratings
        for(int i = 0; i < imdb.series.size(); i++) {
            Series series = imdb.series.get(i);
            for(int j = 0; j < series.ratings.size(); j++) {
                if(series.ratings.get(j).username.compareTo(username) == 0) {
                    imdb.series.get(i).ratings.remove(j);
                    j = series.ratings.size();
                }
            }
        }
        // remove req from - requests for all
        for(int i = 0; i < imdb.requests.size(); i++) {
            if(imdb.requests.get(i).username.compareTo(username) == 0) {
                imdb.requests.remove(i);
            }
        }
        // remove req from - requests for admins
        RequestsHolder.deleteRequest(username);
    }

    public boolean checkPermissionProduction(String name) {
        // check permission to modify Production
        int ok = 0;
        Iterator<Production> obj = this.productionsContribution.iterator();
        while(obj.hasNext()) {
            Production production = (Production) obj;
            if (production.title.compareTo(name) == 0) {
                ok = 1;
                break;
            }
        }
        IMDB imdb = IMDB.getInstance();
        for (int i = 0; i < imdb.movies.size(); i++) {
            if(imdb.movies.get(i).title.compareTo(name) == 0) {
                ok = 1;
                break;
            }
        }
        for (int i = 0; i < imdb.series.size(); i++) {
            if(imdb.series.get(i).title.compareTo(name) == 0) {
                ok = 1;
                break;
            }
        }
        if (ok == 0) {
            System.out.println("Permission denied");
            return false;
        }
        return true;
    }
    public boolean checkPermissionActor(String name) {
        // check permission to modify Actor
        int ok = 0;
        Iterator<Actor> obj = this.actorsContribution.iterator();
        while(obj.hasNext()) {
            Actor actor = (Actor) obj;
            if (actor.name.compareTo(name) == 0) {
                ok = 1;
                break;
            }
        }
        IMDB imdb = IMDB.getInstance();
        for (int i = 0; i < imdb.actors.size(); i++) {
            if(imdb.actors.get(i).name.compareTo(name) == 0) {
                ok = 1;
                break;
            }
        }
        if (ok == 0) {
            System.out.println("Permission denied");
            return false;
        }
        return true;
    }
}