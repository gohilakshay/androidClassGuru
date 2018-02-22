package in.classguru.classguru.models;

/**
 * Created by a2z on 2/4/2018.
 */

public class PortionModel {
    String subject;
    String topic;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    String completed;

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    String batch;

    public String getPortionId() {
        return portionId;
    }

    public void setPortionId(String portionId) {
        this.portionId = portionId;
    }

    String portionId;

    public String getTotalTopics() {
        return totalTopics;
    }

    public void setTotalTopics(String totalTopics) {
        this.totalTopics = totalTopics;
    }

    String totalTopics;
}
