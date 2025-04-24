package fr.jamailun.examples.commands;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.ScoreboardTrait;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;

/**
 * You can ignore that, I'm just messing around with tags.
 */
public class NpcTagCommand implements CommandExecutor, TabCompleter {

    private static final List<String> ARGS = List.of("list", "add", "remove");

    public NpcTagCommand() {
        PluginCommand command = Objects.requireNonNull(Bukkit.getPluginCommand("tag.npc"));
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        // Always need an NPC
        NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);
        if(npc == null) {
            sender.sendMessage("§cYou need to select an NPC first.");
            return true;
        }
        ScoreboardTrait scoreboardTrait = npc.getOrAddTrait(ScoreboardTrait.class);
        Set<String> tags = getTags(scoreboardTrait);

        // List
        if(args.length == 0 || "list".equalsIgnoreCase(args[0])) {
            sender.sendMessage("§aNPC tags: §7" + tags);
            return true;
        }

        if("remove".equalsIgnoreCase(args[0])) {
            if(args.length < 2) {
                sender.sendMessage("§cMissing argument: tag to remove.");
                return true;
            }
            String tag = args[1];
            if(tags.remove(tag)) {
                scoreboardTrait.setTags(tags);
                sender.sendMessage("§aTag successfully removed.");
            } else {
                sender.sendMessage("§cTag not found.");
            }
            return true;
        }

        if("add".equalsIgnoreCase(args[0])) {
            if(args.length < 2) {
                sender.sendMessage("§cMissing argument: tag to add.");
                return true;
            }
            String tag = args[1];
            if(tags.add(tag)) {
                scoreboardTrait.setTags(tags);
                sender.sendMessage("§aTag successfully added.");
            } else {
                sender.sendMessage("§cTag already exists.");
            }
            return true;
        }

        sender.sendMessage("§cInvalid sub-command. Use one of: " + ARGS);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length == 1) {
            String arg0 = args[0].toLowerCase();
            return ARGS.stream()
                    .filter(a -> a.contains(arg0))
                    .toList();
        }

        if(args.length == 2) {
            NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);
            if(npc != null && "remove".equalsIgnoreCase(args[0])) {
                return getTags(npc.getOrAddTrait(ScoreboardTrait.class))
                        .stream()
                        .filter(i -> i.contains(args[1]))
                        .toList();
            }
        }
        return List.of();
    }

    private static Field TAGS_FIELD;
    @SuppressWarnings("all")
    private Set<String> getTags(ScoreboardTrait trait) {
        if(TAGS_FIELD == null) {
            try {
                TAGS_FIELD = ScoreboardTrait.class.getDeclaredField("tags");
                Objects.requireNonNull(TAGS_FIELD, "Field 'ScoreboardTrait#tags' does not exists in this version.");
            } catch(Exception e) {
                throw new RuntimeException("Could not get field ScoreboardTrait#tags.");
            }
        }
        TAGS_FIELD.setAccessible(true);
        try {
            return new HashSet<>((Set<String>) TAGS_FIELD.get(trait));
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Coult not get value of ScoreboardTrait#tags for instance " + trait, e);
        }
    }
}
