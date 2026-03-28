package project.sesac.domain.type;

public enum MissionStage {
    DEFAULT(0),
    OUTSIDE(1),
    MEET(2);

    private final int code;

    MissionStage(int code) {
        this.code = code;
    }

    public static MissionStage fromCode(int code) {
        for (MissionStage value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown mission stage code: " + code);
    }

    public static MissionStage fromPoint(int point) {
        if (point < 10) {
            return DEFAULT;
        }
        if (point < 20) {
            return OUTSIDE;
        }
        return MEET;
    }

    public int code() {
        return code;
    }
}
