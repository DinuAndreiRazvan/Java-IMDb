import java.io.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class JsonSimpleProduction {
    public static void readProduction(String file){
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try {
            //  for checking the current path:
            //  System.out.println(new File(".").getAbsolutePath());
            FileReader reader = new FileReader(file);
            // Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray productionList = (JSONArray) obj;

            // Iterate over array
            for(Object object : productionList) {
                JSONObject jsonObject = (JSONObject) object;
                parseRequestProduction(jsonObject);
            }

        } catch (FileNotFoundException e) {
            System.err.println("Production - File Not Found");
        } catch (IOException e) {
            System.err.println("Production - IO exception");
        } catch (ParseException e) {
            System.err.println("Production - Parse exception");
        }
    }

    private static void parseRequestProduction(JSONObject jsonObject)
    {
        if(jsonObject == null) {
            System.err.println("Null argument");
            return;
        }

        // Get production Title
        String title = (String) jsonObject.get("title");

        // Get production Type
        String type = (String) jsonObject.get("type");

        // Get production Directors
        List<String> directors = new ArrayList<String>();
        JSONArray directorsList = (JSONArray) jsonObject.get("directors");
        for (Object object : directorsList) {
            String x = (String) object;
            directors.add(x);
        }

        // Get production Actors
        List<String> actors = new ArrayList<String>();
        JSONArray actorsList = (JSONArray) jsonObject.get("actors");
        for (Object object : actorsList) {
            String x = (String) object;
            actors.add(x);
        }

        // Get production Genres
        List<Genre> genres = new ArrayList<Genre>();
        JSONArray genresList = (JSONArray) jsonObject.get("genres");
        for (Object object : genresList) {
            String x = (String) object;
            Genre y = Genre.valueOf(x);
            genres.add(y);
        }

        // Get production Ratings
        List<Rating> ratings = new ArrayList<Rating>();
        JSONArray performanceList = (JSONArray) jsonObject.get("ratings");
        // Iterate over performances
        for(Object object : performanceList) {
            JSONObject jsonObj = (JSONObject) object;
            String username = (String) jsonObj.get("username");
            Long evaluation = (Long) jsonObj.get("rating");
            String comment = (String) jsonObj.get("comment");
            Rating x = new Rating(username, evaluation, comment);
            ratings.add(x);
        }

        // Get production Plot
        String plot = (String) jsonObject.get("plot");

        // Get production Average Rating
        double averageRating = (double) jsonObject.get("averageRating");

        // Get Movie Release Year (Series & Movies)
        Long releaseYear = (Long) jsonObject.get("releaseYear");

        if(type.compareTo("Movie") == 0) {
            // Get Movie Duration
            String  duration = (String) jsonObject.get("duration");

            // Build Object
            Movie movie = new Movie(title, directors, actors, genres, ratings, plot, averageRating, duration, releaseYear);
            IMDB imdb = IMDB.getInstance();
            imdb.movies.add(movie);
//            movie.displayInfo();
//            movie.displayInfoProduction();

        } else {
            Series series;

            // Get Seasons
            Long numSeasons = (Long) jsonObject.get("numSeasons");

            // Get Episodes for each Season
            Map<String, List<Episode>> seasons = new HashMap<>();
            JSONObject jsonObj= (JSONObject) jsonObject.get("seasons");
            // Iterate over seasons
            for(int i = 1; i <= numSeasons; i++) {
                String name = "Season " + i;
                // get list of Episodes
                List<Episode> episodes = new ArrayList<Episode>();
                JSONArray episodesList = (JSONArray) jsonObj.get(name);

                for(Object object_2 : episodesList) {
                    JSONObject jsonObj_2 = (JSONObject) object_2;
                    String  episodeName = (String) jsonObj_2.get("episodeName");
                    String duration = (String)  jsonObj_2.get("duration");
                    Episode epi = new Episode(episodeName, duration);
                    episodes.add(epi);
                }
                seasons.put(name, episodes);
            }
            series = new Series(title, directors, actors, genres, ratings, plot, averageRating, releaseYear, numSeasons, seasons);
            IMDB imdb = IMDB.getInstance();
            imdb.series.add(series);
//             series.displayInfo();
//             series.displayInfoProduction();
        }
    }
}
