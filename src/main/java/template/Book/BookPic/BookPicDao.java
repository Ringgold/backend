package template.Book.BookPic;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(BookPicMapper.class)
public interface BookPicDao {
    @SqlUpdate("insert into book_pic values (:id, :book_id, :picture)")
    void insertBookPic(@BindBean BookPic bookPic);

    @SqlQuery("select * from book_pic where book_id = :book_id")
    List<BookPic> getBookPicByBookId(@Bind("book_id") String bookId);

    @SqlUpdate("delete from book_pic where book_id = :book_id")
    void deleteBookPicByBookId(@Bind("book_id") String bookId);

}
