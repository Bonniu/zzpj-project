package app.database.exceptions.validations;

import app.database.exceptions.HotelException;
import app.database.exceptions.HotelExceptionBuilder;
import app.database.exceptions.messages.ReservationErrorMessages;

import java.time.LocalDate;
import java.util.HashMap;

public class ReservationValidator implements Validator<HashMap<String, String>> {

    @Override
    public void validateInsert(HashMap<String, String> param) throws HotelException {
        if (LocalDate.parse(param.get("setStartDate")).isAfter(LocalDate.parse(param.get("setEndDate"))) ||
                LocalDate.parse(param.get("setStartDate")).isEqual(LocalDate.parse(param.get("setEndDate"))))
            throw new HotelExceptionBuilder().addError(ReservationErrorMessages.WRONG_DATE).build();

    }

    @Override
    public void validateUpdate(HashMap<String, String> param) throws HotelException {
        validateInsert(param);
    }
}
