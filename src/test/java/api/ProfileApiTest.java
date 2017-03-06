package api;

import com.sun.tools.internal.jxc.ap.Const;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import template.Constant;
import template.Profile.Profile;
import template.Profile.ProfileDao;

import template.Book.Book;
import template.Book.BookDao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProfileApiTest {
    private DBI dbi;
    private Gson gson;
    private ProfileApi api;
    private String profileID;
    private String userID;


    @Before
    public void setUp() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        dbi = new DBI("jdbc:mysql://localhost/booktrader?useUnicode=true&characterEncoding=UTF-8&useSSL=false", "root", "delivery");
        api = new ProfileApi();
        api.setDao(dbi.onDemand(ProfileDao.class), dbi.onDemand(BookDao.class));
        Handle handle = dbi.open();
        handle.execute("DELETE FROM profile");
        handle.execute("DELETE FROM book");
        handle.close();
        profileID = Constant.generateUUID();
        userID = Constant.generateUUID();
        Profile validProfile = new Profile(profileID, "I_am_a_good_seller", 5.0, "102", userID);

    }

    @After
    public void tearDown() {
        Handle handle = dbi.open();
        handle.execute("DELETE FROM profile");
        handle.execute("DELETE FROM book");
    }


    @Test
    public void insertProfileTest(){
        Profile test = new Profile(profileID,"test_aboutme",5.0,"test_Phonenumber","test_userid");
        String jsonString = gson.toJson(test,Profile.class);
        System.out.println(jsonString);
        String ret = api.insertProfile(jsonString);
        System.out.println(ret);
        assertEquals(Constant.SUCCESS,ret);
    }

    @Test
    public void updateAboutMeByUserIdTest(){
        Profile test = new Profile(profileID,"test_aboutme",5.0,"test_Phonenumber","test_userid");
        String oldAboutme = test.getAbout_me();
        String newAboutme = api.updateAboutMeByUserId("test_userid", "new_test_aboutme");
        assertTrue(oldAboutme!=newAboutme);
    }

    @Test
    public void getProfileByBookIdTest(){
        String testEmpty = api.getProfileByBookId("fakeBookId123");
        assertEquals("[]",testEmpty);
        String testGood = api.getProfileByBookId(profileID);
        Profile profile = gson.fromJson(testGood,new TypeToken<Profile>(){}.getType());
        assertEquals(testGood,profile);
    }
}






