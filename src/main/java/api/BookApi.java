package api;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import template.Book.Book;
import template.Book.BookDao;
import template.Constant;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/book")
public class BookApi {
    private static final Logger LOG = LoggerFactory.getLogger(BookApi.class);
    private BookDao bookDao;

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

    public void setDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }
}
