import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gerli on 26/09/2017.
 */
public class CheckUsername {

    private final String USERNAME_PATTERN = "^[A-Za-z0-9_-]{1,12}$";
    private Pattern pattern;
    private Matcher matcher;

    public CheckUsername() {
        pattern = Pattern.compile(USERNAME_PATTERN);
    }

    public boolean validate(final String username) {
        matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public boolean unique(final String username) {
        Set<ServerThread> threadList = Server.getThreadList();
        boolean result = false;
        for (ServerThread t : threadList) {
            String name = t.toString();
            if (name.equals(username)) {
                result = false;
            } else {
                result = true;
            }
        }
        return result;
    }
}
