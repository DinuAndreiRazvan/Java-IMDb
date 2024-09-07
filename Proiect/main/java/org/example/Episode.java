public class Episode {
    String episodeName;
    String duration;

    public Episode(String episodeName, String duration) {
        this.episodeName = episodeName;
        this.duration = duration;
    }

    public void printInfo() {
        System.out.println("    Title : " + this.episodeName);
        System.out.println("    Duration : " + this.duration);
    }
}