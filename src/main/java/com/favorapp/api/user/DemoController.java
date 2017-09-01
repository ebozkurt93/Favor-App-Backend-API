package com.favorapp.api.user;

import com.favorapp.api.helper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/demo/")
public class DemoController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private MessageParamsService messageParamsService;

	@RequestMapping(value = "test")
	public String test () {
		//return new JSONResponseHelper().errorDefault(MessageCode.ERROR);
		return messageParamsService.getMessageValue(MessageCode.ERROR, LanguageCode.en);
	}

	@RequestMapping(value = "test1")
	public JSONResponse test1 () {
		return new JSONResponse(messageParamsService).errorDefault(MessageCode.ERROR);
		//return messageParamsService.getMessageValue(MessageCode.ERROR, LanguageCode.en);
	}
	/*
	@RequestMapping(value = "/secure/all")
	public List<User> getAllUsers(@RequestHeader(value = "Authorization") String jwt) throws ServletException {
		if (JwtMyHelper.getIfJWTAdmin(jwt)) {
			return userService.getAllUsers();
		} else {
			throw new ServletException("You are not authorized to do that");
		}
	}
*/
}
