package project.sesac.domain.type;

public enum InformationPreference {
    CARE(0),
    JOB(1),
    ALL(2);

    private final int code;

    InformationPreference(int code) {
        this.code = code;
    }

    public static InformationPreference fromCode(int code) {
        for (InformationPreference value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown information preference code: " + code);
    }

    public int code() {
        return code;
    }

    public boolean includes(InformationType type) {
        return this == ALL || type.code() == code;
    }
}
