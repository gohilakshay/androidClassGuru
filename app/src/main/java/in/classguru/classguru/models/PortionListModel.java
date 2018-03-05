package in.classguru.classguru.models;

/**
 * Created by a2z on 3/5/2018.
 */

public class PortionListModel {
    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    String topics;

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    int completed;
}
