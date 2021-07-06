package ab.demo.myschedule;

public enum Timetable {
    FIRST("0-8"),
    SECOND("8-16"),
    THIRD("16-24");

    private String description;

    Timetable(String description) {
        this.description = description;
    }

    public static Timetable ofDescription(String description) {
        for (Timetable shift : values()) {
            if (shift.description.equals(description.trim())) {
                return shift;
            }
        }
        return null;
    }

    public String description() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
