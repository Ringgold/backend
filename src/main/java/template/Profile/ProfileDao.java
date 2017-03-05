package template.Profile;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface ProfileDao {
    @SqlUpdate("insert into profile values (:id, :about_me, :rating, :phone_number, :user_id)")
    void insert(@BindBean Profile profile);

    @SqlQuery("select * from profile where user_id = :user_id")
    Profile getProfileByUserId(@Bind("user_id") String userId);

    @SqlQuery("update profile set about_me = :about_me where user_id = :user_id")
    void updateAboutMe(@Bind("about_me") String aboutMe, @Bind("user_id") String userId);
}

