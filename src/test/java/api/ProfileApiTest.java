package api;

import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import template.Book.Book;
import template.Book.BookDao;
import template.Constant;
import template.Profile.Profile;
import template.Profile.ProfileDao;
import template.User.User;
import template.User.UserDao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProfileApiTest {
    private ProfileApi api;
    private DBI dbi;
    private Gson gson;
    private Profile validProfile;

    @Before
    public void setUp() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        dbi = new DBI("jdbc:mysql://localhost/booktrader_test?useUnicode=true&characterEncoding=UTF-8&useSSL=false", "root", "delivery");
        gson = new Gson();
        api = new ProfileApi();

        ProfileDao profileDao = dbi.onDemand(ProfileDao.class);
        UserDao userDao = dbi.onDemand(UserDao.class);
        BookDao bookDao = dbi.onDemand(BookDao.class);

        api.setDao(profileDao, bookDao);

        Handle handle = dbi.open();
        handle.execute("DELETE FROM book");
        handle.execute("DELETE FROM profile");
        handle.execute("DELETE FROM user");
        handle.close();
        User validUser = new User(Constant.generateUUID(), "please@gg", "12345678", "please", Constant.generateUUID(), 0);
        Book validBook = new Book(Constant.generateUUID(), "somebook", "author_a", "code_a", 10.99, "good", validUser.getId(), 0);
        validProfile = new Profile(Constant.generateUUID(), "testAboutMe", 5.0, "12345", validUser.getId(), 1);

        profileDao.insert(validProfile);
        userDao.insert(validUser);
        bookDao.insert(validBook);
    }

    @After
    public void tearDown() {
        Handle handle = dbi.open();
        handle.execute("DELETE FROM book");
        handle.execute("DELETE FROM profile");
        handle.execute("DELETE FROM user");
        handle.close();
    }

    @Test
    public void insertProfile() {
        Profile test = new Profile(Constant.generateUUID(), "test_about_me", 1.0, "1234567890", Constant.generateUUID(), 1);
        String jsonString = gson.toJson(test, Profile.class);
        String ret = api.insertProfile(jsonString);
        assertEquals(Constant.SUCCESS, ret);
        String testFail = api.insertProfile("sdfsdfasdadgfsa");
        assertEquals(Constant.FAIL, testFail);
    }

    @Test
    public void updateAboutMeByUserId() {
        String userID = Constant.generateUUID();
        String profileID = Constant.generateUUID();
        Profile testProfile = new Profile(profileID, "test_about_me", 5.0, "1234567890", userID, 1);
        String jsonString = gson.toJson(testProfile, Profile.class);
        String ret = api.insertProfile(jsonString);
        assertEquals(ret, Constant.SUCCESS);

        testProfile.setAbout_me("old_about_me");
        ret = api.updateAboutMeByUserId(userID, "new_about_me");
        assertEquals(Constant.SUCCESS, ret);

        testProfile = gson.fromJson(api.getProfileByUserId(userID), Profile.class);
        String aboutMe = testProfile.getAbout_me();
        assertTrue(aboutMe.equals("new_about_me"));
    }

    @Test
    public void updatePhoneNumberByUserId() {
        String userID = Constant.generateUUID();
        String profileID = Constant.generateUUID();
        Profile testProfile = new Profile(profileID, "test_about_me", 5.0, "1234567890", userID, 1);
        String jsonString = gson.toJson(testProfile, Profile.class);
        String ret = api.insertProfile(jsonString);
        assertEquals(ret, Constant.SUCCESS);

        testProfile.setPhone_number("1234567890");
        ret = api.updatePhoneNumberByUserId(userID, "0987654321");
        assertEquals(Constant.SUCCESS, ret);
        testProfile = gson.fromJson(api.getProfileByUserId(userID), Profile.class);
        assertTrue(testProfile.getPhone_number().equals("0987654321"));
    }

    @Test
    public void getAboutMeByUserId() throws Exception {
        String result = api.getProfileByUserId(validProfile.getUser_id());
        Gson gson = new Gson();
        Profile profile = gson.fromJson(result, Profile.class);
        assertEquals(profile.getAbout_me(), validProfile.getAbout_me());
    }

    @Test
    public void getPhoneNumberByUserId() throws Exception {
        String result = api.getProfileByUserId(validProfile.getUser_id());
        Gson gson = new Gson();
        Profile profile = gson.fromJson(result, Profile.class);
        assertEquals(profile.getPhone_number(), validProfile.getPhone_number());
    }

    @Test
    public void getRatingByUserId() throws Exception {
        String result = api.getProfileByUserId(validProfile.getUser_id());
        Gson gson = new Gson();
        Profile profile = gson.fromJson(result, Profile.class);
        assertTrue(profile.getRating() == validProfile.getRating());
    }

    @Test
    public void updateUserRating() throws Exception {
        String userID = Constant.generateUUID();
        String profileID = Constant.generateUUID();
        Profile testProfile = new Profile(profileID, "test_about_me", 9.0, "1234567890", userID, 1);
        String jsonString = gson.toJson(testProfile, Profile.class);
        String ret = api.insertProfile(jsonString);
        assertEquals(ret, Constant.SUCCESS);

        ret = api.updateRating(userID, 1.0);
        assertEquals(Constant.SUCCESS, ret);
        testProfile = gson.fromJson(api.getProfileByUserId(userID), Profile.class);
        assertTrue(testProfile.getRating() == 5.0);
    }
}
