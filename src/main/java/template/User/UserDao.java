package template.User;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(UserMapper.class)

public interface UserDao {
    @SqlUpdate("insert into user values (:id, :email, :password, :name, :activationCode, :status)")
    void insert(@BindBean User user);

    @SqlQuery("select * from user where email = :email")
    User getUserByEmail(@Bind("email") String email);

    @SqlQuery("select * from user where id = :id")
    User getUserById(@Bind("id") String id);

    @SqlQuery("select * from user")
    List<User> getAllUser();

    @SqlUpdate("update user set status = 1, activation_code = 'null' where id = :id")
    void activate(@Bind("id") String id);
}
