import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Actor implements Comparable<Actor>, ExperienceStrategy{
    String name;
    String biography;
    Map<String, String> performances;

    public Actor() {
        this.name = "";
        biography = "";
        performances = new TreeMap<>();
    }

    public Actor(String name, String biography, Map<String, String> performances) {
        this.name = name;
        this.biography = biography;
        this.performances = performances;
    }

    public void printActor() {
        System.out.println("Name : " + this.name);
        System.out.println("Performances : ");
        for (Map.Entry<String, String> entry : performances.entrySet()) {
            System.out.println("    " + entry.getKey() + "(" + entry.getValue() + ")");
        }
        System.out.println("Biography : " + this.biography);
        System.out.println();
    }

    public int compareTo(Actor actor) {
        if(actor == null) {
            System.out.println("Null argument");
            return 1;
        }
        return this.name.compareTo((actor.name));
    }

    //      Strategy Pattern
    @Override
    public int calculateEXP() {
        return 2;
    }
}