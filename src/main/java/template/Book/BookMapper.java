package template.Book;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements ResultSetMapper<Book> {
    public Book map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Book(resultSet.getString("id"),
                resultSet.getString("title"),
                resultSet.getString("author"),
                resultSet.getString("code"),
                resultSet.getDouble("price"),
                resultSet.getString("description"),
                resultSet.getString("seller"),
                resultSet.getInt("views"));
    }
}
