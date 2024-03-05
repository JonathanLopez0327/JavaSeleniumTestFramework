package testingmyframework;

import org.testframework.emails.EmailService;

public class Test {
    public static void main(String[] args) {
        EmailService emailService = new EmailService();

        System.out.println(emailService.getMessageBySimilarSubject(
                "waleko27@gmail.com", "ximiuibzwqujhfjb", "Prueba01", true
        ));
    }
}
