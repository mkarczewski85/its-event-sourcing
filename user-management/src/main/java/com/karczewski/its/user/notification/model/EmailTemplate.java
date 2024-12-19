package com.karczewski.its.user.notification.model;

import lombok.Getter;

@Getter
public enum EmailTemplate {

    WELCOME_EMAIL("[ITS] Utworzenie konta",
            "<p>Dzień dobry,</p><div>utworzono konto z danymi logowania:</div><div><b>Login:</b> %s</div><div><b>Hasło:</b> %s</div>"
                    + "<br><div> Zapraszamy do skorzystania z aplikacji pod adresem: <a href=\"%s\">%s</a>"),
    PASSWORD_RESET_EMAIL("[ITS] Reset hasła",
            "<p>Dzień dobry,</p><div>zainicjowano procedurę resetu hasła do konta.</div>"
                    + "<br><div>W celu zmiany hasła należy skorzystać z adresu:  <a href=\"%s\">%s</a>");


    final String message;
    final String subject;

    EmailTemplate(final String subject, final String message) {
        this.message = message;
        this.subject = subject;
    }
}
