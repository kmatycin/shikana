package net.dunice.mstool.constants;

public enum EventStatus {
    UPCOMING("Предстоящее"),
    ONGOING("Текущее"),
    FINISHED("Завершенное");

    private final String displayName;

    EventStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 