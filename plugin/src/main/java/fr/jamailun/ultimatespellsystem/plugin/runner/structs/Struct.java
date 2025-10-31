package fr.jamailun.ultimatespellsystem.plugin.runner.structs;

import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

}
