package net.twasi.pluginvalidator.checks;

import net.twasi.core.plugin.PluginConfig;
import net.twasi.pluginvalidator.exceptions.CheckException;
import net.twasi.pluginvalidator.exceptions.EvaluationException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ConfigCheck extends BaseCheck<PluginConfig> {

    private JarFile jarFile;
    private List<String> files;

    public ConfigCheck(JarFile jarFile, List<String> files) {
        this.jarFile = jarFile;
        this.files = files;
    }

    @Override
    public PluginConfig runCheck() throws CheckException {
        logger.info("SEARCHING-PLUGIN-YML");
        if (!files.contains("plugin.yml"))
            throw new CheckException("NO-PLUGIN-YML");

        logger.info("LOADING-PLUGIN-YML");
        PluginConfig c;
        try {
            c = PluginConfig.fromInputStream(jarFile.getInputStream(new ZipEntry("plugin.yml")));
        } catch (Exception e) {
            throw new CheckException("CORRUPT-PLUGIN-YML");
        }
        return c;
    }

    @Override
    protected PluginConfig evaluate(PluginConfig result) throws EvaluationException {
        logger.info("CHECKING-PLUGIN-YML");
        if (result.name == null)
            throw new EvaluationException("NO-PLUGIN-NAME");
        if (result.author == null)
            throw new EvaluationException("NO-PLUGIN-AUTHOR");
        if (result.main == null)
            throw new EvaluationException("NO-PLUGIN-MAIN");
        if (result.version == null)
            throw new EvaluationException("NO-PLUGIN-VERSION");

        if (result.autoInstall)
            logger.warning("PLUGIN-AUTO-INSTALL");
        if (result.hidden)
            logger.warning("PLUGIN-HIDDEN");
        if (result.dynamicCommandNames)
            logger.warning("PLUGIN-DYNAMIC-COMMANDS");

        for (Field declaredField : PluginConfig.class.getDeclaredFields()) {
            try {
                logger.property(declaredField.getName(), declaredField.get(result));
            } catch (IllegalAccessException ignored) {
                // Will never be thrown
            }
        }
        return result;
    }

}
