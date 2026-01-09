package tickets;

import enums.BusinessValue;

public final class UIFeedbackBuilder extends TicketBuilder<UIFeedback, UIFeedbackBuilder> {

    public UIFeedbackBuilder() {
        this.ticket = new UIFeedback();
    }

    /**
     * Returns this builder instance.
     *
     * @return this builder
     */
    @Override
    protected UIFeedbackBuilder self() {
        return this;
    }

    /**
     * Sets the UI element ID.
     *
     * @param uiElementId the UI element identifier
     * @return this builder
     */
    public UIFeedbackBuilder uiElementId(final String uiElementId) {
        ticket.uiElementId = uiElementId;
        return this;
    }

    /**
     * Sets the business value.
     *
     * @param value the business value
     * @return this builder
     */
    public UIFeedbackBuilder businessValue(final BusinessValue value) {
        ticket.businessValue = value;
        return this;
    }

    /**
     * Sets the usability score.
     *
     * @param score the usability score
     * @return this builder
     */
    public UIFeedbackBuilder usabilityScore(final Integer score) {
        ticket.usabilityScore = score;
        return this;
    }

    /**
     * Sets the screenshot URL.
     *
     * @param url the screenshot URL
     * @return this builder
     */
    public UIFeedbackBuilder screenshotUrl(final String url) {
        ticket.screenshotUrl = url;
        return this;
    }

    /**
     * Sets the suggested fix.
     *
     * @param fix the suggested fix
     * @return this builder
     */
    public UIFeedbackBuilder suggestedFix(final String fix) {
        ticket.suggestedFix = fix;
        return this;
    }

    /**
     * Builds the UIFeedback ticket.
     *
     * @return the built UIFeedback instance
     */
    @Override
    public UIFeedback build() {
        return ticket;
    }
}
