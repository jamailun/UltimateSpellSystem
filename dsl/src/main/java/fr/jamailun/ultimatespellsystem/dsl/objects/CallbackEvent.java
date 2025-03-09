package fr.jamailun.ultimatespellsystem.dsl.objects;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A type of callback event.
 * @param name the non-null name, used in the DSL. Must not contain any space.
 * @param argument optional argument value, able to be read in the callback scope.
 */
public record CallbackEvent(@NotNull String name, @Nullable CallbackArgument argument) {

    public CallbackEvent {
        if(name.indexOf(' ') > -1) {
            throw new RuntimeException("Invalid callback name. Cannot contain spaces.");
        }
    }

    /**
     * Create a new callback event from arguments.
     * @param name the non-null name of the callback. Must not contain any space.
     * @param keyword the keyword to use. Only used if the {@code type} is also non-null.
     * @param type the argument type. Only used if the {@code keyword} is also non-null.
     * @return a new instance of a callback.
     */
    @Contract("_,_,_ -> new")
    public static @NotNull CallbackEvent of(@NotNull String name, @Nullable TokenType keyword, @Nullable TypePrimitive type) {
        CallbackArgument arg = (keyword != null && type != null) ? new CallbackArgument(keyword, type) : null;
        return new CallbackEvent(name, arg);
    }

    /**
     * An argument, able to be read in the callback scope.
     * @param keyword
     * @param type
     */
    public record CallbackArgument(@NotNull TokenType keyword, @NotNull TypePrimitive type) {
        public CallbackArgument {
            if(!keyword().letters)
                throw new RuntimeException("Invalid keyword type. Should be a 'letters' keyword.");
            if(type == TypePrimitive.NULL)
                throw new RuntimeException("Invalid callback argument type. Cannot be null.");
        }
    }

}
