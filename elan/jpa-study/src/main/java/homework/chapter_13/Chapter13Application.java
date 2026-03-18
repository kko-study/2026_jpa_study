package homework.chapter_13;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"homework.chapter_12", "homework.chapter_13"})
public class Chapter13Application {

    public static void main(String[] args) {
        SpringApplication.run(Chapter13Application.class, args);
    }
}
