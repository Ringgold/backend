package template.Profile;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;


import java.util.List;


public interface ProfileDao {
    @SqlUpdate("insert into profile values (:id, :about_me, :rating, :phone_number, :id)")
    void insert(@BindBean Profile profile);

    @SqlQuery("select * from profile where id = :id")
    Profile getProfileByUserId(@Bind("id") String id);

    @SqlQuery("select * from profile")
    List<Profile> getAllProfile();
}

