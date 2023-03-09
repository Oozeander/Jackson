package org.example.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
    MALE("Homme"), FEMALE("Femme");

    private String fr;

    Gender(String fr) {
        this.fr = fr;
    }

    public String getFr() {
        return this.fr;
    }
}
