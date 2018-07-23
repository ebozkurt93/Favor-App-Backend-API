/*package com.favorapp.api.config.firebase;

import com.favorapp.api.user.User;
import com.favorapp.api.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public MyUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(username);
        if (user == null) {
            //throw new UsernameNotFoundException(String.format("User %s does not exist!", username));
            // Usually you should throw an exception here...
            // I didnt threw the exception, as I wanted to create a user if it is authenticated by firebase
            // but does not exist in my own DB.
            return null;
        }
        return new MyUserDetails(user);
    }
}*/
