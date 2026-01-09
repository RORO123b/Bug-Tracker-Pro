package enums;

import lombok.Getter;

@Getter
public enum BusinessPriority {
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    CRITICAL(4);

    private final int value;

    BusinessPriority(final int value) {
        this.value = value;
    }
}
