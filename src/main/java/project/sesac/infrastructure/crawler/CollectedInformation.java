package project.sesac.infrastructure.crawler;

import project.sesac.domain.type.InformationType;

public record CollectedInformation(String title, String url, InformationType informationType) {
}
