package tickets.action;

import java.time.LocalDate;

public final class ActionBuilder {
    private String action;
    private String by;
    private String to;
    private String milestone;
    private LocalDate timestamp;
    private String oldStatus;
    private String newStatus;
    private String removedDev;

    /**
     * Sets the action.
     * @param actionParam the action name
     * @return the builder instance
     */
    public ActionBuilder action(final String actionParam) {
        this.action = actionParam;
        return this;
    }

    /**
     * Sets the performer.
     * @param byParam the username
     * @return the builder instance
     */
    public ActionBuilder by(final String byParam) {
        this.by = byParam;
        return this;
    }

    /**
     * Sets the recipient.
     * @param toParam the username
     * @return the builder instance
     */
    public ActionBuilder to(final String toParam) {
        this.to = toParam;
        return this;
    }

    /**
     * Sets the milestone.
     * @param milestoneParam the milestone name
     * @return the builder instance
     */
    public ActionBuilder milestone(final String milestoneParam) {
        this.milestone = milestoneParam;
        return this;
    }

    /**
     * Sets the timestamp.
     * @param timestampParam the date
     * @return the builder instance
     */
    public ActionBuilder timestamp(final LocalDate timestampParam) {
        this.timestamp = timestampParam;
        return this;
    }

    /**
     * Sets the old status.
     * @param oldStatusParam the status
     * @return the builder instance
     */
    public ActionBuilder oldStatus(final String oldStatusParam) {
        this.oldStatus = oldStatusParam;
        return this;
    }

    /**
     * Sets the new status.
     * @param newStatusParam the status
     * @return the builder instance
     */
    public ActionBuilder newStatus(final String newStatusParam) {
        this.newStatus = newStatusParam;
        return this;
    }

    /**
     * Sets the removed developer.
     * @param removedDevParam the username
     * @return the builder instance
     */
    public ActionBuilder removedDev(final String removedDevParam) {
        this.removedDev = removedDevParam;
        return this;
    }

    /**
     * Builds the action object.
     * @return the final Action
     */
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
