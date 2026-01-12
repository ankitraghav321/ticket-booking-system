package com.ticketBooking.util;

import org.mindrot.jbcrypt.BCrypt;

public class UserServiceUtil {

    public static String hashPassword(String plainPassword){
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String hasdedPassword){
        return BCrypt.checkpw(plainPassword, hasdedPassword);
    }
}
