package org.jandroid.common.autoupdate;

/**
* @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
*/
public class VersionInfo {

    private String name;
    private String version;
    private String description;
    private String update;

    private String downloadUrl;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "VersionInfo{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", description='" + description + '\'' +
                ", update='" + update + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}
