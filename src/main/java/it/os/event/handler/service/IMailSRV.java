package it.os.event.handler.service;

public interface IMailSRV {

    /**
     * Sends an email to the address specified by property.
     * 
     * @param subject Subject of the mail.
     * @param body Body of the mail.
     * @return {@code true} if the email is sent correctly, {@code false} otherwise.
     */
    boolean sendMail(String subject, String body);
    
}
