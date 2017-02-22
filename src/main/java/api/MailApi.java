package api;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import template.Constant;
import template.Mail.Mail;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Properties;

@Path("/mail")
public class MailApi {
    private static final Logger LOG = LoggerFactory.getLogger(MailApi.class);

    private static final String host = "smtp.gmail.com";
    private static final String user = "bookTrader428";
    private static final String password = "428bookTrader";

    private static final String activationHost = "smtp.gmail.com";
    private static final String activationUser = "booktrader.activation";
    private static final String activationPassword = "428bookTrader";

    private Properties mailProps;
    private Session mailSession;

    private Properties activationMailProps;
    private Session activationMailSession;

    private void send(String userEmail, String userName, String sellerEmail, String content) throws Exception {
        MimeMessage message = new MimeMessage(mailSession);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(sellerEmail));
        message.setSubject(userName + " wants to buy the book [book trader]");
        message.setText("from: " + userEmail + "\n" + content + "\n" + "name: " + userName);

        Transport transport = mailSession.getTransport("smtp");
        transport.connect(host, user, password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    private Properties getProps(String host, String user, String password) {
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", user);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.auth", "true");
        return props;
    }

    @POST
    @Path("/send")
    public String main(String request) {
        Gson gson = new Gson();
        try {
            Mail mail = gson.fromJson(request, Mail.class);
            if (!mail.validate()) {
                throw new NullPointerException();
            }
            send(mail.getUserEmail(), mail.getUserName(), mail.getSellerEmail(), mail.getContent());
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
        return Constant.SUCCESS;
    }

    public void sendActivationEmail(String name, String address, String userId, String code) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("Hi, " + name + "!\n\n");
        sb.append("Please click the following link to activate your account:\n");
        sb.append("http://silentdoor.net/activate?user=" + userId + "&code=" + code + "\n\n");
        sb.append("Thank you,\nBook Trader\n");
        String content = sb.toString();

        MimeMessage message = new MimeMessage(activationMailSession);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
        message.setSubject("Book Trader: " + name + ", please activate your account!");
        message.setText(content);

        Transport transport = activationMailSession.getTransport("smtp");
        transport.connect(activationHost, activationUser, activationPassword);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    public void initMail() {
        mailProps = getProps(host, user, password);
        activationMailProps = getProps(activationHost, activationUser, activationPassword);

        mailSession = Session.getDefaultInstance(mailProps);
        activationMailSession = Session.getDefaultInstance(activationMailProps);
    }
}
