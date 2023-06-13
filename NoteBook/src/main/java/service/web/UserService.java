package service.web;

import database.entity.Otp;
import database.entity.User;
import database.repository.OtpDAO;
import database.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

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

    @Transactional
    public User getByUsername(String username){
     return userDAO.get(username);
    }

    @Transactional
    public boolean exists(String username){
        return userDAO.get(username)!=null;
    }

    @Transactional
    public void registerUser(User user){
        userDAO.insert(user);
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

    @Transactional
    public void generateOtp(String email){
        Otp otp  = otpDAO.get(email);
        if (otp.getExpire().compareTo(new Timestamp(System.currentTimeMillis()))>=0){

            Random random = new Random();
            String new_otp = ""+random.nextInt(100000,1000000);
            otp.setOtp(new_otp);
            //otp.setExpire();
        }
    }

}
