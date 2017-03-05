package api;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import template.Book.Book;
import template.Book.BookDao;
import template.Constant;
import template.Profile.Profile;
import template.Profile.ProfileDao;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/profile")
public class ProfileApi {
    private static final Logger LOG = LoggerFactory.getLogger(BookApi.class);
    private BookDao bookDao;
    private ProfileDao profileDao;

    @POST
    @Path("/insert_profile") //TODO unit test missing
    public String insertProfile(String request) {
        Profile profile;
        Gson gson = new Gson();
        try {
            profile = gson.fromJson(request, Profile.class);
            String id = Constant.generateUUID();
            profile.setId(id);
            if (!profile.validate()) {
                throw new Exception();
            }
            profileDao.insert(profile);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
        return Constant.SUCCESS;
    }

    @GET
    @Path("/update_about_me_by_user_id/{user_id}/{about_me}")//TODO unit test missing and should be post method
    public String updateAboutMeByUserId(@PathParam("user_id") String userId, @PathParam("about_me") String aboutMe) {
        try {
            profileDao.updateAboutMe(aboutMe, userId);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
        return Constant.SUCCESS;
    }

    @GET
    @Path("/get_seller_profile_by_book_id/{book_id}")//TODO unit test missing
    public String getProfileByBookId(@PathParam("book_id") String book_id) {
        String result;
        try {
            Book book = bookDao.getBookById(book_id);
            String sellerId = book.getSeller();
            Profile profile = profileDao.getProfileByUserId(sellerId);
            Gson gson = new Gson();
            result = gson.toJson(profile);
            if (result == null) {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
        return result;
    }



    public void setDao(ProfileDao profileDao, BookDao bookDao) {
        this.bookDao = bookDao;
        this.profileDao = profileDao;
    }
}