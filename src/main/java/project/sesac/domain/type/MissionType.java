package project.sesac.domain.type;

public enum MissionType {
    DEFAULT(0, 1),
    OUTSIDE(1, 1),
    MEET(2, 5);

    private final int code;
    private final int rewardPoint;

    MissionType(int code, int rewardPoint) {
        this.code = code;
        this.rewardPoint = rewardPoint;
    }

    public static MissionType fromCode(int code) {
        for (MissionType value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown mission type code: " + code);
    }

    public int code() {
        return code;
    }

    public int rewardPoint() {
        return rewardPoint;
    }
}
