package project.sesac.application.common;

public final class ProfileLevelMapper {

    private ProfileLevelMapper() {
    }

    public static ProfileLevel fromExp(int exp) {
        if (exp < 200) {
            return new ProfileLevel("씨앗", "seed", percentage(exp, 200));
        }
        if (exp < 600) {
            return new ProfileLevel("새싹", "sprout", percentage(exp - 200, 400));
        }
        if (exp < 1400) {
            return new ProfileLevel("가지", "branch", percentage(exp - 600, 800));
        }
        if (exp < 2600) {
            return new ProfileLevel("나무", "tree", percentage(exp - 1400, 1200));
        }
        return new ProfileLevel("열매", "apple", 100);
    }

    private static int percentage(int value, int range) {
        return (int) (((double) value / range) * 100);
    }
}
