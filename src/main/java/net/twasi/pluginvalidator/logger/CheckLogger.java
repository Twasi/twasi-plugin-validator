package net.twasi.pluginvalidator.logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckLogger {

    private String checkName;

    private Map<String, String> properties = new HashMap<>();
    private List<String> warnings = new ArrayList<>();

    public CheckLogger(String checkName) {
        this.checkName = checkName;
        System.out.println("\n[Check] " + checkName);
    }

    public void property(String name, Object val) {
        properties.put(name, String.valueOf(val));
    }

    public void warning(String warning) {
        warnings.add(warning);
    }

    public void info(String info) {
        System.out.println("[Info] " + info);
    }

    public void print() {
        System.out.println("\n[CheckResults] " + checkName);
        System.out.println("[Warnings]");
        warnings.forEach(System.out::println);
        System.out.println("\n[Properties]");
        properties.forEach((name, val) -> System.out.println(name + " = " + val));
    }

}
