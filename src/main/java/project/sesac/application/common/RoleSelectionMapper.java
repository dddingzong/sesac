package project.sesac.application.common;

import project.sesac.domain.type.InformationPreference;

public final class RoleSelectionMapper {

    private RoleSelectionMapper() {
    }

    public static InformationPreference toPreference(String chooseRole) {
        if ("care".equals(chooseRole)) {
            return InformationPreference.CARE;
        }
        if ("job".equals(chooseRole)) {
            return InformationPreference.JOB;
        }
        if ("all".equals(chooseRole)) {
            return InformationPreference.ALL;
        }
        return null;
    }
}
