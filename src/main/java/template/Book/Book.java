package template.Book;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Book {
    @NotNull
    @Size(min=32, max=32)
    private String id;

    @NotNull
    @NotEmpty
    @Size(max=255)
    private String title;

    @NotNull
    @NotEmpty
    @Size(max=255)
    private String author;

    @NotNull
    @NotEmpty
    @Size(max=255)
    private String code;

    @NotNull
    private double price;

    @NotNull
    @NotEmpty
    @Size(max=255)
    private String description;

    @NotNull
    @Size(min=32, max=32)
    private String seller;

    public Book(String id, String title, String author, String code, double price, String description, String seller) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.code = code;
        this.price = price;
        this.description = description;
        this.seller = seller;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public boolean validate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(this).size() == 0;
    }
}
