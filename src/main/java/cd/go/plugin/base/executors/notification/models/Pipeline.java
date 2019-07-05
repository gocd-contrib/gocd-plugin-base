package cd.go.plugin.base.executors.notification.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pipeline {
    @SerializedName("name")
    private String name;

    @SerializedName("counter")
    private String counter;

    @SerializedName("group")
    private String group;

    @SerializedName("build-cause")
    private List<BuildCause> buildCause;

    @SerializedName("stage")
    private Stage stage;

    @SerializedName("label")
    private String label;

    public String getName() {
        return name;
    }

    public String getCounter() {
        return counter;
    }

    public String getGroup() {
        return group;
    }

    public List<BuildCause> getBuildCause() {
        return buildCause;
    }

    public Stage getStage() {
        return stage;
    }

    public String getLabel() {
        return label;
    }
}
