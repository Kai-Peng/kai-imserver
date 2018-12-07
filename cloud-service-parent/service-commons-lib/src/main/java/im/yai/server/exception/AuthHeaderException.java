package im.kai.server.exception;


public class AuthHeaderException extends RuntimeException  {


    public static AuthHeaderException create() {
        return new AuthHeaderException();
    }
}
