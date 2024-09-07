import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class JsonSimpleActors {
    public static void readActor(String file){
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try {
            //  for checking the current path:
            //  System.out.println(new File(".").getAbsolutePath());
            FileReader reader = new FileReader(file);
            // Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray actorList = (JSONArray) obj;

            // Iterate over array
            for(Object object : actorList) {
                JSONObject jsonObject = (JSONObject) object;
                parseRequestActor(jsonObject);
            }

        } catch (FileNotFoundException e) {
            System.err.println("Actor - File Not Found");
        } catch (IOException e) {
            System.err.println("Actor - IO exception");
        } catch (ParseException e) {
            System.err.println("Actor - Parse exception");
        }
    }

    private static void parseRequestActor(JSONObject jsonObject)
    {
        if(jsonObject == null) {
            System.err.println("Null argument");
            return;
        }
        Actor actor;

        // Get actor name
        String name = (String) jsonObject.get("name");

        // Get actor performances
        Map<String, String> performances = new HashMap<String, String>();
        JSONArray performanceList = (JSONArray) jsonObject.get("performances");
        // Iterate over performances
        for(Object object : performanceList) {
            JSONObject jsonObj = (JSONObject) object;
            String title = (String) jsonObj.get("title");
            String type = (String) jsonObj.get("type");
            performances.put(title, type);
        }

        // Get biography
        String biography = (String) jsonObject.get("biography");

        actor = new Actor(name, biography, performances);
        // actor.printActor();
        IMDB.getInstance().actors.add(actor);
    }
}
