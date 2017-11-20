package com.favorapp.api.user;

import java.util.*;

import com.favorapp.api.config.security.PasswordEncoder;
import com.favorapp.api.event.EventRequest;
import com.favorapp.api.event.EventRequestService;
import com.favorapp.api.helper.JSONResponse;
import com.favorapp.api.helper.MessageCode;
import com.favorapp.api.helper.MessageParamsService;
import com.favorapp.api.helper.partial_classes.EventRequestPrivate;
import com.favorapp.api.helper.partial_classes.client_wrappers.UserEditProfile;
import com.favorapp.api.helper.partial_classes.UserMyAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import com.favorapp.api.config.JwtMyHelper;
import com.favorapp.api.config.MailSenderService;
import com.favorapp.api.config.key.KeyFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping(path = "/user/")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private MessageParamsService messageParamsService;

    @Autowired
    private EventRequestService eventRequestService;

    @RequestMapping(method = RequestMethod.GET, value = "/hello")
    public String hello() {
        return "Hi";

    }

    @RequestMapping(method = RequestMethod.POST, value = "/emaildemo")
    public void emailDemo() throws MailException, InterruptedException {
        User user = new User();
        user.setEmail("favorapp2017@gmail.com");

        mailSenderService.sendEmail(user);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/email_validation/{p1}/{p2}/{p3}")
    public JSONResponse validateEmail(@PathVariable String p1, @PathVariable String p2, @PathVariable String p3) {

        String token = p1 + "." + p2 + "." + p3;
        System.out.println(token);
        Claims claims = Jwts.parser().setSigningKey(KeyFactory.jwtKey).parseClaimsJws(token).getBody();
        System.out.println(claims);

        String email = (String) claims.get("email_validation");
        User user = userService.getUserByEmail(email);
        Collection<UserRoles> roles = user.getRoles();
        if (!roles.contains(Role.VALIDATE_EMAIL)) {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.ALREADY_VALIDATED_ACCOUNT);
        }
        roles.remove(Role.VALIDATE_EMAIL);

        user.setRoles(roles);
        userService.addUser(user);
        return JSONResponse.successNoPayloadDefault();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public JSONResponse addNewUser(@RequestBody User u) {
        // check if email is valid
        String email = u.getEmail();
        if (!userService.isValidEmailAddress(email)) {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.EMAIL_ADDRESS_DOESNT_FIT_TO_REGEX);
        }
        // check if email is used
        else if (!userService.checkIfEmailUsed(email)) {
            Date date = new Date();
            u.setRegisterDate(date);
            u.getRoles().add(new UserRoles(u, Role.USER));

            //default parameters
            u.setPoints(100);
            u.setActiveEventCount(0);

            // TODO enable this later
            // u.getRoles().add(Role.VALIDATE_EMAIL);
            PasswordEncoder passwordEncoder = new PasswordEncoder();
            String encryptedPassword = passwordEncoder.encode(u.getPassword());
            u.setPassword(encryptedPassword);
            userService.addUser(u);

            // get email validation token
            Calendar dateNow = Calendar.getInstance();
            long t = dateNow.getTimeInMillis();
            // 1 day
            Date endDate = new Date(t + (1 * 60 * 60000));
            String jwtToken = Jwts.builder().claim("email_validation", email).setIssuedAt(new Date())
                    .setExpiration(endDate).signWith(SignatureAlgorithm.HS256, KeyFactory.jwtKey).compact();
            System.out.println(jwtToken);
            // send email to token

            String[] tokenParts = jwtToken.split("\\.");
            System.out.println(tokenParts.length);
            int hash = email.hashCode();
            String finalLink = "http://localhost:8080/user/email_validation/" + tokenParts[0] + "/" + tokenParts[1]
                    + "/" + tokenParts[2]; // continue from here
            String emailaddress = "favorapp2017@gmail.com";
            String subject = "Welcome to Boon";
            String text = "link is " + finalLink;
            System.out.println(text);
            return JSONResponse.successNoPayloadDefault();

			/*
             * TODO enable this to send emails try {
			 * mailSenderService.sendEmailWithDetails(emailaddress, subject,
			 * text); } catch (MailException | InterruptedException e1) { //
			 * TODO Auto-generated catch block e1.printStackTrace(); }
			 */
        } else
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.EMAIL_ALREADY_IN_USE);
    }

    @RequestMapping(value = "/secure/all")
    public JSONResponse getAllUsers(@RequestHeader(value = "Authorization") String jwt) {
        if (JwtMyHelper.getIfJWTAdmin(jwt) && KeyFactory.checkKeyValidity(jwt)) {
            return new JSONResponse<List<User>>().successWithPayloadDefault(userService.getAllUsers());
        } else {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/secure/getbyid/")
    public JSONResponse getUserById(@RequestBody User u, @RequestHeader(value = "Authorization") String jwt) {
        if (JwtMyHelper.getIfJWTAdmin(jwt)) {
            int id = u.getId();
            User user = userService.getUserById(id);
            if (user == null) {
                return new JSONResponse(messageParamsService).errorDefault(MessageCode.NO_USER_WITH_ID);
            }
            return new JSONResponse<User>().successWithPayloadDefault(user);
        } else {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NOT_AUTHORIZED);
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/secure/getbyemail/")
    public JSONResponse getUserByEmail(@RequestBody User u, @RequestHeader(value = "Authorization") String jwt) {
        if (JwtMyHelper.getIfJWTAdmin(jwt)) {
            String email = u.getEmail();
            User user = userService.getUserByEmail(email);
            if (user == null) {
                return new JSONResponse(messageParamsService).errorDefault(MessageCode.NO_USER_WITH_EMAIL);
            }
            return new JSONResponse<User>().successWithPayloadDefault(user);
        } else {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NOT_AUTHORIZED);
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public JSONResponse login(@RequestBody User login) {

        String jwtToken = "";
        if (login.getEmail() == null || login.getPassword() == null) {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.FILL_USERNAME_PASSWORD);
        }

        String email = login.getEmail();
        String password = login.getPassword();

        User user = userService.getUserByEmail(email);

        if (user == null) {
            //sending invalid login because we don't want to give details to user
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.INVALID_LOGIN);
        }

        String encodedPassword = user.getPassword();
        PasswordEncoder passwordEncoder = new PasswordEncoder();

        if (!passwordEncoder.matches(password, encodedPassword)) {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.INVALID_LOGIN);
        }

        if (userService.getUserByEmail(email).getRoles().contains(Role.BLOCKED)) {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.BLOCKED_ACCOUNT);
        }

        if (userService.getUserByEmail(email).getRoles().contains(Role.VALIDATE_EMAIL)) {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.EMAIL_NOT_VALIDATED);
        }

        jwtToken = new JwtMyHelper(userService).createAccessToken(user);
        return new JSONResponse<String>().successWithPayloadDefault(jwtToken);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/secure/getmyinfo")
    public JSONResponse getCurrentUserInfo(@RequestHeader(value = "Authorization") String jwt) {
        User user = new JwtMyHelper(userService).getUserFromJWT(jwt);
        UserMyAccount userMyAccount = new UserMyAccount();
        BeanUtils.copyProperties(user, userMyAccount);
        return new JSONResponse<>().successWithPayloadDefault(userMyAccount);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/resetPassword/")
    public JSONResponse resetPasswordWithMail(@RequestBody User user) {
        User u = userService.getUserByEmail(user.getEmail());
        //todo send email related to password reset to user
        return JSONResponse.successNoPayloadDefault();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/secure/editprofile")
    public JSONResponse editProfile(@RequestHeader(value = "Authorization") String jwt, @RequestBody UserEditProfile updatedInfo){
        User user = new JwtMyHelper(userService).getUserFromJWT(jwt);
        if(updatedInfo.getName() != null) {
            user.setName(updatedInfo.getName());
        }
        if (updatedInfo.getLastname() != null) {
            user.setLastname(updatedInfo.getLastname());
            System.out.println(user.getLastname());
        }
        if (updatedInfo.getDescription() != null) {
            user.setDescription(updatedInfo.getDescription());
        }
        if (updatedInfo.getEmail() != null && updatedInfo.getCurrentPassword() != null) {
            PasswordEncoder passwordEncoder = new PasswordEncoder();
            if (!passwordEncoder.matches(updatedInfo.getCurrentPassword(), user.getPassword())) {
                return new JSONResponse(messageParamsService).errorDefault(MessageCode.WRONG_PASSWORD);
            }
            else {
                //todo send an email to user before and after email address change
                user.setEmail(updatedInfo.getEmail());
            }
        }
        if(updatedInfo.getNewPassword() != null && updatedInfo.getCurrentPassword() != null) {
            PasswordEncoder passwordEncoder = new PasswordEncoder();
            if (!passwordEncoder.matches(updatedInfo.getCurrentPassword(), user.getPassword())) {
                return new JSONResponse(messageParamsService).errorDefault(MessageCode.WRONG_PASSWORD);
            }
            String newPassword = passwordEncoder.encode(updatedInfo.getNewPassword());
            user.setPassword(newPassword);
            //todo send an email to user
        }
        userService.addUser(user);

        return JSONResponse.successNoPayloadDefault();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/secure/getmyeventrequests")
    public JSONResponse sendRequest(@RequestHeader(value = "Authorization") String jwt) {
        User user = new JwtMyHelper(userService).getUserFromJWT(jwt);
        Collection<EventRequest> requests = eventRequestService.getAllRequestsToUsersEventsByUserId(user.getId());
        Collection<EventRequestPrivate> eventRequestPrivates = new ArrayList<>();
        requests.forEach(request -> {
            eventRequestPrivates.add(eventRequestService.turnEventRequestToEventRequestPrivate(request));
        });
        return new JSONResponse().successWithPayloadDefault(eventRequestPrivates);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/isemailregistered")
    public JSONResponse isEmailRegistered(@RequestBody String email) {
        String s = email.replace("\"", "");
        return new JSONResponse<>().successWithPayloadDefault(userService.checkIfEmailUsed(s));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/secure/blockbyid/")
    public JSONResponse blockUserbyId(@RequestBody User u, @RequestHeader(value = "Authorization") String jwt) {
        if (JwtMyHelper.getIfJWTAdmin(jwt)) {
            int id = u.getId();
            User user = userService.getUserById(id);
            if (user == null) {
                return new JSONResponse(messageParamsService).errorDefault(MessageCode.NO_USER_WITH_ID);
            }
            Collection<UserRoles> roles = user.getRoles();
            roles.add(new UserRoles(user, Role.BLOCKED));
            user.setRoles(roles);
            userService.addUser(user);
            KeyFactory.tokenMap.remove(user.getId());
        } else {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NOT_AUTHORIZED);
        }
        return JSONResponse.successNoPayloadDefault();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/secure/unblockbyid/")
    public JSONResponse unblockUserbyId(@RequestBody User u, @RequestHeader(value = "Authorization") String jwt) {
        if (JwtMyHelper.getIfJWTAdmin(jwt)) {
            int id = u.getId();
            User user = userService.getUserById(id);
            if (user == null) {
                return new JSONResponse(messageParamsService).errorDefault(MessageCode.NO_USER_WITH_ID);
            }
            Collection<UserRoles> roles = user.getRoles();
            roles.remove(new UserRoles(user, Role.BLOCKED));
            user.setRoles(roles);
            userService.addUser(user);
            KeyFactory.tokenMap.remove(user.getId());
        } else {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NOT_AUTHORIZED);
        }
        return JSONResponse.successNoPayloadDefault();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/secure/resetalltokens/")
    public JSONResponse resetAllTokens(@RequestHeader(value = "Authorization") String jwt) {
        if (JwtMyHelper.getIfJWTAdmin(jwt)) {
            KeyFactory.tokenMap = new HashMap<Integer, String>();
        } else {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NOT_AUTHORIZED);
        }
        return JSONResponse.successNoPayloadDefault();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/secure/resettokenforid/")
    public JSONResponse resetTokenbyId(@RequestBody User u, @RequestHeader(value = "Authorization") String jwt) {
        if (JwtMyHelper.getIfJWTAdmin(jwt)) {
            int id = u.getId();
            User user = userService.getUserById(id);
            if (user == null) {
                return new JSONResponse(messageParamsService).errorDefault(MessageCode.NO_USER_WITH_ID);
            }
            KeyFactory.tokenMap.remove(id);
        } else {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NOT_AUTHORIZED);
        }
        return JSONResponse.successNoPayloadDefault();
    }

    //TODO remove this
    @RequestMapping(method = RequestMethod.POST, value = "/makeidadmin/")
    public JSONResponse makeidadmin(@RequestBody User u) {

        int id = u.getId();
        User user = userService.getUserById(id);
        if (user == null) {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NO_USER_WITH_ID);
        }
        Collection<UserRoles> newRoles = user.getRoles();
        newRoles.add(new UserRoles(user, Role.ADMIN));
        user.setRoles(newRoles);
        userService.addUser(user);
        return JSONResponse.successNoPayloadDefault();
    }

}
