package api;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import template.Book.Book;
import template.Book.BookDao;
import template.Constant;
import template.User.User;
import template.User.UserDao;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.ArrayList;
import java.util.List;

@Path("/book")
public class BookApi {
    private static final Logger LOG = LoggerFactory.getLogger(BookApi.class);
    private BookDao bookDao;
    private UserDao userDao;

    @Path("/all")
    @GET
    public String getAllBooks() {
        String result;
        try {
            List<Book> list = bookDao.getAllBook();
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

    @Path("/getBooksBySellerId/{seller_id}")
    @GET
    public String getBooksBySellerId(@PathParam("seller_id") String userId) {
        String result;
        try {
            List<Book> list = bookDao.getBooksByUser(userId);
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

    @Path("/searched")
    @POST
    public String getSearchedBooks(String search_data) {
        String[] userInputs = search_data.split(",");
        String search_data_lowerCase = userInputs[0].toLowerCase();
        int lowerPLimit = Integer.parseInt(userInputs[2]);
        int upperPLimit = Integer.parseInt(userInputs[3]);
        String search_result;
        try {
            List<Book> list = bookDao.getAllBook();
            List<Book> matches = new ArrayList<Book>();
            for (Book book : list) {
                if (userInputs[1].equals("2")) {
                    if ((book.getTitle().toLowerCase().contains(search_data_lowerCase))
                            && (book.getPrice() >= lowerPLimit) && (book.getPrice() <= upperPLimit)) {
                        matches.add(book);
                    }
                } else if (userInputs[1].equals("3")) {
                    if (book.getAuthor().toLowerCase().contains(search_data_lowerCase)
                            && (book.getPrice() >= lowerPLimit) && (book.getPrice() <= upperPLimit)) {
                        matches.add(book);
                    }
                } else if (userInputs[1].equals("4")) {
                    if ((book.getCode().toLowerCase().contains(search_data_lowerCase))) {
                        matches.add(book);
                    }
                } else if (userInputs[1].equals("1")) {
                    if ((book.getTitle().toLowerCase().contains(search_data_lowerCase)
                            || book.getAuthor().toLowerCase().contains(search_data_lowerCase)
                            || book.getCode().contains(search_data_lowerCase))
                            && (book.getPrice() >= lowerPLimit) && (book.getPrice() <= upperPLimit)) {
                        matches.add(book);
                    }
                }
            }
            Gson gson = new Gson();
            search_result = gson.toJson(matches);
            if (search_result == null) {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }

        return search_result;
    }

    @Path("/get_book/{book_id}")
    @GET
    public String getBook(@PathParam("book_id") String bookId) {
        String result;
        try {
            Book book = bookDao.getBookById(bookId);
            Gson gson = new Gson();
            result = gson.toJson(book);
            if (result == null || result.equals("null")) {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
        return result;
    }

    @Path("/post")
    @POST
    public String postBook(String request) {
        Book book;
        Gson gson = new Gson();
        try {
            book = gson.fromJson(request, Book.class);
            if (book == null) {
                throw new NullPointerException();
            }
            book.setId(Constant.generateUUID());
            if (!book.validate()) {
                throw new Exception();
            }
            User user = userDao.getUserById(book.getSeller());
            if (!user.validate()) {
                throw new Exception();
            }
            bookDao.insert(book);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
        return Constant.SUCCESS;
    }

    @Path("/delete/{user_id}/{book_id}")
    @GET
    public String deleteBook(@PathParam("user_id") String userId, @PathParam("book_id") String bookId) {
        try {
            Book book = bookDao.getBookById(bookId);
            if (!book.validate()) {
                return Constant.FAIL;
            }
            if (!book.getSeller().equals(userId)) {
                return Constant.FAIL;
            }
            bookDao.deleteBookById(bookId);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
        return Constant.SUCCESS;
    }

    @Path("/update")
    @POST
    public String updateBook(String request) {
        Book book;
        Book oldBook;
        Gson gson = new Gson();

        try {
            book = gson.fromJson(request, Book.class);
            if (book == null) {
                throw new NullPointerException();
            }

            oldBook = bookDao.getBookById(book.getId());
            if (oldBook == null) {
                throw new NullPointerException();
            }

            bookDao.updateBook(book);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return e.getMessage();
        }

        return Constant.SUCCESS;
    }

    public void setDao(BookDao bookDao, UserDao userDao) {
        this.bookDao = bookDao;
        this.userDao = userDao;
    }
}