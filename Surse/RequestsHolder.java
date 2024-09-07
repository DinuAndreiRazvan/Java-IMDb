import java.util.ArrayList;
import java.util.List;

public class RequestsHolder {
    private static List<Request> requests;

    public static void addRequest(Request req) {
        if(req  == null) {
            System.out.println("Null Argument");
            return;
        }
        if(requests == null)
            requests = new ArrayList<Request>();
        req.to = "ADMIN";
        requests.add(req);
    }

    public static void deleteRequest(Request req) {
        if(req  == null) {
            System.out.println("Null Argument");
            return;
        }
        requests.remove(req);
    }

    public static void deleteRequest(int index, RequestStatus requestStatus) {
        if(index  >= requests.size() || index < 0) {
            System.out.println("\nInvalid Index");
            return;
        }
        requests.get(index).status = requestStatus;
        requests.get(index).notifyObservers();
        requests.remove(index);
    }
    public static void deleteRequest(String username) {
        for(int i = 0; i < requests.size(); i++) {
            if(requests.get(i).username.compareTo(username) == 0){
                requests.remove(i);
            }
        }
    }
    public static void printRequest(String username) {
        for (Request request : requests) {
            if (request.username.compareTo(username) == 0) {
                request.printRequest();
            }
        }
    }
    public static void printRequest(int index) {
        if (requests.isEmpty()) {
            System.out.println("\nNo Requests found");
            return;
        }
        for (Request request : requests) {
            request.printRequest(index);
            index ++;
        }
    }
    public static void printRequest(String username, int index) {
        for (Request request : requests) {
            if (request.username.compareTo(username) == 0) {
                request.printRequest(index);
                index ++;
            }
        }
    }

    public static Request searchRequest(String username, String description, String to) {
        for(Request request : requests) {
            if(request.username.compareTo(username) == 0
                    && request.description.compareTo(description) == 0
                    && request.to.compareTo(to) == 0) {
                return request;
            }
        }
        return null;
    }

    public static Request getRequest(int index) {
        return requests.get(index);
    }
}