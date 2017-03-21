package api;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import template.Book.BookPic.BookPic;
import template.Book.BookPic.BookPicDao;
import template.Constant;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.ArrayList;
import java.util.List;

@Path("/book_pic")
public class BookPicApi {
    private static final Logger LOG = LoggerFactory.getLogger(BookApi.class);
    private BookPicDao bookPicDao;

    public BookPicApi(BookPicDao bookPicDao) {
        this.bookPicDao = bookPicDao;
    }

    @Path("/update")
    @POST
    public String updateBookPics(String request) {
        Gson gson = new Gson();
        try {
            List<BookPic> list = gson.fromJson(request, new TypeToken<ArrayList<BookPic>>() {
            }.getType());
            if (list.size() == 0) {
                return Constant.SUCCESS;
            }
            String bookId = list.get(0).getBook_id();
            for (BookPic bookPic : list) {
                if (!bookPic.getBook_id().equals(bookId)) {
                    return Constant.FAIL;
                }
            }
            bookPicDao.deleteBookPicByBookId(bookId);
            for (BookPic bookPic : list) {
                bookPic.setId(Constant.generateUUID());
                if (!bookPic.validate()) {
                    return Constant.FAIL;
                }
            }
            for (BookPic bookPic : list) {
                bookPicDao.insertBookPic(bookPic);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
        return Constant.SUCCESS;
    }

    @Path("/get_book_pic/{book_id}")
    @GET
    public String getBookPic(@PathParam("book_id") String bookId) {
        String result;
        try {
            List<BookPic> list = bookPicDao.getBookPicByBookId(bookId);
            result = new Gson().toJson(list);
            if (result == null) {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
        return result;
    }
}
