package im.youdu.sdk.exception;


public class HttpRequestException extends GeneralEntAppException {

    private final int errCode;

    public HttpRequestException(int errCode, String message, Throwable cause) {
        super(message, cause);
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }
}
