package testingmyframework;

import base.HomeTest;
import org.testframework.emails.EmailService;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EmailServiceTest {

    private static final String user = "waleko27@gmail.com";
    private static final String pass = "ximiuibzwqujhfjb";

    @Test(description = "Validate content of email")
    void contentEmailTestBySubject() {
        EmailService emailService = new EmailService();
        boolean status = false;

        String content = emailService.getMessageBySimilarSubject(
                user,
                pass,
                "Prueba 04",
                false
        );

        String[] keyPhrases = {
                "MANUEL FERMIN.",
                "Contrato de Apertura de Cuentas"
        };

        for (String phrase : keyPhrases) {
            status = content.contains(phrase);
        }

        Assert.assertTrue(status, "El correo contiene no lo esperado");
    }
}
