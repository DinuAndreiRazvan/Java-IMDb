import java.util.Iterator;
import java.util.Map;

public class Contributor extends Staff implements RequestsManager {

    public Contributor() {
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
            if ( receiver == null || receiver.compareTo(this) == 0 ) {
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
        if (ok == 0) {
            System.out.println("Permission denied");
            return false;
        }
        return true;
    }
}