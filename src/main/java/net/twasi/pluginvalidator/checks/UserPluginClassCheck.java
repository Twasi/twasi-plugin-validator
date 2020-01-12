package net.twasi.pluginvalidator.checks;

import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.pluginvalidator.exceptions.CheckException;
import net.twasi.pluginvalidator.exceptions.EvaluationException;

public class UserPluginClassCheck extends BaseCheck<Class<? extends TwasiUserPlugin>> {

    @Override
    public Class<? extends TwasiUserPlugin> runCheck() throws CheckException {
        return null;
    }

    @Override
    protected Class<? extends TwasiUserPlugin> evaluate(Class<? extends TwasiUserPlugin> result) throws EvaluationException {
        return null;
    }
}
