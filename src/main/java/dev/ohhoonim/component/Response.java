package dev.ohhoonim.component;

/** 
 * 
 * @see dev.ohhoonim.component.model.payload.DefaultResponseHandler 
 **/ 
public sealed interface Response  {

    public record Success<T>(
        ResponseCode code,
        T data
    ) implements Response { }

    public record Fail<T> (
        ResponseCode code,
        String message,
        T data
    ) implements Response { }
}
