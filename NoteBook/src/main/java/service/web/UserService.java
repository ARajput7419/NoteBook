package service.web;

import database.entity.User;
import database.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public User getByUsername(String username){
     return userDAO.get(username);
    }

    public boolean exists(String username){
        return userDAO.get(username)!=null;
    }

    public void registerUser(User user){
        userDAO.insert(user);
    }

    public void loginThroughOauth2(HttpServletRequest request , HttpServletResponse response, Authentication authentication){

    }
}
