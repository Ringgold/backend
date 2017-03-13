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
import template.Constant;
import template.Profile.ProfileDao;
import template.User.User;
import template.User.UserDao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserApiTest {
    private UserApi api;
    private DBI dbi;
    private final User user_a = new User(Constant.generateUUID(), "test_a@gg", "12345678", "test_a", Constant.generateUUID(), 0);
    private final User user_b = new User(Constant.generateUUID(), "test_b@gg", "87654321", "test_b", Constant.generateUUID(), 1);

    @Before
    public void setUp() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        dbi = new DBI("jdbc:mysql://localhost/booktrader_test?useUnicode=true&characterEncoding=UTF-8&useSSL=false", "root", "root");
        api = new UserApi();
        api.setDao(dbi.onDemand(UserDao.class), dbi.onDemand(BookDao.class), dbi.onDemand(ProfileDao.class));
        Handle handle = dbi.open();
        handle.execute("DELETE FROM book");
        handle.execute("DELETE FROM user");
        handle.execute("DELETE FROM profile");
        handle.close();
        UserDao userDao = dbi.onDemand(UserDao.class);
        userDao.insert(user_a);
        userDao.insert(user_b);
    }

    @After
    public void tearDown() {
        Handle handle = dbi.open();
        handle.execute("DELETE FROM book");
        handle.execute("DELETE FROM user");
        handle.execute("DELETE FROM profile");
        handle.close();
    }

    @Test
    public void getAllUser() {
        String response = api.getAllUser();
        Gson gson = new Gson();
        List<User> list = gson.fromJson(response, new TypeToken<List<User>>() {
        }.getType());
        assertTrue(list.size() == 2);
        Set<String> set = new HashSet<String>();
        set.add(user_a.getName());
        set.add(user_b.getName());
        assertTrue(set.contains(list.get(0).getName()));
        assertTrue(set.contains(list.get(1).getName()));
    }

    @Test
    public void register() {
        User user_temp = new User(Constant.generateUUID(), "test@gg", "12343210", "test", "null", 0);
        Gson gson = new Gson();
        String temp = gson.toJson(user_temp);
        String response = api.register(temp);
        //assertTrue(response.equals(Constant.SUCCESS)); //to do the mail api is null here
        response = api.register("");
        assertTrue(response.equals(Constant.FAIL));
        response = api.register(null);
        assertTrue(response.equals(Constant.FAIL));
        user_temp.setPassword("1234567");
        temp = gson.toJson(user_temp);
        response = api.register(temp);
        assertTrue(response.equals(Constant.NOTVALID));
    }

    @Test
    public void activate() {
        Gson gson = new Gson();

        String resp = api.getUserById(user_a.getId());
        User userOne = gson.fromJson(resp, User.class);
        assertTrue(userOne.getStatus() == 0);

        resp = api.activate(user_a.getId(), user_a.getActivationCode());
        assertEquals(resp, Constant.SUCCESS);

        resp = api.activate(user_a.getId(), user_a.getActivationCode());
        assertEquals(resp, Constant.FAIL);

        resp = api.activate(user_a.getId(), "null");
        assertEquals(resp, Constant.FAIL);

        resp = api.getUserById(user_a.getId());
        userOne = gson.fromJson(resp, User.class);
        assertTrue(userOne.getStatus() == 1);
        assertTrue(userOne.getActivationCode().equals("null"));
    }

    @Test
    public void login() {
        User user = new User("", user_b.getEmail(), user_b.getPassword(), "", "null", 1);
        Gson gson = new Gson();
        String request = gson.toJson(user);
        String response = api.login(request);
        String comparable = gson.toJson(user_b);
        assertTrue(response.equals(comparable));

        user.setPassword(user_a.getPassword());
        request = gson.toJson(user);
        response = api.login(request);
        assertTrue(response.equals(Constant.FAIL));

        response = api.login("non sense");
        assertTrue(response.equals(Constant.FAIL));

        response = api.login(null);
        assertTrue(response.equals(Constant.FAIL));
    }

    @Test
    public void getMyBooks() {
        Book book_a = new Book(Constant.generateUUID(), "title_a", "author_a", "code_a", 10.99, "good", user_a.getId());
        Book book_b = new Book(Constant.generateUUID(), "title_b", "author_b", "code_b", 9.99, "bad", user_b.getId());
        BookApi bookApi = new BookApi();
        bookApi.setDao(dbi.onDemand(BookDao.class), dbi.onDemand(UserDao.class));
        Gson gson = new Gson();
        bookApi.postBook(gson.toJson(book_a));
        bookApi.postBook(gson.toJson(book_b));

        String response = api.getMyBooks(user_a.getId());
        assertTrue(response.contains("title_a"));
        assertTrue(!response.contains("title_b"));

        Book book_c = new Book(Constant.generateUUID(), "title_c", "author_c", "code_c", 9.99, "bad", user_b.getId());
        bookApi.postBook(gson.toJson(book_c));
        response = api.getMyBooks(user_b.getId());
        assertTrue(response.contains("title_b"));
        assertTrue(response.contains("title_c"));
        assertTrue(!response.contains("title_a"));

        response = api.getMyBooks(null);
        assertEquals(response, Constant.FAIL);
        response = api.getMyBooks("");
        assertEquals(response, Constant.FAIL);
    }

    @Test
    public void getUserById() throws Exception {
        String response = api.getUserById(user_a.getId());
        Gson gson = new Gson();
        User user = gson.fromJson(response, User.class);
        assertEquals(user.getEmail(), user_a.getEmail());
        assertEquals(user.getName(), user_a.getName());

        response = api.getUserById(null);
        assertEquals(response, Constant.FAIL);
        response = api.getUserById("");
        assertEquals(response, Constant.FAIL);
    }
}
