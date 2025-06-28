package fr.jamailun.ultimatespellsystem.plugin.bind.trigger;

import fr.jamailun.ultimatespellsystem.api.bind.SpellBindData;
import fr.jamailun.ultimatespellsystem.api.bind.SpellsTriggerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Result of an action.
 * @param result the result to apply.
 * @param data optional data.
 */
public record ActionRes(
        @NotNull SpellsTriggerManager.ActionResult result,
        @Nullable SpellBindData data
) {

    public static @NotNull ActionRes ignored() {
        return new ActionRes(SpellsTriggerManager.ActionResult.IGNORED, null);
    }

    public static @NotNull ActionRes success(@NotNull SpellBindData data) {
        return new ActionRes(SpellsTriggerManager.ActionResult.SPELL_CAST, data);
    }

    public static @NotNull ActionRes okWithoutSpell() {
        return new ActionRes(SpellsTriggerManager.ActionResult.STEP_VALID, null);
    }

}
