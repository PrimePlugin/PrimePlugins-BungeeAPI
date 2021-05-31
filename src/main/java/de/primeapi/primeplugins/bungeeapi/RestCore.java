package de.primeapi.primeplugins.bungeeapi;

import de.primeapi.primeplugins.bungeeapi.api.RestPlugin;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author Lukas S. PrimeAPI
 * created on 31.05.2021
 * crated for PrimePlugins
 */
public class RestCore extends RestPlugin {
    public RestCore(Plugin plugin) {
        super("BungeeAPI", plugin);
    }


    @Override
    public void downloadLatestVersion(String path) {
        try {
            String url = "https://cp.primeapi.de/api.php?action=downloadCore&type=bungee";
            InputStream in = new URL(url).openStream();
            Files.copy(in, Paths.get(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getPath()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
