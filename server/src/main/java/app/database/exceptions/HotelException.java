package app.database.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class HotelException extends RuntimeException{
    private List<String> errors;
}
