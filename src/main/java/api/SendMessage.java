package api;



import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;


public class SendMessage(){

    private String from = "";
    private String to = "";
    private String content = "";
    private String host = "localhost";


    public SendMessage(String receiver_email, String sender_email, String text){



                this.from = sender_email;
                this.to = receiver_email;
                this.content = text;


    }
    public void send_msg(){
            

    }







}
