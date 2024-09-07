import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonSimpleRequest {
    public static void readRequests(String file){
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try {
            //  for checking the current path:
            //  System.out.println(new File(".").getAbsolutePath());
            FileReader reader = new FileReader(file);
            // Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray requestList = (JSONArray) obj;

            // Iterate over array
            for(Object object : requestList) {
                JSONObject jsonObject = (JSONObject) object;
                parseRequestObject(jsonObject);
            }

        } catch (FileNotFoundException e) {
            System.err.println("Request - File Not Found");
        } catch (IOException e) {
            System.err.println("Request - IO exception");
        } catch (ParseException e) {
            System.err.println("Request - Parse exception");
        }
    }

    private static void parseRequestObject(JSONObject jsonObject)
    {
        if(jsonObject == null) {
            System.err.println("Null argument");
            return;
        }
        Request request;

        // Get request type
        String Type = (String) jsonObject.get("type");
        RequestTypes type = RequestTypes.valueOf(Type);
        // Get request createdDate
        String createdDate = (String) jsonObject.get("createdDate");
        // Get request username
        String username = (String) jsonObject.get("username");
        // Get request to
        String to = (String) jsonObject.get("to");
        // Get request description
        String description = (String) jsonObject.get("description");

        // create the Request object based on it's type
        if(type == RequestTypes.MOVIE_ISSUE) {
            String movieTitle = (String) jsonObject.get("movieTitle");
            request = new Request(type, createdDate, username, movieTitle, to, description);
            IMDB.getInstance().requests.add(request);

        } else if(type == RequestTypes.ACTOR_ISSUE) {
            String actorName = (String) jsonObject.get("actorName");
            request = new Request(type, createdDate, username, actorName, to, description);
            IMDB.getInstance().requests.add(request);

        } else {
            request = new Request(type, createdDate, username, to, description);
            RequestsHolder.addRequest(request);

        }
        // request.printRequest();
    }

}
