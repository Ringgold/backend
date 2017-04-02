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
    private static final String domain = "bookTrader428";
    private static final String prop = "428bookTrader";
    private static final String adminEmail = "danial.almani@mail.mcgill.ca";

    private Session mailSession;

    private void send(String userEmail, String userName, String sellerEmail, String content) throws Exception {
        MimeMessage message = new MimeMessage(mailSession);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(sellerEmail));
        message.setSubject(userName + " wants to buy the book [book trader]");
        message.setText("from: " + userEmail + "\n" + content + "\n" + "name: " + userName);

        Transport transport = mailSession.getTransport("smtp");
        transport.connect(host, domain, prop);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    private void report(String userEmail, String userName, String sellerEmail, String content, boolean anon) throws Exception {
        MimeMessage message = new MimeMessage(mailSession);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(adminEmail));
        message.setSubject("A post was reported on your website BookTrader");
        if(anon){
            message.setText("Hi, someone reported a post on your website. \nThat person didn't give any contact info. Here is the report:\n" +
                    content + "\nIt came from the seller with email: " + sellerEmail + " and with username: " +
                    userName + ".\nPlease take the necessary actions to keep the website correct.");
        }else{
            message.setText("Hi, someone reported a post on your website. \nThat person gave their email: " + userEmail + ". Here is the report:\n" +
                    content + "\nIt came from the seller with email: " + sellerEmail + " and with username: " +
                    userName + ".\nPlease take the necessary actions to keep the website correct.");
        }
        Transport transport = mailSession.getTransport("smtp");
        transport.connect(host, domain, prop);
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
    public String sendContactSellerEmail(String request) {
        Gson gson = new Gson();
        try {
            Mail mail = gson.fromJson(request, Mail.class);
            if (!mail.validate()) {
                throw new NullPointerException();
            }

            if (!mail.getSellerEmail().contains("@test")){
                send(mail.getUserEmail(), mail.getUserName(), mail.getSellerEmail(), mail.getContent());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
        return Constant.SUCCESS;
    }

    void sendActivationEmail(String name, String address, String userId, String code) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("Hi, ").append(name).append("!\n\n");
        sb.append("Please click the following link to activate your account:\n");
        sb.append("http://silentdoor.net/activate?user=").append(userId).append("&code=").append(code).append("\n\n"); //todo convert domain into constant
        sb.append("Thank you,\nBook Trader\n");
        String content = sb.toString();

        MimeMessage message = new MimeMessage(mailSession);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
        message.setSubject("Book Trader: " + name + ", please activate your account!");
        message.setText(content);

        Transport transport = mailSession.getTransport("smtp");
        transport.connect(host, domain, prop);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }


    @POST
    @Path("/report")
    public String sendPostReportEmail(String request) {
        Gson gson = new Gson();
        boolean anon = false;
        try {
            Mail mail = gson.fromJson(request, Mail.class);
            if (!mail.validate()) {
                throw new NullPointerException();
            }
            if(mail.getUserEmail() == "null"){
                anon = true;
            }
            report(mail.getUserEmail(), mail.getUserName(), mail.getSellerEmail(), mail.getContent(), anon);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
        return Constant.SUCCESS;

    }

    public void initMail() {
        Properties mailProps = getProps(host, domain, prop);

        mailSession = Session.getDefaultInstance(mailProps);
    }
}
