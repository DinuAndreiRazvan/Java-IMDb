import java.util.Map;

public class Regular extends User implements RequestsManager {

    public Regular() {
        super();
    }

    public void addRequest(Request req) {
        if(req  == null) {
            System.out.println("Null Argument");
            return;
        }
        IMDB imdb = IMDB.getInstance();
        // Check Sender
        User sender = imdb.searchUser(req.username);
        if ( sender == null ) {
            System.out.println("\nInvalid sender");
            return;
        }

        // Check request type
        if (req.getType() == RequestTypes.DELETE_ACCOUNT || req.getType() == RequestTypes.OTHERS) {
            // All Admins receive this
            for (Admin admin : imdb.admins) {
                req.addObserver(admin);
            }
            // notify observers
            req.notifyObservers();
            // add request to sender
            this.requestsSent.put(requestsSent.size() + 1 , req);
            // add to System
            RequestsHolder.addRequest(req);

        } else {
            // Check Receiver
            Staff receiver = (Staff)imdb.searchStaff(req.to);
            if ( receiver == null ) {
                System.out.println("\nInvalid receiver");
                return;
            }
            // notify observers
            req.addObserver(receiver);
            req.notifyObservers();
            // add request to sender and receiver
            this.requestsSent.put(requestsSent.size() + 1 , req);
            receiver.requests.add(req);
            // add to System
            IMDB.getInstance().requests.add(req);
        }
    }

    public void removeRequest(int index) {
        IMDB imdb = IMDB.getInstance();
        // Check Index
        if (index > requestsSent.size()) {
            System.out.println("\nIndex not in range");
            return;
        }

        Request request = requestsSent.get(index);
        requestsSent.remove(index);
        if(request.getType() == RequestTypes.OTHERS || request.getType() == RequestTypes.DELETE_ACCOUNT) {
            // Remove for all admins
            for (Admin admin : imdb.admins) {
                request.deleteObserver(admin);
            }
            RequestsHolder.deleteRequest(request);

        } else {
            // Get receiver
            Staff receiver = (Staff)imdb.searchStaff(request.to);
            receiver.requests.remove(request);
            imdb.requests.remove(request);
        }
    }
    public void removeRequest(Request request) {
        for (Map.Entry<Integer,  Request> entry : requestsSent.entrySet()) {
            int index = entry.getKey();
            Request req = entry.getValue();
            if (request.compareTo(req) == 0) {
                removeRequest(index);
                return;
            }
        }
    }

    public void addRating(Movie movie, Long evaluation, String comment) {
        if(evaluation < 1 || evaluation > 10) {
            System.out.println("Number must be between 1 and 10 !");
            return;
        }
        if(comment == null || comment.compareTo("") == 0) {
            System.out.println("Comment missing !");
            return;
        }
        Rating rating = new Rating(this.getUsername(), evaluation, comment);
        // Modify in System
        IMDB imdb = IMDB.getInstance();
        for (Movie m : imdb.movies) {
            if (m.compareTo(movie) == 0) {
                m.ratings.add(rating);
            }
        }
        // Add Rating experience
        this.addExperiencePoints(rating.calculateEXP());
    }
    public void addRating(Series series, Long evaluation, String comment) {
        if(evaluation < 1 || evaluation > 10) {
            System.out.println("Number must be between 1 and 10 !");
            return;
        }
        if(comment == null || comment.compareTo("") == 0) {
            System.out.println("Comment missing !");
            return;
        }
        Rating rating = new Rating(this.getUsername(), evaluation, comment);
        // Modify in System
        IMDB imdb = IMDB.getInstance();
        for (Series s : imdb.series) {
            if (s.compareTo(series) == 0) {
                s.ratings.add(rating);
            }
        }
        // Add Rating experience
        this.addExperiencePoints(rating.calculateEXP());
    }
}