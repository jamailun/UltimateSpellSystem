package fr.jamailun.ultimatespellsystem.extension.citizens;

import com.google.common.base.Preconditions;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.plugin.configuration.UssConfig;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@TraitName("uss-caster")
public class CasterTrait extends Trait implements SpellEntity {

    private int tickRate;

    // Persisted manually
    private final List<SpellCastRule> rules = new ArrayList<>();

    private transient int tickCounter = 0;

    public CasterTrait() {
        super("uss-caster");
        tickRate = UssConfig.getTicksCitizensTrait();
    }

    public void cast(@NotNull Spell spell) {
        spell.castNotCancellable(this);
    }

    public @NotNull List<SpellCastRule> getRules() {
        return rules;
    }

    @Override
    public void run() {
        if (!exists()) return;
        tickCounter++;
        if (tickCounter >= tickRate) {
            tickCounter = 0;
            runUpdate();
        }
    }

    private boolean exists() {
        return npc.isSpawned() && npc.getEntity() != null && npc.getEntity().isValid() && npc.getEntity().getWorld().getPlayerCount() > 0;
    }

    /**
     * Change the tick rate. This change is transient.
     * @param newTickRate new value of the tick-rate.
     */
    public void changeTickRate(int newTickRate) {
        Preconditions.checkArgument(newTickRate > 0, "newTickRate must be > 0.");
        this.tickRate = newTickRate;
    }

    private void runUpdate() {
        for(SpellCastRule rule : rules) {
            if(rule.canExecute(this)) {
                Spell spell = UltimateSpellSystem.getSpellsManager().getSpell(rule.getSpellId());
                if(spell != null)
                    spell.castNotCancellable(this);
                return;
            }
        }
    }

    // persistence

    @Override
    public void load(@NotNull DataKey root) {
        for(DataKey key : root.getIntegerSubKeys()) {
            rules.add(SpellCastRule.create(key));
        }
    }

    @Override
    public void save(@NotNull DataKey key) {
        int i = 0;
        for(SpellCastRule rule : rules) {
            rule.save(key.getRelative(i ++));
        }
    }


    // -- Spell Entity

    @Override
    public @NotNull UUID getUniqueId() {
        return getBukkitEntity()
                .map(Entity::getUniqueId)
                .orElse(getNPC().getUniqueId());
    }

    @Override
    public @NotNull Optional<Entity> getBukkitEntity() {
        if(getNPC().isSpawned())
            return Optional.of(getNPC().getEntity());
        return Optional.empty();
    }

    @Override
    public @NotNull Location getLocation() {
        return getBukkitEntity().map(Entity::getLocation).orElse(getNPC().getStoredLocation());
    }

    @Override
    public @NotNull Location getEyeLocation() {
        return getBukkitEntity().map(LivingEntity.class::cast)
                .map(LivingEntity::getEyeLocation)
                .orElse(getNPC().getStoredLocation());
    }

    @Override
    public void teleport(@NotNull Location location) {
        if(getNPC().isSpawned())
            getNPC().teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Override
    public void remove() {
        if(getNPC().isSpawned())
            getNPC().despawn();
    }

    @Override
    public boolean isValid() {
        return getNPC().isSpawned();
    }

    @Override
    public void sendMessage(Component component) {
        // Nothing here
    }

    @Override
    public void addPotionEffect(PotionEffect effect) {
        getBukkitEntity().map(LivingEntity.class::cast)
                .ifPresent(le -> le.addPotionEffect(effect));
    }

    @Override
    public void setVelocity(@NotNull Vector vector) {
        getBukkitEntity().map(LivingEntity.class::cast)
            .ifPresent(le -> le.setVelocity(vector));
    }

    @Override
    public String toString() {
        return "CasterTrait(" + (npc==null?"unset":npc.getName()+")");
    }
}
