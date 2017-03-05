package template.Profile;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Profile {
    @NotNull
    @Size(min = 32, max = 32)
    private String id;

    @NotNull
    @Size(max = 255)
    private String about_me;

    @NotNull
    private double rating;

    @NotNull
    @Size(max = 10)
    private String phone_number;

    @NotNull
    @Size(min = 32, max = 32)
    private String user_id;

    public Profile(String id, String about_me, double rating, String phone_number, String user_id) {
        this.id = id;
        this.about_me = about_me;
        this.rating = rating;
        this.phone_number = phone_number;
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean validate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(this).size() == 0;
    }
}
