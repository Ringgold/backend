package template.Profile;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Profile {
    @NotNull
    @Size(min=32, max=32)
    private String id;

    @NotNull
    @NotEmpty
    @Size(max=255)
    private String about_me;

    @NotNull
    @NotEmpty
    @Size(max=4)
    private String rating;

    @NotNull
    @NotEmpty
    @Size(max=10)
    private String phone_number;

    public Profile(String id, String about_me, String rating, String phone_number) {
        this.id = id;
        this.about_me = about_me;
        this.rating = rating;
        this.phone_number = phone_number;
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

    public void setAbout_me(String email) {
        this.about_me = about_me;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean validate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(this).size() == 0;
    }
}
