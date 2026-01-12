package tickets.action;

import java.time.LocalDate;

public class ActionBuilder {
    private String action;
    private String by;
    private String to;
    private String milestone;
    private LocalDate timestamp;
    private String oldStatus;
    private String newStatus;
    private String removedDev;

    public ActionBuilder action(final String action) {
        this.action = action;
        return this;
    }

    public ActionBuilder by(final String by) {
        this.by = by;
        return this;
    }

    public ActionBuilder to(final String to) {
        this.to = to;
        return this;
    }

    public ActionBuilder milestone(final String milestone) {
        this.milestone = milestone;
        return this;
    }

    public ActionBuilder timestamp(final LocalDate timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public ActionBuilder oldStatus(final String oldStatus) {
        this.oldStatus = oldStatus;
        return this;
    }

    public ActionBuilder newStatus(final String newStatus) {
        this.newStatus = newStatus;
        return this;
    }

    public ActionBuilder removedDev(final String removedDev) {
        this.removedDev = removedDev;
        return this;
    }

    public Action build() {
        Action actionObj = new Action();
        actionObj.setAction(action);
        actionObj.setBy(by);
        actionObj.setTo(to);
        actionObj.setMilestone(milestone);
        actionObj.setTimestamp(timestamp);
        actionObj.setOldStatus(oldStatus);
        actionObj.setNewStatus(newStatus);
        actionObj.setRemovedDev(removedDev);
        return actionObj;
    }
}
