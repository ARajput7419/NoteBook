package service.web;

import database.entity.Otp;
import database.entity.User;
import database.repository.OtpDAO;
import database.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private OtpDAO otpDAO;

    @Value("${email}")
    private String from;

    @Transactional
    public User getByUsername(String username){
     return userDAO.get(username);
    }

    public enum UserExistenceStatus{
        Oauth2,PASSWORD,NOT_EXIST;
    }

    @Transactional
    public UserExistenceStatus exists(String username){
        User user = userDAO.get(username);
        if (user == null) return UserExistenceStatus.NOT_EXIST;
        else{
            if (user.isOauth2()) return UserExistenceStatus.Oauth2;
            else return UserExistenceStatus.PASSWORD;
        }
    }

    @Transactional
    public void registerUser(User user){
        userDAO.insert(user);
    }

    @Transactional
    public void update(User user)
    {
        User temp = userDAO.get(user.getEmail());
        temp.setOauth2(user.isOauth2());
        temp.setPassword(user.getPassword());
        temp.setName(user.getName());
    }

    @Transactional
    public void loginThroughOauth2(HttpServletRequest request , HttpServletResponse response, Authentication authentication){

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email,"Through Oauth2",null));
        User user = userDAO.get(email);
        if (user == null){
            user = new User();
            user.setEmail(email);
            user.setOauth2(true);
            user.setName(name);
            userDAO.insert(user);
        }

    }

    public void sendEmail(String email,String otp) throws MessagingException {
        String body ="<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "        /* CSS styles for the email body */\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            background-color: #f2f2f2;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .otp-code {\n" +
                "            font-size: 24px;\n" +
                "            font-weight: bold;\n" +
                "            text-align: center;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .message {\n" +
                "            text-align: center;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .sent-by {\n" +
                "            text-align: center;\n" +
                "            margin-top: 20px;\n" +
                "            font-size: 14px;\n" +
                "            color: #888888;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <p class=\"message\">Your One-Time Password (OTP) is:</p>\n" +
                "        <p class=\"otp-code\">{"+otp+"}</p>\n" +
                "        <p class=\"message\">Please use this OTP to verify your account.</p>\n" +
                "        <p class=\"sent-by\">Sent By: Notebook Team</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
        EmailService.sendMail(from,email,"OTP For Registration Verification",body);
    }

    public enum OtpStatus{
        EXPIRED,FAILED,SUCCESS
    }


    @Transactional
    public OtpStatus verifyOtp(String email , String otp){
        Otp actual_otp = otpDAO.get(email);
        if (actual_otp == null) return OtpStatus.FAILED;
        else if (actual_otp.getExpire().compareTo(new Timestamp(System.currentTimeMillis()))<0) return OtpStatus.EXPIRED;
        else
        {
            if (actual_otp.getOtp().equals(otp)) return OtpStatus.SUCCESS;
            else return OtpStatus.FAILED;
        }


    }

    @Transactional
    public void otpCleanUp(){
        otpDAO.cleanUp();
    }


    @Transactional(rollbackOn = MessagingException.class)
    public void generateOtp(String email) throws MessagingException {
        Otp otp  = otpDAO.get(email);
        Random random = new Random();
        if (otp == null){
            otp = new Otp();
            otp.setOtp(random.nextInt(100000,1000000)+"");
            otp.setExpire(new Timestamp(System.currentTimeMillis()+2000*60));
            otp.setEmail(email);
            otpDAO.insert(otp);

        }
        else {

            if (otp.getExpire().compareTo(new Timestamp(System.currentTimeMillis())) < 0) {
                String new_otp = "" + random.nextInt(100000, 1000000);
                otp.setOtp(new_otp);
            }
            otp.setExpire(new Timestamp(System.currentTimeMillis() + 2000 * 60));
            otpDAO.update(otp);
        }

        sendEmail(email,otp.getOtp());
    }

}
