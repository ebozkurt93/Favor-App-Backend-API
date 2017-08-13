package com.favorapp.api.user;

import com.favorapp.api.config.JwtMyHelper;
import com.favorapp.api.config.key.KeyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/")
public class AdminController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/secure/all")
    public List<User> getAllUsers(@RequestHeader(value = "Authorization") String jwt) throws ServletException {
        if (JwtMyHelper.getIfJWTAdmin(jwt) && KeyFactory.checkKeyValidity(jwt)) {
            return userService.getAllUsers();
        } else {
            throw new ServletException("You are not authorized to do that");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/secure/getbyid/")
    public User getUserById(@RequestBody User u, @RequestHeader(value = "Authorization") String jwt)
            throws ServletException {
        if (JwtMyHelper.getIfJWTAdmin(jwt)) {
            int id = u.getId();
            User user = userService.getUserById(id);
            if (user == null) {
                throw new ServletException("No user with that id");
            }
            return user;
        } else {
            throw new ServletException("You are not authorized to do that");
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/secure/getbyemail/")
    public User getUserByEmail(@RequestBody User u, @RequestHeader(value = "Authorization") String jwt)
            throws ServletException {
        if (JwtMyHelper.getIfJWTAdmin(jwt)) {
            String email = u.getEmail();
            User user = userService.getUserByEmail(email);
            if (user == null) {
                throw new ServletException("No user with that email");
            }
            return user;
        } else {
            throw new ServletException("You are not authorized to do that");
        }

    }


    @RequestMapping(method = RequestMethod.POST, value = "/secure/blockbyid/")
    public void blockUserbyId(@RequestBody User u, @RequestHeader(value = "Authorization") String jwt)
            throws ServletException {
        if (JwtMyHelper.getIfJWTAdmin(jwt)) {
            int id = u.getId();
            User user = userService.getUserById(id);
            if (user == null) {
                throw new ServletException("No user with that id");
            }
            Collection<Role> roles = user.getRoles();
            roles.add(Role.BLOCKED);
            user.setRoles(roles);
            userService.addUser(user);
            KeyFactory.tokenMap.remove(user.getId());
        } else {
            throw new ServletException("You are not authorized to do that");
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/secure/unblockbyid/")
    public void unblockUserbyId(@RequestBody User u, @RequestHeader(value = "Authorization") String jwt)
            throws ServletException {
        if (JwtMyHelper.getIfJWTAdmin(jwt)) {
            int id = u.getId();
            User user = userService.getUserById(id);
            if (user == null) {
                throw new ServletException("No user with that id");
            }
            Collection<Role> roles = user.getRoles();
            roles.remove(Role.BLOCKED);
            user.setRoles(roles);
            userService.addUser(user);
            KeyFactory.tokenMap.remove(user.getId());
        } else {
            throw new ServletException("You are not authorized to do that");
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/secure/resetalltokens/")
    public void resetAllTokens(@RequestHeader(value = "Authorization") String jwt) throws ServletException {
        if (JwtMyHelper.getIfJWTAdmin(jwt)) {
            KeyFactory.tokenMap = new HashMap<Integer, String>();
        } else {
            throw new ServletException("You are not authorized to do that");
        }

    }

    //TODO remove this
    @RequestMapping(method = RequestMethod.POST, value = "/makeidadmin/")
    public void makeidadmin(@RequestBody User u) throws ServletException {

        int id = u.getId();
        User user = userService.getUserById(id);
        if (user == null) {
            throw new ServletException("No user with that id");
        } else {
            Collection<Role> newRoles = user.getRoles();
            newRoles.add(Role.ADMIN);
            user.setRoles(newRoles);
            userService.addUser(user);
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/secure/resettokenforid/")
    public void resetTokenbyId(@RequestBody User u, @RequestHeader(value = "Authorization") String jwt)
            throws ServletException {
        if (JwtMyHelper.getIfJWTAdmin(jwt)) {
            int id = u.getId();
            User user = userService.getUserById(id);
            if (user == null) {
                throw new ServletException("No user with that id");
            }
            KeyFactory.tokenMap.remove(id);
        } else {
            throw new ServletException("You are not authorized to do that");
        }

    }

}
