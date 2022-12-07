package com.epam.mail;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;


public class SendMail {

    public static String getLinkFromMessage() throws EmailMessageIsAbsentException {
        Properties properties = System.getProperties();

        properties.put("mail.pop3.host", "pop.gmail.com");
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");

        Session session = Session.getInstance(properties);
        String link = "";
        try {
            Store store = session.getStore("pop3s");
            store.connect("pop.gmail.com", "TestAutomationArmenia@gmail.com", "xnkqfnlppqhmjhye");
            Folder inbox = store.getFolder("inbox");
            inbox.open(Folder.READ_ONLY);
            Message[] message = inbox.getMessages();
            Message lastMessage = message[message.length-1];
            link = lastMessage.getContent().toString().split("\n")[1];
            System.out.println(link);
            return link;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new EmailMessageIsAbsentException();
    }

    public static String getCodeFromMessage() throws EmailMessageIsAbsentException {
        Properties properties = System.getProperties();

        properties.put("mail.pop3.host", "pop.gmail.com");
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");

        Session session = Session.getInstance(properties);
        String code = "";
        try {
            Store store = session.getStore("pop3s");
            store.connect("pop.gmail.com", "TestAutomationArmenia@gmail.com", "xnkqfnlppqhmjhye");
            Folder inbox = store.getFolder("inbox");
            inbox.open(Folder.READ_ONLY);
            Message[] message = inbox.getMessages();
            Message lastMessage = message[message.length-1];
            code = lastMessage.getContent().toString();
            System.out.println(code);
            return code;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    private static class EmailMessageIsAbsentException extends IllegalArgumentException {
        EmailMessageIsAbsentException() {
            super("Message doesn't contain any links or code");
        }
    }
}