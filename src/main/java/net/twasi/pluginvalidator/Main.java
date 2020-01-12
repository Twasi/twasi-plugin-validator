package net.twasi.pluginvalidator;

import net.twasi.core.plugin.PluginConfig;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.pluginvalidator.checks.ClassLoaderCheck;
import net.twasi.pluginvalidator.checks.ConfigCheck;
import net.twasi.pluginvalidator.checks.JarFileCheck;
import net.twasi.pluginvalidator.checks.PluginClassCheck;
import net.twasi.pluginvalidator.exceptions.CheckException;
import net.twasi.pluginvalidator.exceptions.EvaluationException;

import java.net.URLClassLoader;
import java.util.jar.JarFile;

public class Main {

    public static void main(String[] args) {
        try {
            // JarFile Check
            JarFileCheck jfCheck = new JarFileCheck(String.join(" ", args));
            JarFile jarFile = jfCheck.run();

            // Config Check
            ConfigCheck cfgCheck = new ConfigCheck(jarFile, jfCheck.files);
            PluginConfig cfg = cfgCheck.run();

            // ClassLoader Check
            ClassLoaderCheck clCheck = new ClassLoaderCheck(jarFile, jfCheck.file);
            URLClassLoader classLoader = clCheck.run();

            // PluginClass Check
            PluginClassCheck pcCheck = new PluginClassCheck(classLoader, cfg);
            Class<? extends TwasiPlugin<?>> twasiPluginClass = pcCheck.run();

        } catch (CheckException c) {
            if (c instanceof EvaluationException) {
                System.out.println("[EvaluationError] " + c.getMessage());
            } else {
                System.out.println("[CheckError] " + c.getMessage());
            }
        }
    }

}
