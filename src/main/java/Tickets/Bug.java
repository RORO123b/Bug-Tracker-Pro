package Tickets;

import enums.Frequency;
import enums.Severity;

public class Bug extends Ticket {
    private String expectedBehavior;
    private String actualBehavior;
    private Frequency frequency;
    private Severity severity;
    private String environment;
    private Integer errorCode;
}
