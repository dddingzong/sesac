package project.sesac.domain.type;

public enum InformationType {
    CARE(0, "복지 정보"),
    JOB(1, "취업 정보");

    private final int code;
    private final String label;

    InformationType(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public static InformationType fromCode(int code) {
        for (InformationType value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown information type code: " + code);
    }

    public int code() {
        return code;
    }

    public String label() {
        return label;
    }
}
