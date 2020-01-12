package net.twasi.pluginvalidator.checks;

import net.twasi.core.plugin.PluginConfig;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.pluginvalidator.exceptions.CheckException;
import net.twasi.pluginvalidator.exceptions.EvaluationException;

import java.lang.reflect.Constructor;
import java.net.URLClassLoader;

public class PluginClassCheck extends BaseCheck<Class<? extends TwasiPlugin<?>>> {

    private URLClassLoader loader;
    private final PluginConfig cfg;

    public TwasiPlugin<?> pluginInstance;

    public PluginClassCheck(URLClassLoader loader, PluginConfig cfg) {
        this.loader = loader;
        this.cfg = cfg;
    }

    @Override
    public Class<? extends TwasiPlugin<?>> runCheck() throws CheckException {
        logger.info("LOADING-TWASIPLUGIN-CLASS");
        Class<?> anyClass;
        try {
            anyClass = loader.loadClass(cfg.main);
        } catch (ClassNotFoundException e) {
            throw new CheckException("CANNOT-LOAD-MAIN");
        }
        if (!TwasiPlugin.class.isAssignableFrom(anyClass))
            throw new CheckException("TWASIPLUGINCLASS-NOT-EXTENDED");
        return (Class<TwasiPlugin<?>>) anyClass;
    }

    @Override
    protected Class<? extends TwasiPlugin<?>> evaluate(Class<? extends TwasiPlugin<?>> result) throws EvaluationException {
        if (result.getTypeParameters().length == 0)
            logger.warning("NO-PLUGIN-CONFIGURATION");

        Constructor<? extends TwasiPlugin<?>> declaredConstructor;
        try {
            declaredConstructor = result.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new EvaluationException("NO-TWASIPLUGIN-DEFAULT-CONSTRUCTOR");
        }
        try {
            pluginInstance = declaredConstructor.newInstance();
        } catch (Exception e) {
            throw new EvaluationException("TWASIPLUGIN-INSTANTIATION-EXCEPTION");
        }

        if (!(cfg.api == null || cfg.api.trim().equals(""))) {
            if (pluginInstance.getGraphQLResolver() == null)
                throw new EvaluationException("API-DEFINED-BUT-NO-RESOLVER");
        } else if (pluginInstance.getGraphQLResolver() != null)
            logger.warning("API-UNDEFINED-BUT-HAS-RESOLVER");

        if (!cfg.dependency && pluginInstance.getUserPluginClass() == null)
            throw new EvaluationException("NO-USERPLUGIN-CLASS");

        return result;
    }
}
