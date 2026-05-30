package fr.jamailun.ultimatespellsystem.extension.mythicMobs;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.INoTargetSkill;
import io.lumine.mythic.api.skills.SkillCaster;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.io.File;
import java.util.Optional;

/**
 * Generic skill, usable by MM. It will cast a spell from USS when triggered.
 */
public class MythicMobSkill extends SkillMechanic implements INoTargetSkill {

  private final String spellId;

  public MythicMobSkill(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
    super(manager, file, line, mlc);
    spellId = mlc.getString("spell", "s");
  }

  @Override
  public SkillResult cast(SkillMetadata skillMetadata) {
    var spell = UltimateSpellSystem.getSpellsManager().getSpell(spellId);
    if(spell == null) {
      UssLogger.logWarning("MythicMobSkill: Spell with id " + spellId + " not found.");
      return SkillResult.ERROR;
    }
    Entity bukkitCaster = Optional.ofNullable(skillMetadata.getCaster())
        .map(SkillCaster::getEntity)
        .map(AbstractEntity::getBukkitEntity)
        .orElse(null);
    if(bukkitCaster == null || !bukkitCaster.isValid()) {
      UssLogger.logWarning("MythicMobSkill: Spell " + spellId + " cannot cast without valid caster.");
      return SkillResult.ERROR;
    }
    if(!(bukkitCaster instanceof LivingEntity livingCaster)) {
      UssLogger.logWarning("MythicMobSkill: Spell " + spellId + " cannot cast without valid caster (Not LivingEntity).");
      return SkillResult.ERROR;
    }

    spell.castNotCancellable(livingCaster);
    return SkillResult.SUCCESS;
  }
}
