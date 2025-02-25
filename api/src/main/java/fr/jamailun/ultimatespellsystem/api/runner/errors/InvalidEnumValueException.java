package fr.jamailun.ultimatespellsystem.api.runner.errors;

public class InvalidEnumValueException extends UssRuntimeException {

    public InvalidEnumValueException(Class<?> enumClass, String value) {
        super("Invalid ENUM entry. For class " + enumClass + ", got value '" + value + "'.");
    }
}
