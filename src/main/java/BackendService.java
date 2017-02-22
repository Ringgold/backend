import api.BookApi;
import api.MailApi;
import api.UserApi;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;
import template.Book.BookDao;
import template.Constant;
import template.User.UserDao;

public class BackendService extends Application<Configuration> {
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Constant.dbi = new DBI("jdbc:mysql://localhost/booktrader?useUnicode=true&characterEncoding=UTF-8&useSSL=false", "root", "delivery");
        new BackendService().run(args);
    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html", "intro"));
        bootstrap.addBundle(new AssetsBundle("/assets/pages/", "/login", "login.html", "login"));
        bootstrap.addBundle(new AssetsBundle("/assets/pages/", "/sign_up", "register.html", "register"));
        bootstrap.addBundle(new AssetsBundle("/assets/pages/", "/book_detail", "postDetail.html", "postDetail"));
        bootstrap.addBundle(new AssetsBundle("/assets/pages/", "/post", "posts.html", "post"));
        bootstrap.addBundle(new AssetsBundle("/assets/pages/", "/contact", "contact.html", "contact"));
        bootstrap.addBundle(new AssetsBundle("/assets/pages/", "/search", "search.html", "search"));
        bootstrap.addBundle(new AssetsBundle("/assets/pages/", "/activate", "activate.html", "activate"));
    }

    @Override
    public void run(Configuration configuration, Environment environment) {
        final UserApi userApi = new UserApi();
        final BookApi bookApi = new BookApi();
        final MailApi mailApi = new MailApi();

        userApi.setDao(Constant.dbi.onDemand(UserDao.class), Constant.dbi.onDemand(BookDao.class));
        bookApi.setDao(Constant.dbi.onDemand(BookDao.class), Constant.dbi.onDemand(UserDao.class));

        mailApi.initMail();
        userApi.setMailApi(mailApi);

        environment.jersey().register(userApi);
        environment.jersey().register(bookApi);
        environment.jersey().register(mailApi);
    }
}