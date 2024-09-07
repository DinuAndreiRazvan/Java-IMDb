public interface Subject {
    public void addObserver(User user);
    public void deleteObserver(User user);
    public void notifyObservers();
}
