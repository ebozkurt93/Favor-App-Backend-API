package com.favorapp.api.config.key;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class KeyFactory {

	public static String jwtKey;
	public static HashMap<Integer, String> tokenMap;

	@PostConstruct
	public void init() throws Exception {
		generateKey();
		tokenMap = new HashMap<Integer, String>();
	}

	public void generateKey() throws Exception {
	
		SecureRandom random = new SecureRandom();
		jwtKey = new BigInteger(130, random).toString(32);
		//TODO disable this before release
		System.out.println("JWT KEY: " + jwtKey + "\n\n\n");
	}

}
