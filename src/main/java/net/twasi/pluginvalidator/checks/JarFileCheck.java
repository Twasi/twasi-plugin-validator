package net.twasi.pluginvalidator.checks;

import net.twasi.pluginvalidator.exceptions.CheckException;
import net.twasi.pluginvalidator.exceptions.EvaluationException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarFileCheck extends BaseCheck<JarFile> {

    public final List<String> files = new ArrayList<>();
    public final File file;

    public JarFileCheck(String path) {
        this.file = new File(path);
    }

    @Override
    public JarFile runCheck() throws CheckException {
        logger.info("SEARCHING-JARFILE");
        if (!file.exists() || !file.isFile())
            throw new CheckException("JARFILE-NOT-EXISTING");
        if (!file.getName().toLowerCase().endsWith(".jar"))
            throw new CheckException("FILE-IS-NOT-JARFILE");

        JarFile jarFile;
        try {
            logger.info("OPENING-JARFILE");
            jarFile = new JarFile(file);
            logger.property("jar-file", jarFile.getName());
        } catch (IOException e) {
            throw new CheckException(e.getMessage());
        }
        return jarFile;
    }

    @Override
    protected JarFile evaluate(JarFile jarFile) throws EvaluationException {
        JarEntry entry;
        Enumeration<JarEntry> entries = jarFile.entries();
        logger.info("SCANNING-CONTENTS");
        try {
            while ((entry = entries.nextElement()) != null)
                files.add(entry.getName());
        } catch (Exception ignored) {
        }
        logger.property("found-files", files.size());

        if (files.stream().anyMatch(f -> f.startsWith("net/twasi/core/")))
            logger.warning("CONTAINS-CORE");
        return jarFile;
    }
}
