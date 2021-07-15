package webapp.dso;


public final class Session {

    private static final SessionBcf _current = new SessionBcf();
    public static SessionBcf current(){
        return _current;
    }
}
