package template.Profile;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(ProfileMapper.class)

public interface ProfileDao {
    @SqlUpdate("insert into profile values (:id, :about_me, :rating, :rating_count, :phone_number, :user_id)")
    @SqlUpdate("insert into profile values (:id, :about_me, :rating, :phone_number, :user_id, :rating_count)")
    void insert(@BindBean Profile profile);

    @SqlQuery("select * from profile where user_id = :user_id")
    Profile getProfileByUserId(@Bind("user_id") String userId);

    @SqlUpdate("update profile set about_me = :about_me where user_id = :user_id")
    void updateAboutMe(@Bind("about_me") String aboutMe, @Bind("user_id") String userId);

    @SqlUpdate("update profile set rating = ((rating*rating_count) + :rating)/(rating_count+1), rating_count = rating_count+1 where user_id = :user_id")
    void updateRating(@Bind("rating") double rating, @Bind("user_id") String user_id);
    @SqlUpdate("update profile set phone_number = :phone_number where user_id = :user_id")
    void updatePhoneNumber(@Bind("phone_number") String phoneNumber, @Bind("user_id") String userId);
}
