package fr.jamailun.ultimatespellsystem.plugin.bind;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellBindData;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.plugin.bind.costs.SpellCostFactory;
import fr.jamailun.ultimatespellsystem.plugin.spells.NotFoundSpell;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SpellBindDataSerializer {
    private SpellBindDataSerializer() {}

    /**
     * Encode a {@link SpellBindData} to base 64.
     * @param data a non-null container.
     * @return a base64 string.
     */
    public static @NotNull String encodeSpellBindData(@NotNull SpellBindDataContainer data) {
        List<JSONObject> serialized =  data.list().stream().map(SpellBindDataSerializer::serialize).toList();
        String arrayToString = new JSONArray(serialized).toString();
        return Base64.getEncoder().encodeToString(arrayToString.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decode a base64 string into a {@link SpellBindData}.
     * @param string a non-null string.
     * @return a new spell-bind-data instance.
     */
    public static @NotNull SpellBindDataContainer decodeSpellBindData(@NotNull String string) {
        byte[] rawBytes = Base64.getDecoder().decode(string);
        String rawString = new String(rawBytes, StandardCharsets.UTF_8);
        JSONArray array = new JSONArray(rawString);
        List<SpellBindData> list = array.toList().stream()
                .map(object -> switch(object) {
                    case Map<?,?> map -> new JSONObject(map);
                    case String str -> new JSONObject(str);
                    case JSONObject j -> j;
                    default -> throw new IllegalStateException("Unknown type in data. JSON = " + rawString);
                })
                .map(SpellBindDataSerializer::deserialize)
                .toList();
        return new SpellBindDataContainer(list);
    }

    public static @NotNull JSONObject serialize(@NotNull SpellBindData data) {
        JSONObject json = new JSONObject();
        json.put("spell", data.getSpellId());
        json.put("cost", SpellCostFactory.serialize(data.getCost()));
        json.put("trigger", new JSONArray(data.getTrigger().getTriggersList().stream().map(ItemBindTrigger::name).toList()));
        json.put("cooldown", serialize(data.getCooldown()));
        return json;
    }

    public static @NotNull SpellBindData deserialize(@NotNull JSONObject json) {
        String spellId = json.getString("spell");
        Spell spell = Objects.requireNonNullElseGet(
                UltimateSpellSystem.getSpellsManager().getSpell(spellId),
                () -> new NotFoundSpell(spellId)
        );

        JSONObject rawCost = json.getJSONObject("cost");
        List<ItemBindTrigger> triggersRaw = json.getJSONArray("trigger").toList().stream()
                .map(String.class::cast)
                .map(ItemBindTrigger::valueOf)
                .toList();
        String cooldown = json.getString("cooldown");
        return new SpellBindDataImpl(
                spell,
                new SpellTriggerImpl(triggersRaw, SpellCostFactory.deserialize(rawCost)),
                deserializeDuration(cooldown)
        );
    }

    private static @NotNull String serialize(@Nullable Duration duration) {
        if(duration == null) return "";
        return duration.amount() + ";" + duration.timeUnit().name();
    }

    private static @Nullable Duration deserializeDuration(@NotNull String raw) {
        if(raw.isEmpty()) return null;
        String[] tokens = raw.split(";");
        double amount = Double.parseDouble(tokens[0]);
        TimeUnit unit  = TimeUnit.valueOf(tokens[1]);
        return new Duration(amount, unit);
    }

}
