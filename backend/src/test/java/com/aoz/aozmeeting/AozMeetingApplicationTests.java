package com.aoz.aozmeeting;

import com.aoz.aozmeeting.POJO.User;
import com.aoz.aozmeeting.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AozMeetingApplicationTests {


    @Autowired
    private UserMapper userMapper;

    @Test
    void testInsert2(){
        User user=new User();
        // user.setType(2);
        user.setPhone("222");
        user.setUsername("刘");
        // user.setStatus(2);
        user.setDepartmentId(3L);
        int insert = userMapper.insert(user);
        System.out.println(insert);
    }

}
