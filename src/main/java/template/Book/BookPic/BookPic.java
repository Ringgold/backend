package template.Book.BookPic;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BookPic {
    @NotNull
    @Size(min=32, max=32)
    private String id;

    @NotNull
    @Size(min=32, max=32)
    private String book_id;

    @NotNull
    private String picture;

    public BookPic(String id, String book_id, String picture) {
        this.id = id;
        this.book_id = book_id;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean validate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(this).size() == 0;
    }
}
