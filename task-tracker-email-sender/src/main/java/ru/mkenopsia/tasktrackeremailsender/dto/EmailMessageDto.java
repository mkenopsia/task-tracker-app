package ru.mkenopsia.tasktrackeremailsender.dto;

public record EmailMessageDto(
        String emailAddress,
        String header,
        String body
) {
}
