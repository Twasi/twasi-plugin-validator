package net.twasi.pluginvalidator.checks;

import net.twasi.pluginvalidator.exceptions.CheckException;
import net.twasi.pluginvalidator.exceptions.EvaluationException;
import net.twasi.pluginvalidator.logger.CheckLogger;

public abstract class BaseCheck<T> {

    protected final CheckLogger logger = new CheckLogger(getCheckName());

    public String getCheckName() {
        return getClass().getSimpleName();
    }

    public final T run() throws CheckException {
        T t;
        try {
            t = runCheck();
        } catch (Exception e) {
            throw new CheckException(e.getMessage());
        }
        try {
            t = evaluate(t);
        } catch (Exception e) {
            throw new EvaluationException(e.getMessage());
        }
        logger.print();
        return t;
    }

    protected abstract T runCheck() throws CheckException;

    protected abstract T evaluate(T result) throws EvaluationException;

}
