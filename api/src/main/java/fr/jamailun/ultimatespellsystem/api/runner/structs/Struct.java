package fr.jamailun.ultimatespellsystem.api.runner.structs;

import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Instance of a structured object.
 */
public interface Struct {

    /**
     * Get a field value.
     * @param pos token position requesting the data.
     * @param fieldName name of the field.
     * @return the field value.
     */
    @Nullable Object getField(@NotNull TokenPosition pos, @NotNull String fieldName);

    /**
     * Set a field value.
     * @param pos token position requesting the data.
     * @param fieldName name of the field.
     * @param value the field new value.
     */
    void setField(@NotNull TokenPosition pos, @NotNull String fieldName, @Nullable Object value);

    /**
     * Call a function of the object.
     * @param pos token position requesting the data.
     * @param functionName name fo the function.
     * @param parameters parameters of the function.
     * @return the function output.
     */
    @Nullable Object callFunction(@NotNull TokenPosition pos, @NotNull String functionName, @NotNull List<Object> parameters);

}
