package template.Book.BookPic;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookPicMapper implements ResultSetMapper<BookPic> {
    public BookPic map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new BookPic(resultSet.getString("id"),
                resultSet.getString("book_id"),
                resultSet.getString("picture"));
    }
}
