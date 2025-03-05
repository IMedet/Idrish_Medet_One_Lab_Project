package kz.medet.exceptions;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String attribute, String fieldName, String valueName){
        super(attribute + " with " + fieldName + ": " + valueName + " already exist");
    }
}
