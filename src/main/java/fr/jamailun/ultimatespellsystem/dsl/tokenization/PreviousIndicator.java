package fr.jamailun.ultimatespellsystem.dsl.tokenization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is <b>declarative-only</b>. No check will be done.
 *<br />
 * Used to indicate to the developer what cas the expected previous token type.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface PreviousIndicator {

    TokenType[] expected();

}
