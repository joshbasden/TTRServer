package Plugin;

public class PluginDescriptor {
    private String name;
    private String className;
    private String classPath;

    public PluginDescriptor(String name, String className, String classPath) {
        this.name = name;
        this.className = className;
        this.classPath = classPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }
}
