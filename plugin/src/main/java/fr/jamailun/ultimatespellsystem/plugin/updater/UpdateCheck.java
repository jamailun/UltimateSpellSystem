package fr.jamailun.ultimatespellsystem.plugin.updater;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

/**
 * Used to test for a more recent version.
 */
public final class UpdateCheck {
    private UpdateCheck() {}

    private static final String URL_SOURCE = "https://api.github.com/repos/jamailun/UltimateSpellSystem/releases/latest";
    private static String version;

    public static @NotNull String getPublicUrl() {
        return "https://github.com/jamailun/UltimateSpellSystem/releases/latest";
    }

    /**
     * Test if a more recent version exist.
     * @return an optional containing the latest version, if it exists.
     */
    public static Optional<String> getLatestRelease() {
        String pluginVersion = UltimateSpellSystem.getVersion();
        String latest = getLatestVersion();
        if(isVersionMoreRecent(pluginVersion, latest)) {
            return Optional.of(latest);
        }
        return Optional.empty();
    }

    public static @Nullable String getLatestVersion() {
        if(version == null) {
            version = getLatestVersionOnGithub();
        }
        return version;
    }

    private static @Nullable String getLatestVersionOnGithub() {
        String json = doGithubCallOnRepository();
        if(json == null)
            return null;
        return new JSONObject(json).getString("tag_name");
    }

    private static @Nullable String doGithubCallOnRepository() {
        try {
            URL url = URI.create(URL_SOURCE).toURL();
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder output = new StringBuilder();
            String str;
            while ((str = br.readLine()) != null) {
                output.append(str);
            }
            br.close();
            return output.toString();
        } catch(Exception e) {
            System.err.println("Could not fetch latest plugin version on " + URL_SOURCE);
            return null;
        }
    }

    private static boolean isVersionMoreRecent(@NotNull String current, @Nullable String latest) {
        if(latest == null)
            return false;
        return latest.compareTo(current) > 0;
    }
}
