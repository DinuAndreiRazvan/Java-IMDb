import java.util.ArrayList;
import java.util.List;

public class Request implements Comparable<Request>, Subject, ExperienceStrategy {
    private RequestTypes type;
    private String createdDate;
    String username;
    String actorName;
    String title;
    String to;
    String description;

    public Request(RequestTypes type, String createdDate, String username, String to, String description) {
        this.status = RequestStatus.WAITING;
        this.type = type;
        this.createdDate = createdDate;
        this.username = username;
        this.to = to;
        this.description = description;
    }
    public Request(RequestTypes type, String createdDate, String username, String actor_movie, String to, String description) {
        this.type = type;
        this.createdDate = createdDate;
        this.username = username;
        if (type == RequestTypes.ACTOR_ISSUE)
            this.actorName = actor_movie;
        else
            this.title = actor_movie;
        this.to = to;
        this.description = description;
    }

    public RequestTypes getType() {
        return type;
    }
    public void setType(RequestTypes type) {
        this.type = type;
    }

    public String getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    public void printRequest() {
        System.out.println("Type : " + this.type);
        System.out.println("Creation Date : " + this.createdDate);
        System.out.println("Username : " + this.username);
        System.out.println("To : " + this.to);
        System.out.println("Description : " + this.description);
        System.out.println();
    }
    public void printRequest(int index) {
        System.out.printf("Request index: (%d)\n", index);
        System.out.println("    Type : " + this.type);
        System.out.println("    Creation Date : " + this.createdDate);
        System.out.println("    Username : " + this.username);
        System.out.println("    To : " + this.to);
        System.out.println("    Description : " + this.description);
        System.out.println();
    }

    public User getSender() {
        IMDB imdb = IMDB.getInstance();
        return imdb.searchUser(this.username);
    }

    public int compareTo(Request request) {
        if(request == null) {
            System.out.println("Null argument");
            return 1;
        }
        if (this.username.compareTo(request.username) != 0)
            return this.username.compareTo(request.username);
        if (this.description.compareTo(request.description) != 0)
            return this.description.compareTo(request.description);
        if (this.to.compareTo(request.to) != 0)
            return this.to.compareTo(request.to);
        if (this.getType() != request.getType())
            return 1;
        if (this.createdDate.compareTo(request.createdDate) != 0)
            return this.createdDate.compareTo(request.createdDate);

        return 0;
    }

    //      Observer Pattern - Subject
    RequestStatus status; // Request status
    User sender;
    private List<User> users = new ArrayList<>(); // Request receivers
    public void addObserver(User user) {
        users.add(user);
    }
    public void deleteObserver(User user) {
        users.remove(user);
    }
    public void notifyObservers() {
        String message;
        if (status == null) {
            status = RequestStatus.WAITING;
        }
        if (sender == null)
            sender = IMDB.getInstance().searchUser(this.username);
        if (status == RequestStatus.REJECTED) {
            message = "Your Request has been Rejected";
            sender.update(message);
            return;
        } else if (status == RequestStatus.RESOLVED) {
            message = "Your Request has been Resolved";
            sender.update(message);
            sender.addExperiencePoints(calculateEXP());
            return;
        }

        for (User user : this.users) {
            if (user.getUserType() == AccountType.Admin || user.getUserType() == AccountType.Contributor) {
                message = "You have a new Request";
                user.update(message);
            }
        }
    }

    //      Strategy Pattern
    @Override
    public int calculateEXP() {
        return 3;
    }
}