package Tickets;

import enums.Frequency;
import enums.Severity;

public class BugBuilder extends TicketBuilder<Bug, BugBuilder> {

    public BugBuilder() {
        this.ticket = new Bug();
    }

    @Override
    protected BugBuilder self() {
        return this;
    }

    public BugBuilder expectedBehavior(String value) {
        ticket.setExpectedBehavior(value);
        return this;
    }

    public BugBuilder actualBehavior(String value) {
        ticket.setActualBehavior(value);
        return this;
    }

    public BugBuilder severity(Severity severity) {
        ticket.setSeverity(severity);
        return this;
    }

    public BugBuilder frequency(Frequency frequency) {
        ticket.setFrequency(frequency);
        return this;
    }

    public BugBuilder environment(String environment) {
        ticket.setEnvironment(environment);
        return this;
    }

    public BugBuilder errorCode(Integer errorCode) {
        ticket.setErrorCode(errorCode);
        return this;
    }

    @Override
    public Bug build() {
        return ticket;
    }
}

