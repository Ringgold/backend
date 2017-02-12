package api;

import com.google.common.base.Strings;
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
import template.User.User;
import template.User.UserDao;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BookApiTest {
    private DBI dbi;
    private Gson gson;
    private BookApi api;
    private String userID;

    @Before
    public void setUp() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        dbi = new DBI("jdbc:mysql://localhost/booktrader_test?useUnicode=true&characterEncoding=UTF-8&useSSL=false", "root", "delivery");
        gson = new Gson();
        api = new BookApi();
        UserDao userDao = dbi.onDemand(UserDao.class);
        api.setDao(dbi.onDemand(BookDao.class), dbi.onDemand(UserDao.class));
        Handle handle = dbi.open();
        handle.execute("DELETE FROM book");
        handle.execute("DELETE FROM user");
        handle.close();
        userID = Constant.generateUUID();
        User validUser = new User(userID, "testEmail12345@gmail.com", "lsaKSfjsalkncc2","Oscar");
        userDao.insert(validUser);
    }

    @After
    public void tearDown() {
        Handle handle = dbi.open();
        handle.execute("DELETE FROM book");
        handle.execute("DELETE FROM user");
        handle.close();
    }

    @Test
    public void getBook(){
        String testFail = api.getBook("lakfjlksd");
        assertEquals(Constant.FAIL, testFail);
        int count = 1;
        addBooks(count);
        List<Book> list = gson.fromJson(api.getAllBooks(), new TypeToken<List<Book>>(){}.getType());
        assertEquals(list.size(), count);
        Book first = list.get(0);
        String testSuccess = api.getBook(first.getId());
        Book book = gson.fromJson(testSuccess, new TypeToken<Book>(){}.getType());
        assertEquals(first.getId(), book.getId());
        assertEquals(first.getSeller(), book.getSeller());
        assertEquals(first.getPrice(), book.getPrice(),0.001);
        assertEquals(first.getDescription(), book.getDescription());
        assertEquals(first.getCode(), book.getCode());
        assertEquals(first.getAuthor(), book.getAuthor());
        assertEquals(first.getTitle(), book.getTitle());
    }

    @Test
    public void getAllBooks() {
        String testEmpty = api.getAllBooks();
        assertEquals("[]", testEmpty);
        int count = 1;
        addBooks(count);
        testEmpty = api.getAllBooks();
        List<Book> list = gson.fromJson(testEmpty, new TypeToken<List<Book>>() {}.getType());
        assertTrue(list.size()==count);
        for(int i=0;i<count;i++){
            Book test = list.get(i);
            assertTrue(test!=null);
            assertEquals("Test_Title"+i, test.getTitle());
            assertEquals("Test_Author"+i,test.getAuthor());
            assertEquals("Test_Code"+i,test.getCode());
            assertEquals("Test_Desc"+i, test.getDescription());
            assertEquals(i+1,test.getPrice(),0.001);
            assertEquals(userID, test.getSeller());
            api.deleteBook(test.getSeller(),test.getId());
        }
        testEmpty = api.getAllBooks();
        assertEquals("[]", testEmpty);
    }

    @Test
    public void getAllBooks2(){
        String testEmpty = api.getAllBooks();
        assertEquals("[]", testEmpty);
        int count = 10;
        addBooks(count);
        testEmpty = api.getAllBooks();
        List<Book> list = gson.fromJson(testEmpty, new TypeToken<List<Book>>() {}.getType());
        assertTrue(list.size()==count);
        for(int i=0;i<count;i++){
            Book test = list.get(i);
            assertTrue(test!=null);
            api.deleteBook(test.getSeller(),test.getId());
        }
        testEmpty = api.getAllBooks();
        assertEquals("[]", testEmpty);
    }
    @Test
    public void postBook() {
        Book test = new Book(Constant.generateUUID(),"Test_Title", "Test_Author", "Test_Code", 1, "Test_Desc", userID);
        String jsonString = gson.toJson(test, Book.class);
        System.out.println(jsonString);
        String ret = api.postBook(jsonString);
        System.out.println(ret);
        assertEquals(Constant.SUCCESS, ret);
        String testFail = api.postBook("laskfjasfn30920r932r9{}lsafjals");
        assertEquals(Constant.FAIL, testFail);
    }

    @Test
    public void deleteBook() {
        String testEmpty = api.deleteBook("lasfjaslkf","laskfjlaseee");
        assertEquals(Constant.FAIL, testEmpty);
        Book test = new Book("Test_ID","Test_Title", "Test_Author", "Test_Code", 1, "Test_Desc", userID);
        String jsonString = gson.toJson(test, Book.class);
        String ret = api.postBook(jsonString);
        assertEquals(Constant.SUCCESS, ret);
        String s = api.getAllBooks();
        List<Book> list = gson.fromJson(s, new TypeToken<List<Book>>() {}.getType());
        Book theBook = list.get(0);
        String testWrongID = api.deleteBook(theBook.getSeller(),"lakfjaslkfnsa");
        assertEquals(Constant.FAIL, testWrongID);
        String testWrongSeller = api.deleteBook("lkafjaslkeieieie",theBook.getId());
        assertEquals(Constant.FAIL, testWrongSeller);
        String testWorks = api.deleteBook(theBook.getSeller(),theBook.getId());
        assertEquals(Constant.SUCCESS, testWorks);
    }

    @Test
    public void updateBookPrice() {
        // Create test book
        Book testBook = new Book("Test_ID_For_Price","Test_Title", "Test_Author", "Test_Code", 1, "Test_Desc", userID);
        String jsonString = gson.toJson(testBook, Book.class);
        String ret = api.postBook(jsonString);
        assertEquals(ret, Constant.SUCCESS);

        // Get the test book's ID and set it to previously created book
        jsonString = api.getAllBooks();
        List<Book> list = gson.fromJson(jsonString, new TypeToken<List<Book>>() {}.getType());
        String testBookId = list.get(0).getId();
        testBook.setId(testBookId);

        // Update book price and verify return value
        testBook.setPrice(2.0);
        jsonString = gson.toJson(testBook, Book.class);
        ret = api.updateBook(jsonString);
        assertEquals(Constant.SUCCESS, ret);

        // Verify book price
        String bookString = api.getBook(testBookId);
        Book bookPriceChanged = gson.fromJson(bookString, new TypeToken<Book>(){}.getType());
        assertTrue(bookPriceChanged.getPrice() == 2.0);
    }

    @Test
    public void updateBookDescription() {
        // Create test book
        Book testBook = new Book("Test_ID_For_Price","Test_Title", "Test_Author", "Test_Code", 1, "Test_Desc", userID);
        String jsonString = gson.toJson(testBook, Book.class);
        String ret = api.postBook(jsonString);
        assertEquals(ret, Constant.SUCCESS);

        // Get the test book's ID and set it to previously created book
        jsonString = api.getAllBooks();
        List<Book> list = gson.fromJson(jsonString, new TypeToken<List<Book>>() {}.getType());
        String testBookId = list.get(0).getId();
        testBook.setId(testBookId);

        // Update book price and verify return value
        testBook.setDescription("Description has been changed.");
        jsonString = gson.toJson(testBook, Book.class);
        ret = api.updateBook(jsonString);
        assertEquals(Constant.SUCCESS, ret);

        // Verify book price
        String bookString = api.getBook(testBookId);
        Book bookPriceChanged = gson.fromJson(bookString, new TypeToken<Book>(){}.getType());
        assertTrue(bookPriceChanged.getDescription().equals("Description has been changed."));
    }

    private void addBooks(int count) {
        for(int i=0;i<count;i++){
            Book test = new Book("Test_ID"+i,"Test_Title"+i, "Test_Author"+i, "Test_Code"+i, i+1, "Test_Desc"+i, userID);
            String jsonString = gson.toJson(test, Book.class);
            String ret = api.postBook(jsonString);
            assertTrue(ret.equals(Constant.SUCCESS));
        }
    }

}