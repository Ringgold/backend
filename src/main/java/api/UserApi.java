package api;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import template.Book.Book;
import template.Book.BookDao;
import template.Constant;
import template.User.User;
import template.User.UserDao;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.Properties;

@Path("/user")
public class UserApi {
    private static final Logger LOG = LoggerFactory.getLogger(UserApi.class);
    private UserDao userDao;
    private BookDao bookDao;

    private static final String host = "smtp.gmail.com";
    private static final String user = "booktrader.activation";
    private static final String password = "428bookTrader";

    private Properties mailProps;
    private Session mailSession;

    @Path("/all")
    @GET
    public String getAllUser() {
        String result;
        try {
            List<User> list = userDao.getAllUser();
            if (list.size() == 0) {
                return Constant.EMPTY;
            }

            for (User u : list) {
                u.setPassword("null");
            }

            Gson gson = new Gson();
            result = gson.toJson(list);
            if (result == null) {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
        return result;
    }

    @Path("/register")
    @POST
    public String register(String request) {
        User user;
        Gson gson = new Gson();
        try {
            user = gson.fromJson(request, User.class);
            user.setId(Constant.generateUUID());
            user.setStatus(0);

            String code = Constant.generateUUID();
            user.setActivationCode(code);

            final String name = user.getName();
            final String email = user.getEmail();
            final String id = user.getId();
            final String actCode = new String(code);

            new Thread(new Runnable() {
                public void run() {
                    try {
                        sendActivationEmail(name, email, id, actCode);
                    } catch (Exception e) {
                        LOG.error(e.getMessage());
                    }
                }
            }).start();

            if (!user.validate()) {
                return Constant.NOTVALID;
            }
            userDao.insert(user);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
        return Constant.SUCCESS;
    }

    @Path("/login")
    @POST
    public String login(String request) {
        Gson gson = new Gson();
        String result;
        try {
            User potential = gson.fromJson(request, User.class);
            User user = userDao.getUserByEmail(potential.getEmail());
            if (!user.getPassword().equals(potential.getPassword())) {
                return Constant.FAIL;
            }
            result = gson.toJson(user);
            if (result == null) {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
        return result;
    }

    @Path("/my_book/{user_id}")
    @GET
    public String getMyBooks(@PathParam("user_id") String userId) {
        String result;
        try {
            User user = userDao.getUserById(userId);
            if (!user.validate()) {
                throw new Exception();
            }
            List<Book> books = bookDao.getBooksByUser(userId);
            if (books.size() == 0) {
                return Constant.EMPTY;
            }
            Gson gson = new Gson();
            result = gson.toJson(books);
            if (result == null) {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
        return result;
    }

    @Path("/get_user/{user_id}")
    @GET
    public String getUserById(@PathParam("user_id") String userId) {
        String result;
        try {
            User user = userDao.getUserById(userId);
            if (!user.validate()) {
                throw new Exception();
            }
            user.setPassword("null");
            user.setActivationCode("null");
            Gson gson = new Gson();
            result = gson.toJson(user);
            if (result == null) {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
        return result;
    }

    @Path("/activate/{user_id}/{activation_code}")
    @GET
    public String activate(@PathParam("user_id") String userId, @PathParam("activation_code") String activationCode) {
        try {
            User user = userDao.getUserById(userId);

            if (user.getActivationCode().equals(activationCode) && user.getStatus() == 0) {
                userDao.activate(user.getId());
                return Constant.SUCCESS;
            }

            return Constant.FAIL;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
    }

    private void sendActivationEmail(String name, String address, String userId, String code) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("Hi, " + name + "!\n\n");
        sb.append("Please click the following link to activate your account:\n");
        sb.append("http://silentdoor.net/activate?user=" + userId + "&code=" + code + "\n\n");
        sb.append("Thank you,\nBook Trader\n");
        String content = sb.toString();

        MimeMessage message = new MimeMessage(mailSession);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
        message.setSubject("Book Trader: " + name + ", please activate your account!");
        message.setText(content);

        Transport transport = mailSession.getTransport("smtp");
        transport.connect(host, user, password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    public void initMail() {
        mailProps = System.getProperties();
        mailProps.put("mail.smtp.starttls.enable", "true");
        mailProps.put("mail.smtp.host", host);
        mailProps.put("mail.smtp.user", user);
        mailProps.put("mail.smtp.password", password);
        mailProps.put("mail.smtp.socketFactory.port", "465");
        mailProps.put("mail.smtp.auth", "true");

        mailSession = Session.getDefaultInstance(mailProps);
    }

    public void setDao(UserDao userDao, BookDao bookDao) {
        this.userDao = userDao;
        this.bookDao = bookDao;
    }
}
