package api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import template.Book.Book;
import template.Book.BookDao;
import template.User.User;
import template.User.UserDao;
import template.Constant;
import template.Profile.Profile;
import template.Profile.ProfileDao;

import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProfileApiTest {
    private ProfileApi api;
    private DBI dbi;
    private Gson gson;
    private final User validUser = new User(Constant.generateUUID(), "please@gg", "12345678", "please", Constant.generateUUID(), 0);
    private final Book validBook = new Book(Constant.generateUUID(), "somebook", "author_a", "code_a", 10.99, "good", validUser.getId());
    private final Profile validProfile = new Profile(Constant.generateUUID(), "testAboutMe", 5.0, 1, "12345", validUser.getId());

    @Before
    public void setUp() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        dbi = new DBI("jdbc:mysql://localhost/booktrader_test?useUnicode=true&characterEncoding=UTF-8&useSSL=false", "root", "");
        gson = new Gson();
        api = new ProfileApi();

        ProfileDao profileDao = dbi.onDemand(ProfileDao.class);
        UserDao userDao = dbi.onDemand(UserDao.class);
        BookDao bookDao = dbi.onDemand(BookDao.class);

        api.setDao(dbi.onDemand(ProfileDao.class), dbi.onDemand(BookDao.class));

        Handle handle = dbi.open();
        handle.execute("DELETE FROM BOOK");
        handle.execute("DELETE FROM PROFILE");
        handle.execute("DELETE FROM USER");
        handle.close();

        profileDao.insert(validProfile);
        userDao.insert(validUser);
        bookDao.insert(validBook);
    }

    @After
    public void tearDown() {
        Handle handle = dbi.open();
        handle.execute("DELETE FROM BOOK");
        handle.execute("DELETE FROM PROFILE");
        handle.execute("DELETE FROM USER");
        handle.close();
    }

    @Test
    public void insertProfile() {
        Profile test = new Profile(Constant.generateUUID(),"test_about_me",1.0, 1,"1234567890",Constant.generateUUID());
        String jsonString = gson.toJson(test,Profile.class);
        //System.out.println(jsonString);
        String ret = api.insertProfile(jsonString);
        //System.out.println(ret);
        assertEquals(Constant.SUCCESS, ret);
        String testFail = api.insertProfile("sdfsdfasdadgfsa");
        assertEquals(Constant.FAIL,testFail);
    }


    @Test
    public void getProfileByBookId(){
        String testEmpty = api.getProfileByBookId("fakeBookId123");
        assertEquals(Constant.FAIL,testEmpty);
        String testGood = api.getProfileByBookId(validBook.getId());
        Profile profile = gson.fromJson(testGood,new TypeToken<Profile>(){}.getType());
        assertEquals(testGood,profile);
    }

    @Test
    public void updateAboutMeByUserId(){
        String userID = Constant.generateUUID();
        String profileID = Constant.generateUUID();
        Profile testProfile = new Profile(profileID,"test_about_me",5.0, 1,"1234567890",userID);
        String jsonString = gson.toJson(testProfile, Profile.class);
        String ret = api.insertProfile(jsonString);
        assertEquals(ret,Constant.SUCCESS);

        testProfile.setAbout_me("old_about_me");
        jsonString = gson.toJson(testProfile,Profile.class);
        ret = api.updateAboutMeByUserId(userID, "new_about_me");
        assertEquals(Constant.SUCCESS, ret);

        String aboutMe = testProfile.getAbout_me();
        Profile profileChanged = gson.fromJson(aboutMe,new TypeToken<Profile>(){}.getType());
        assertTrue(profileChanged.getAbout_me() == "new_about_me");

        //test if rating, phone_numbers are remain unchanged.
        assertTrue(profileChanged.getPhone_number().equals(testProfile.getPhone_number()));
        assertTrue(profileChanged.getRating() == testProfile.getRating());
    }

    @Test
    public void updatePhoneNumberByUserId(){
        String userID = Constant.generateUUID();
        String profileID = Constant.generateUUID();
        Profile testProfile = new Profile(profileID,"test_about_me",5.0, 1,"1234567890",userID);
        String jsonString = gson.toJson(testProfile, Profile.class);
        String ret = api.insertProfile(jsonString);
        assertEquals(ret,Constant.SUCCESS);

        testProfile.setPhone_number("1234567890");
        jsonString = gson.toJson(testProfile,Profile.class);
        ret = api.updatePhoneNumberByUserId(userID,"0987654321");
        assertEquals(Constant.SUCCESS, ret);

        String phoneNumbe = testProfile.getPhone_number();
        Profile profileChanged = gson.fromJson(phoneNumbe,new TypeToken<Profile>(){}.getType());
        assertTrue(profileChanged.getPhone_number()=="new_phone_number");

        assertTrue(profileChanged.getAbout_me().equals(testProfile.getAbout_me()));
        assertTrue(profileChanged.getRating() == testProfile.getRating());
    }

    @Test
    public void getAboutmeByUserId() throws Exception{
        String result = api.getAboutmeByUserId(validProfile.getUser_id());
        Gson gson = new Gson();
        Profile profile = gson.fromJson(result,Profile.class);
        assertEquals(profile.getAbout_me(), validProfile.getAbout_me());

        result = api.getPhoneNumberByUserId(null);
        assertEquals(result,Constant.FAIL);
    }

    @Test
    public void getPhoneNumberByUserId() throws Exception{
        String result = api.getPhoneNumberByUserId(validProfile.getUser_id());
        Gson gson = new Gson();
        Profile profile = gson.fromJson(result,Profile.class);
        assertEquals(profile.getPhone_number(),validProfile.getPhone_number());
    }

    @Test
    public void getRatingByUserId() throws Exception{
        String result = api.getAboutmeByUserId(validProfile.getUser_id());
        Gson gson = new Gson();
        Profile profile = gson.fromJson(result,Profile.class);
        assertEquals(profile, validProfile.getPhone_number());
    }
}
