package net.twasi.pluginvalidator.checks;

import net.twasi.pluginvalidator.exceptions.CheckException;
import net.twasi.pluginvalidator.exceptions.EvaluationException;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

public class ClassLoaderCheck extends BaseCheck<URLClassLoader> {

    private final JarFile jarFile;
    private final File file;

    public ClassLoaderCheck(JarFile jarFile, File file) {
        this.jarFile = jarFile;
        this.file = file;
    }

    @Override
    protected URLClassLoader runCheck() throws CheckException {
        logger.info("CREATING-CLASSLOADER");
        try {
            URL[] urls = {new URL("jar:file:" + file.getAbsolutePath() + "!/")};
            URLClassLoader cl = URLClassLoader.newInstance(urls);
            return cl;
        } catch (Exception e) {
            throw new CheckException("CLASSLOADER-ERROR");
        }
    }

    @Override
    protected URLClassLoader evaluate(URLClassLoader result) throws EvaluationException {
        return result;
    }
}
