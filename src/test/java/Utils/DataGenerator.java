package Utils;

import com.github.javafaker.Faker;

public class DataGenerator {
    static Faker faker = new Faker();

    public static String getRandomEmail() {
        return faker.internet().emailAddress();
    }

}
