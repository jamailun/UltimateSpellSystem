package fr.jamailun.ultimatespellsystem.api.runner.errors;

/**
 * Unexpected enumeration value. <br/>
 * This is a <b>user</b> exception.
 */
public class InvalidEnumValueException extends UssRuntimeException {

    /**
     * New instance.
     * @param enumClass class of the enum.
     * @param value value of the user that does not exist.
     */
    public InvalidEnumValueException(Class<?> enumClass, String value) {
        super("Invalid ENUM entry. For class " + enumClass + ", got value '" + value + "'.");
    }
}
