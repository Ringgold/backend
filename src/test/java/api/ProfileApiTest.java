package api;

import org.junit.Before;

import template.Book.Book;
import template.Book.BookDao;
import template.Constant;
import template.Profile.Profile;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import template.User.User;
import template.User.UserDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import template.Profile.ProfileDao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProfileApiTest {
    private ProfileApi api;
    private DBI dbi;
    private Gson gson;
    private final User validUser = new User(Constant.generateUUID(), "please@gg", "12345678", "please", Constant.generateUUID(), 0);
    private final Book validBook = new Book(Constant.generateUUID(), "SOMEBOOK", "author_a", "code_a", 10.99, "good", validUser.getId());
    private final Profile validProfile = new Profile(Constant.generateUUID(), "testAboutMe", 5.0, "12345", validUser.getId());


    @Before
    public void setUp() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        dbi = new DBI("jdbc:mysql://localhost/booktrader_test?useUnicode=true&characterEncoding=UTF-8&useSSL=false", "root", "");
        api = new ProfileApi();
        api.setDao(dbi.onDemand(ProfileDao.class), dbi.onDemand(BookDao.class));
        Handle handle = dbi.open();
        handle.execute("DELETE FROM BOOK");
        handle.execute("DELETE FROM PROFILE");
        handle.execute("DELETE FROM USER");
        handle.close();

        ProfileDao profileDao = dbi.onDemand(ProfileDao.class);
        profileDao.insert(validProfile);

        UserDao userDao = dbi.onDemand(UserDao.class);
        userDao.insert(validUser);

        BookDao bookDao = dbi.onDemand(BookDao.class);
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
    public void insertProfileTest() {
        for (int i = 0; i < 10; i++) {
            Profile test = new Profile( Constant.generateUUID()+ i, "Test_aboutme" + i, i + 1, "Test_phonenumber" + i, Constant.generateUUID() + i);
            String jsonString = gson.toJson(test, Profile.class);
            String ret = api.insertProfile(jsonString);
            assertTrue(ret.equals(Constant.SUCCESS));
        }
    }


    @Test
    public void getProfileByBookIdTest(){
        String testEmpty = api.getProfileByBookId("fakeBookId123");
        assertEquals(Constant.FAIL,testEmpty);
        String testGood = api.getProfileByBookId(validBook.getId());
        Profile profile = gson.fromJson(testGood,new TypeToken<Profile>(){}.getType());
        assertEquals(testGood,profile);
    }

    @Test
    public void updateAboutMeByUserIdTest(){
        String userID = Constant.generateUUID();
        String profileID = Constant.generateUUID();
        Profile testProfile = new Profile(profileID,"test_aboutme",5.0,"test_Phonenumber",userID);
        String jsonString = gson.toJson(testProfile, Profile.class);
        String ret = api.insertProfile(jsonString);
        assertEquals(ret,Constant.SUCCESS);

        testProfile.setAbout_me("old_aboutme");
        jsonString = gson.toJson(testProfile,Profile.class);
        ret = api.updateAboutMeByUserId(userID, "new_aboutme");
        assertEquals(Constant.SUCCESS, ret);

        String aboutMe = testProfile.getAbout_me();
        Profile profileChanged = gson.fromJson(aboutMe,new TypeToken<Profile>(){}.getType());
        assertTrue(profileChanged.getAbout_me() == "new_aboutme");

        //test if rating, phone_numbers are remain unchanged.
        assertTrue(profileChanged.getPhone_number().equals(testProfile.getPhone_number()));
        assertTrue(profileChanged.getRating() == testProfile.getRating());
    }

    @Test
    public void updatePhoneNumberByUserIdTest(){
        String userID = Constant.generateUUID();
        String profileID = Constant.generateUUID();
        Profile testProfile = new Profile(profileID,"test_aboutme",5.0,"test_Phonenumber",userID);
        String jsonString = gson.toJson(testProfile, Profile.class);
        String ret = api.insertProfile(jsonString);
        assertEquals(ret,Constant.SUCCESS);

        testProfile.setPhone_number("old_phonenumber");
        jsonString = gson.toJson(testProfile,Profile.class);
        ret = api.updatePhonenumberByUserId(userID, "new_phonenumber");
        assertEquals(Constant.SUCCESS, ret);

        String phoneNumbe = testProfile.getPhone_number();
        Profile profileChanged = gson.fromJson(phoneNumbe,new TypeToken<Profile>(){}.getType());
        assertTrue(profileChanged.getPhone_number()=="new_phonenumber");

        assertTrue(profileChanged.getAbout_me().equals(testProfile.getAbout_me()));
        assertTrue(profileChanged.getRating() == testProfile.getRating());

    }


}
