package org.example.model;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public record Person(
        String firstName,
        String lastName,
        Gender gender,
        LocalDate birthDate,
        ZonedDateTime createdAt,
        List<String> hobbies,
        Profession profession,
        Map<String, Double> personalProjectsCompletion) {
}
