package template.Book;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(BookMapper.class)

public interface BookDao {
    @SqlUpdate("insert into book values (:id, :title, :author, :code, :price, :description, :seller, :preview)")
    void insert(@BindBean Book book);

    @SqlQuery("select * from book")
    List<Book> getAllBook();

    @SqlQuery("select * from book where id = :id")
    Book getBookById(@Bind("id") String id);

    @SqlQuery("select * from book where seller = :seller")
    List<Book> getBooksByUser(@Bind("seller") String seller);

    @SqlUpdate("delete from book where id = :id")
    void deleteBookById(@Bind("id") String id);

    @SqlUpdate("update book set title = :title, author = :author, code = :code, price = :price, description = :description where id = :id")
    void updateBook(@BindBean Book book);
}