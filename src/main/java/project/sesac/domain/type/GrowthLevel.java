package project.sesac.domain.type;

public enum GrowthLevel {
    SEED("씨앗", "seed", 0, 200),
    SPROUT("새싹", "sprout", 200, 600),
    BRANCH("가지", "branch", 600, 1400),
    TREE("나무", "tree", 1400, 2600),
    APPLE("열매", "apple", 2600, Integer.MAX_VALUE);

    private final String label;
    private final String imageName;
    private final int minExp;
    private final int maxExp;

    GrowthLevel(String label, String imageName, int minExp, int maxExp) {
        this.label = label;
        this.imageName = imageName;
        this.minExp = minExp;
        this.maxExp = maxExp;
    }

    public static GrowthLevel fromExp(int exp) {
        for (GrowthLevel level : values()) {
            if (exp >= level.minExp && exp < level.maxExp) {
                return level;
            }
        }
        return APPLE;
    }

    public String label() {
        return label;
    }

    public String imageName() {
        return imageName;
    }

    public int progressPercent(int exp) {
        if (this == APPLE) {
            return 100;
        }
        return (int) (((double) (exp - minExp) / (maxExp - minExp)) * 100);
    }
}
