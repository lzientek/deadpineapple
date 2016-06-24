package com.deadpineapple.front.Forms;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by saziri on 09/04/2016.
 */
public class LoginForm {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public static String getEncryptedPassword(String clearPassword)   {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(clearPassword.getBytes());
            return new sun.misc.BASE64Encoder().encode(md.digest());
        } catch (NoSuchAlgorithmException e) {
            //_log.error("Failed to encrypt password.", e);
        }
        return "";
    }
}
