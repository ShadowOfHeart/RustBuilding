package UA.RustFlex.buildings.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

public class DiscordLogger implements Listener {

    private final String webhookUrl;

    public DiscordLogger(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String playerName = event.getEntity().getName();
        Vector location = event.getEntity().getLocation().toVector();
        String message = String.format("**%s** has died at coordinates: x: %.2f, y: %.2f, z: %.2f",
                playerName, location.getX(), location.getY(), location.getZ());

        sendToDiscord(message);
    }

    private void sendToDiscord(String message) {
        String jsonPayload = String.format("{\"content\": \"%s\"}", message);

        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                Bukkit.getLogger().info("Successfully sent death log to Discord.");
            } else {
                Bukkit.getLogger().warning("Failed to send death log to Discord. Response Code: " + responseCode);
            }

        } catch (Exception e) {
            Bukkit.getLogger().severe("Error while sending message to Discord: " + e.getMessage());
        }
    }
}
