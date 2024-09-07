public class UserFactory {
    public static User Factory(AccountType type) {
        return switch (type) {
            case Admin -> new Admin();
            case Contributor -> new Contributor();
            case Regular -> new Regular();
        };
    }
}
