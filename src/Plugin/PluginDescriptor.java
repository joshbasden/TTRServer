package Plugin;

public class PluginDescriptor {
    private String name;
    private String className;
    private String jarName;
    private String jarPath;

    public PluginDescriptor(String name, String jarPath, String jarName, String className) {
        this.name = name;
        this.className = className;
        this.jarName = jarName;
        this.jarPath = jarPath;
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

    public String getJarName() {
        return jarName;
    }

    public void setJarName(String jarName) {
        this.jarName = jarName;
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }
}
