package org.example.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.example.serializer.ZonedDateTimeSerializer;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@JsonRootName("person")
@JsonPropertyOrder({
        "full_name", "birth_date", "title", "salary"
})
@JsonIgnoreProperties({"gender"})
@JsonInclude(JsonInclude.Include.NON_EMPTY) // non empty = non blank and non null
public record JacksonAnnotations(
        @JsonProperty("full_name")
        String fullName,
        @JsonAnyGetter
        @JsonAnySetter
        Map<String, Double> personalProjects,
        @JsonProperty("gender")
        Gender gender,
        @JsonRawValue
        @JsonIgnore
        String json,
        @JsonProperty("birth_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate birthDate,
        @JsonUnwrapped
        Profession profession,
        @JsonManagedReference
        List<Car> cars,
        @JsonSerialize(using = ZonedDateTimeSerializer.class) // Same with @JsonDeserialize
        ZonedDateTime createdAt
) {
    public enum Gender {
        MALE("Homme"), FEMALE("Femme");

        private String fr;

        Gender(String fr) {
            this.fr = fr;
        }

        @JsonValue
        public String getFr() {
            return this.fr;
        }
    }

    public record Profession(
            @JsonProperty
            String title,
            @JsonProperty
            int salary
    ) {}

    public static class Car {
            @JsonProperty
            private String make;
            @JsonProperty
            private String model;
            @JsonProperty
            private int year;
            @JsonBackReference
            private JacksonAnnotations owner;

        public Car(String make, String model, int year) {
            this.make = make;
            this.model = model;
            this.year = year;
        }

        public void setOwner(JacksonAnnotations owner) {
            this.owner = owner;
        }

        @Override
        public String toString() {
            return "Car{" +
                    "make='" + make + '\'' +
                    ", model='" + model + '\'' +
                    ", year=" + year +
                    '}';
        }
    }
}
