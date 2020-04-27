package com.check;

import com.entity.User;

import java.util.Objects;

/**
 * ClassName UseCheck
 * Description TODO
 *
 * @author mokuanyuan
 * @version 1.0
 * @date 2020/4/19 18:02
 **/
public class CheckUser {

    public static int checkLogin(User user){
        String username = user.getUsername();
        String password = user.getPassword();
        Integer role = user.getRole();

        if(Objects.isNull(username) || username.isEmpty()) {
            return 1;
        }
        if(Objects.isNull(password) || password.isEmpty()) {
            return 2;
        }
        if(Objects.isNull(role)) {
            return 3;
        }
        if(role != 0 && role != 1 && role != 2) {
            return 4;
        }

        return 0;

    }


}
