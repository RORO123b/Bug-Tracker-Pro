package Tickets;

import enums.BusinessValue;
import enums.TicketType;

public class UIFeedbackBuilder extends TicketBuilder<UIFeedback, UIFeedbackBuilder> {

    public UIFeedbackBuilder() {
        this.ticket = new UIFeedback();
    }

    @Override
    protected UIFeedbackBuilder self() {
        return this;
    }

    public UIFeedbackBuilder uiElementId(String uiElementId) {
        ticket.uiElementId = uiElementId;
        return this;
    }

    public UIFeedbackBuilder businessValue(BusinessValue value) {
        ticket.businessValue = value;
        return this;
    }

    public UIFeedbackBuilder usabilityScore(Integer score) {
        ticket.usabilityScore = score;
        return this;
    }

    public UIFeedbackBuilder screenshotUrl(String url) {
        ticket.screenshotUrl = url;
        return this;
    }

    public UIFeedbackBuilder suggestedFix(String fix) {
        ticket.suggestedFix = fix;
        return this;
    }

    @Override
    public UIFeedback build() {
        return ticket;
    }
}

