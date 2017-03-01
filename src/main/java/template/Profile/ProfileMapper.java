package template.Profile;


import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileMapper implements ResultSetMapper<Profile> {
    public Profile map(int i, ResultSet resultSet, StatementContext statementContext )
        throws SQLException{
        return new Profile(resultSet.getString("id"),
                resultSet.getString("about_me"),
                resultSet.getString("rating"),
                resultSet.getString("phone_number"));
    }
}
