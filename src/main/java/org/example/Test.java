package org.example;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.example.model.Gender;
import org.example.model.JacksonAnnotations;
import org.example.model.Person;
import org.example.model.Profession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class Test {

    private static ObjectMapper mapper = configureObjectMapper();
    private static final Path JSON_PATH = Paths.get("person.json");

    public static void main(String[] args) throws IOException {
        var person = new Person("Billel", "KETROUCI", Gender.MALE, LocalDate.of(1996, Month.SEPTEMBER, 9),
                ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()),
                List.of("Mangas", "Animes", "Sports"), new Profession("Software Engineer", 45_000),
                Map.of("Manga", 0.01d, "Sports", 0.18d, "Video Game", 0.07));
        Files.deleteIfExists(JSON_PATH);

        System.out.println(ZonedDateTime.of(LocalDate.now(), LocalTime.now(), ZoneId.systemDefault()));

        // POJO to JSON
        System.out.println(mapper.writeValueAsString(person));
        mapper.writeValue(JSON_PATH.toFile(), person);

        // JSON to POJO
        System.out.println(mapper.readValue(JSON_PATH.toFile(), Person.class));
        String personJson = """
            {
                "firstName": "Billel",
                "lastName": "KETROUCI",
                "gender": "MALE",
                "birthDate": "1996-09-09",
                "createdAt": "2023-03-08T12:04:53+01:00",
                "hobbies" : ["Mangas", "Animes", "Sports"],
                "profession": {
                    "title": "Software Engineer",
                    "salary": 45000
                }, "personalProjectsCompletion": {
                    "Manga": 0.01,
                    "Sports": 0.18,
                    "Video Game": 0.07
                }
            }""";
        System.out.println(mapper.readValue(personJson, Person.class));

        // Collection JSON to Java, Works with Lists, sets and maps
        String professionsJson = """
                [
                    {
                        "title": "Software Engineer",
                        "salary": 45000
                    },
                    {
                        "title": "Network Engineer",
                        "salary": 42000
                    },
                    {
                        "title": "CEO",
                        "salary": 120000
                    }
                ]""";
        System.out.println(mapper.readValue(professionsJson, new TypeReference<List<Profession>>() {}));

        // JsonNode
        var jsonNode = mapper.readTree(personJson);
        var professionSalaryWithPointer = jsonNode.at(JsonPointer.valueOf("/profession/salary")).asInt(0);
        var professionTitleWithGet = jsonNode.get("profession").get("title").asText("default");
        System.out.println("I'm a ".concat(professionTitleWithGet).concat(", my annual salary is at ".concat(String.valueOf(professionSalaryWithPointer)).concat(" Euros")));

        // Creating Nodes, put = create new node, set = modify existing (only takes JsonNode, no primitive values), remove = remove existing
        var emptyNode = mapper.createObjectNode();
        emptyNode.put("fullName", "Billel");
        emptyNode.put("birthDate", LocalDate.of(1996, Month.SEPTEMBER, 9).format(DateTimeFormatter.ISO_LOCAL_DATE));
        var arrayNode = mapper.createArrayNode();
        arrayNode.add("Mangas");
        arrayNode.add("Sports"); // addPOJO, ...
        emptyNode.put("hobbies", arrayNode);
        emptyNode.putPOJO("profession", new Profession("Software Engineer", 45_000));
        System.out.println(emptyNode);

        // Jackson Annotations
        var cars = List.of(
                new JacksonAnnotations.Car("BMW", "3000", 2018),
                new JacksonAnnotations.Car("Audi", "A4", 2016)
        );
        var annotations = new JacksonAnnotations("Billel KETROUCI", Map.of(
                "Mangas", 0.01d, "Video Game", 0.02d), JacksonAnnotations.Gender.MALE, """
                {"car": {"mark": "Audi", "model": "A4", "year": 2018}}""", LocalDate.of(1996, Month.SEPTEMBER, 9),
                new JacksonAnnotations.Profession("Software Engineer", 45_000), cars, ZonedDateTime.now(ZoneId.systemDefault()));
        cars.forEach(car -> car.setOwner(annotations));
        String annotationsJson = mapper.writeValueAsString(annotations);
        // Had to remove owner from carInstance.toString() to avoid StackOverflowError
        System.out.println(annotationsJson.concat("\n" + annotations + "\n" + mapper.writeValueAsString(cars) + "\n" + cars));
    }

    private static ObjectMapper configureObjectMapper() {
        var mapper = new ObjectMapper();
        mapper.findAndRegisterModules(); // register modules such as JavaTimeModule to serialize/deserialize java.time objects
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        return mapper;
    }
}
