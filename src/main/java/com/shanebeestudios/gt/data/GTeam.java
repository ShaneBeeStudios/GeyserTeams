package com.shanebeestudios.gt.data;

public class GTeam {

    private String name;
    private String id;
    private String prefix;
    private String suffix;
    private String color;
    private int priority;

    public GTeam(String name, String prefix, String suffix, String color, int priority) {
        this.name = name;
        // Team names can only be 15 chars
        String i = "gt-" + priority + "-" + name;
        if (i.length() > 15) {
            i = i.substring(0, 15);
        }
        this.id = i;
        this.prefix = prefix;
        this.suffix = suffix;
        this.color = color;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getColor() {
        return color;
    }

    public int getPriority() {
        return priority;
    }

}
