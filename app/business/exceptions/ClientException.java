package business.exceptions;

public class ClientException extends Exception {

    private String errorCode;

    public ClientException(String errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
