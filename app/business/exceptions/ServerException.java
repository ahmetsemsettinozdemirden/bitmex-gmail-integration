package business.exceptions;

public class ServerException extends Exception {

    private String errorCode;

    public ServerException(String errorCode, Exception e) {
        super(e);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
