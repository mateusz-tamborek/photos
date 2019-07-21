package pl.com.tambo.photos.core.exception;

public class StoreImageException extends RuntimeException {
    public StoreImageException(Throwable e) {
        super(e);
    }
}