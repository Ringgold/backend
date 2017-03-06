package api;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import template.Book.Book;
import template.Book.BookDao;
import template.Constant;
import template.Profile.Profile;
import template.Profile.ProfileDao;

import javax.ws.rs.*;

@Path("/profile")
public class ProfileApi {
    private static final Logger LOG = LoggerFactory.getLogger(ProfileApi.class);
    private BookDao bookDao;
    private ProfileDao profileDao;

    @POST
    @Path("/insert_profile")
    public String insertProfile(String request) {
        Profile profile;
        Gson gson = new Gson();
        try {
            profile = gson.fromJson(request, Profile.class);
            if (profile == null) {
                throw new NullPointerException();
            }
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
    @Path("/update_about_me_by_user_id/{user_id}/{about_me}")
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
    @Path("/update_phone_number_by_user_id/{user_id}/{phone_number}")
    public String updatePhoneNumberByUserId(@PathParam("user_id") String userId, @PathParam("phone_number") String phoneNumber) {
        try {
            profileDao.updatePhoneNumber(phoneNumber, userId);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
        return Constant.SUCCESS;
    }

    @GET
    @Path("/get_seller_profile_by_book_id/{book_id}")
    public String getProfileByBookId(@PathParam("book_id") String bookId) {
        String result;
        try {
            Book book = bookDao.getBookById(bookId);
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

    @GET
    @Path("/get_seller_profile_by_user_id/{user_id}")
    public String getProfileByUserId(@PathParam("user_id") String userId) {
        String result;
        try {
            Profile profile = profileDao.getProfileByUserId(userId);
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
    @POST
    @Path("{user_id}/update_rating")
    public String updateRating(@PathParam("user_id") String user_id, @FormParam("rating") double rating) {
        try {
            profileDao.updateRating(rating, user_id);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Constant.FAIL;
        }
        return Constant.SUCCESS;
    }


    public void setDao(ProfileDao profileDao, BookDao bookDao) {
        this.bookDao = bookDao;
        this.profileDao = profileDao;

    }
}
