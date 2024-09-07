public class Rating implements ExperienceStrategy, Comparable<Rating> {
    String username;
    Long evaluation;
    String comment;

    public Rating(String username, Long rating, String comment) {
        this.username = username;
        this.evaluation = rating;
        this.comment = comment;
    }

    public void printRating() {
        System.out.println("    Username : " + this.username);
        System.out.println("    Evaluation : " + this.evaluation);
        System.out.println("    Comment : " + this.comment);
        System.out.println("-------");
    }

    @Override
    public int compareTo(Rating rating) {
        IMDB imdb = IMDB.getInstance();
        User thisUser = imdb.searchUser(this.username);
        User user = imdb.searchUser(rating.username);
        return thisUser.getExperience() - user.getExperience();
    }

    //      Strategy Pattern
    @Override
    public int calculateEXP() {
        return 1;
    }
}