package tickets;

import enums.Frequency;
import enums.Severity;

public final class BugBuilder extends TicketBuilder<Bug, BugBuilder> {

    public BugBuilder() {
        this.ticket = new Bug();
    }

    @Override
    protected BugBuilder self() {
        return this;
    }

    /**
     * @param value the expected behavior description
     * @return this builder instance
     */
    public BugBuilder expectedBehavior(final String value) {
        ticket.setExpectedBehavior(value);
        return this;
    }

    /**
     * @param value the actual behavior description
     * @return this builder instance
     */
    public BugBuilder actualBehavior(final String value) {
        ticket.setActualBehavior(value);
        return this;
    }

    /**
     * @param severity the severity level of the bug
     * @return this builder instance
     */
    public BugBuilder severity(final Severity severity) {
        ticket.setSeverity(severity);
        return this;
    }

    /**
     * @param frequency the frequency of occurrence
     * @return this builder instance
     */
    public BugBuilder frequency(final Frequency frequency) {
        ticket.setFrequency(frequency);
        return this;
    }

    /**
     * @param environment the environment where the bug was found
     * @return this builder instance
     */
    public BugBuilder environment(final String environment) {
        ticket.setEnvironment(environment);
        return this;
    }

    /**
     * @param errorCode the associated error code
     * @return this builder instance
     */
    public BugBuilder errorCode(final Integer errorCode) {
        ticket.setErrorCode(errorCode);
        return this;
    }

    @Override
    public Bug build() {
        return ticket;
    }
}
