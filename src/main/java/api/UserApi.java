package api;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import template.Book.Book;
import template.Book.BookDao;
import template.Constant;
import template.Profile.Profile;
import template.Profile.ProfileDao;
import template.User.User;
import template.User.UserDao;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/user")
public class UserApi {
    private static final Logger LOG = LoggerFactory.getLogger(UserApi.class);
    private UserDao userDao;
    private BookDao bookDao;
    private ProfileDao profileDao;
    private MailApi mailApi;

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

            // This is a user used for testing if the email if the following is true
            boolean isATestUser = user.getEmail().contains("@test.test");

            if (isATestUser) {
                user.setStatus(1);
                user.setPassword("password");
                user.setActivationCode("null");
                String profileId = Constant.generateUUID();
                Profile profile = new Profile(profileId, "who am I?", 0.0, "0000000000", user.getId(), 0);
                profileDao.insert(profile);
            } else {
                user.setStatus(0);
                String code = Constant.generateUUID();
                user.setActivationCode(code);
            }

            if (!user.validate()) {
                return Constant.NOTVALID;
            }

            userDao.insert(user);

            if (!isATestUser) {
                mailApi.sendActivationEmail(user.getName(), user.getEmail(), user.getId(), user.getActivationCode());
            }

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
            if (user.getStatus() != 1) {
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

            if ((user.getActivationCode().equals(activationCode) && user.getStatus() == 0) || user.getEmail().contains("@test.test")) {
                userDao.activate(user.getId());
                String profileId = Constant.generateUUID();
                Profile profile = new Profile(profileId, "who am I?", 0.0, "0000000000", user.getId(), 0);
                profileDao.insert(profile);
                return Constant.SUCCESS;
            }

            return Constant.FAIL;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
    }

    public void setDao(UserDao userDao, BookDao bookDao, ProfileDao profileDao) {
        this.userDao = userDao;
        this.bookDao = bookDao;
        this.profileDao = profileDao;
    }

    public void setMailApi(MailApi mailApi) {
        this.mailApi = mailApi;
    }

    @Path("/delete/{user_id}")
    @GET
    public String delete(@PathParam("user_id") String userId) {
        try {
            userDao.deleteUser(userId);
            userDao.deletePosts(userId);
            userDao.deleteProfile(userId);
            return Constant.SUCCESS;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
    }
}
