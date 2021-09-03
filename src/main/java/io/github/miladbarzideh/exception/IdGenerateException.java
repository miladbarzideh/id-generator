package io.github.miladbarzideh.exception;


public class IdGenerateException extends RuntimeException {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -27048199131316992L;

    /**
     * Default constructor
     */
    public IdGenerateException() {
        super();
    }

    /**
     * Constructor with message
     *
     * @param message message
     */
    public IdGenerateException(String message) {
        super(message);
    }

    /**
     * Constructor with message & cause
     *
     * @param message message
     * @param cause   cause
     */
    public IdGenerateException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with message format
     *
     * @param msgFormat message format
     * @param args      argument
     */
    public IdGenerateException(String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
    }

    /**
     * Constructor with cause
     *
     * @param cause cause
     */
    public IdGenerateException(Throwable cause) {
        super(cause);
    }
}
