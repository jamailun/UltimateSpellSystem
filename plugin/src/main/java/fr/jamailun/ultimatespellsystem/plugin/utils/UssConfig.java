package fr.jamailun.ultimatespellsystem.plugin.utils;

import fr.jamailun.ultimatespellsystem.plugin.utils.observable.AbstractObservable;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.jetbrains.annotations.NotNull;

@Getter
public class UssConfig extends AbstractObservable<UssConfig> {

    private boolean debug;
    private boolean triggerLeftClickAir;
    private boolean triggerLeftClickBlock;
    private boolean triggerRightClickAir;
    private boolean triggerRightClickBlock;
    private boolean triggerAttack;
    private boolean ignoreTriggerOnSneak;
    private boolean ignoreTriggerOnSprint;
    private boolean afterTriggerUseItem;
    private boolean afterTriggerUseBlock;

    private double checkSummonsAggroEverySeconds;

    public void reload(@NotNull ConfigurationSection config) {
        debug = config.getBoolean("debug", false);

        triggerLeftClickAir = config.getBoolean("trigger.left-click.air", false);
        triggerLeftClickBlock = config.getBoolean("trigger.left-click.block", false);
        triggerRightClickAir = config.getBoolean("trigger.right-click.air", true);
        triggerRightClickBlock = config.getBoolean("trigger.right-click.block", true);
        triggerAttack = config.getBoolean("trigger.attack", false);
        ignoreTriggerOnSneak = config.getBoolean("trigger.ignore.sneak", true);
        ignoreTriggerOnSprint = config.getBoolean("trigger.ignore.sprint", false);

        afterTriggerUseItem = config.getBoolean("after-trigger.use-item", true);
        afterTriggerUseBlock = config.getBoolean("after-trigger.use-block", true);

        checkSummonsAggroEverySeconds = config.getDouble("tick.aggro.summons", 5d);

        callObservers(this);
    }

    public boolean doesTriggerInteract(@NotNull Action action, @NotNull Player player) {
        if((player.isSneaking() && ignoreTriggerOnSneak) || (player.isSprinting() && ignoreTriggerOnSprint))
            return false;
        return switch (action) {
            case PHYSICAL -> false;
            case LEFT_CLICK_AIR -> triggerLeftClickAir;
            case LEFT_CLICK_BLOCK -> triggerLeftClickBlock;
            case RIGHT_CLICK_AIR -> triggerRightClickAir;
            case RIGHT_CLICK_BLOCK -> triggerRightClickBlock;
        };
    }
    public boolean doesTriggerAttack(@NotNull Player player) {
        if((player.isSneaking() && ignoreTriggerOnSneak) || (player.isSprinting() && ignoreTriggerOnSprint))
            return false;
        return triggerAttack;
    }

}
