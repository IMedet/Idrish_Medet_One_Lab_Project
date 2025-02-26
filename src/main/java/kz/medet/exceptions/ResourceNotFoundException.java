package kz.medet.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String attribute, String fieldName, Long fieldValue) {
        super(attribute + " with " + fieldName + ": " + fieldValue);
    }
}
