package app.database.exceptions;

import java.util.ArrayList;
import java.util.List;

public class HotelExceptionBuilder {
    private List<String> errors;

    public HotelExceptionBuilder(){
        this.errors = new ArrayList<>();
    }

    public HotelExceptionBuilder addError(String message){
        errors.add(message);
        return this;
    }

    public boolean isError(){
        return !errors.isEmpty();
    }

    public HotelException build(){
        HotelException chatbotException = new HotelException();
        chatbotException.setErrors(errors);
        return chatbotException;
    }
}
