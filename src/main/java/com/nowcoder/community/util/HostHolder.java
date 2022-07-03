package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author longke
 * @description  持有用户信息，用于代替session对象
 * @create 2022/6/30 18:01
 */
@Component
public class HostHolder {
    private ThreadLocal<User> userThreadLocal=new ThreadLocal<>();

    public void setUserThreadLocal(User user) {
        userThreadLocal.set(user);
    }

    public User getUserThreadLocal() {
        return userThreadLocal.get();
    }

    public void clear(){
        userThreadLocal.remove();
    }

}
