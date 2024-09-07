import java.io.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class JsonSimpleUsers {
    public static void readUser(String file){
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try {
            //  for checking the current path:
            //  System.out.println(new File(".").getAbsolutePath());
            FileReader reader = new FileReader(file);
            // Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray usersList = (JSONArray) obj;

            // Iterate over array
            for(Object object : usersList) {
                JSONObject jsonObject = (JSONObject) object;
                parseRequestUser(jsonObject);
            }

        } catch (FileNotFoundException e) {
            System.err.println("Actor - File Not Found");
        } catch (IOException e) {
            System.err.println("Actor - IO exception");
        } catch (ParseException e) {
            System.err.println("Actor - Parse exception");
        }
    }

    private static void parseRequestUser(JSONObject jsonObject)
    {
        if(jsonObject == null) {
            System.err.println("Null argument");
            return;
        }

        // Get Username
        String username = (String) jsonObject.get("username");

        // Get Experience
        int experience;
        String exp = (String) jsonObject.get("experience");
        if (exp == null)
            experience = 0;
        else
            experience = Integer.valueOf(exp);

        // Get Information Object
        JSONObject json_obj_info = (JSONObject) jsonObject.get("information");

            // Get Credentials Object
            JSONObject json_obj_credentials = (JSONObject) json_obj_info.get("credentials");
                // Get email
                String email = (String) json_obj_credentials.get("email");
                // Get password
                String password = (String) json_obj_credentials.get("password");

            //Get Country
            String country = (String) json_obj_info.get("country");
            // Get name
            String name = (String) json_obj_info.get("name");
            // Get age
            Long age = (Long) json_obj_info.get("age");
            // Get Gender
            String gender = (String) json_obj_info.get("gender");
            // Get Birth Date
            String birthDate = (String) json_obj_info.get("birthDate");

        // Get User Type
        String x = (String) jsonObject.get("userType");
        AccountType userType = AccountType.valueOf(x);

        // Initialize user
        User user = UserFactory.Factory(userType);

        // Get FavoriteProductions
        JSONArray jsonArray = (JSONArray) jsonObject.get("notifications");
        if (jsonArray != null) {
            List<String> notifications = new ArrayList<>();
            for (Object object : jsonArray) {
                // Get Production Title
                String notification = (String) object;
                // Add production
                notifications.add(notification);
            }
            user.setNotifications(notifications);
        }

        // Get FavoriteProductions
        jsonArray = (JSONArray) jsonObject.get("favoriteProductions");
        if (jsonArray != null)
            for(Object object : jsonArray) {
                // Get Production Title
                String title = (String) object;
                // Add production
                user.addFavoriteProduction(title);
            }

        // Get FavoriteActors
        jsonArray = (JSONArray) jsonObject.get("favoriteActors");
        if (jsonArray != null)
            for(Object object : jsonArray) {
                // Get Actor Name
                String actorName = (String) object;
                // Add production
                user.addFavoriteActor(actorName);
            }

        // Build Information
        user.information = new User.Information.Builder(email, password, name)
                .Country(country)
                .Age(age)
                .BirthDate(birthDate)
                .Gender(gender)
                .build();

        // Build User
        user.user_set(username, experience, userType);

        if (user.getClass() == Admin.class || user.getClass() == Contributor.class) {
            // Get Productions Contribution
            jsonArray = (JSONArray) jsonObject.get("productionsContribution");
            if (jsonArray != null)
                for(Object object : jsonArray) {
                    // Get Production Title
                    String title = (String) object;
                    // Add production
                    ((Staff) user).addProductionContribution(title);
                }

            // Get productionsContribution
            jsonArray = (JSONArray) jsonObject.get("actorsContribution");
            if (jsonArray != null)
                for(Object object : jsonArray) {
                    // Get Actor Name
                    String actorName = (String) object;
                    // Add production
                    ((Staff) user).addActorsContribution(actorName);
                }
            IMDB imdb = IMDB.getInstance();
            if(user.getClass() == Admin.class) {
                imdb.admins.add( ((Admin) user) );
            } else {
                imdb.contributors.add( ((Contributor) user) );
            }

        } else {
            IMDB imdb = IMDB.getInstance();
            imdb.regulars.add( ((Regular) user) );
        }
    }
}
