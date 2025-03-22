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
     * @param keyword the keyword to use for the argument.
     * @param type the argument type of the argument.
     * @return a new instance of a callback.
     */
    @Contract("_,_,_ -> new")
    public static @NotNull CallbackEvent of(@NotNull String name, @NotNull TokenType keyword, @NotNull TypePrimitive type) {
        return new CallbackEvent(name, new CallbackArgument(keyword, type));
    }

    /**
     * Create a new callback event without argument.
     * @param name the non-null name of the callback. Must not contain any space.
     * @return a new instance of a callback.
     */
    @Contract("_ -> new")
    public static @NotNull CallbackEvent of(@NotNull String name) {
        return new CallbackEvent(name, null);
    }

    /**
     * An argument, able to be read in the callback scope.
     * @param keyword keyword used by the argument.
     * @param type type of the argument.
     */
    public record CallbackArgument(@NotNull TokenType keyword, @NotNull TypePrimitive type) {
        public CallbackArgument {
            if(!keyword.letters)
                throw new RuntimeException("Invalid keyword type. Should be a 'letters' keyword.");
            if(type == TypePrimitive.NULL)
                throw new RuntimeException("Invalid callback argument type. Cannot be null.");
        }
    }

}
