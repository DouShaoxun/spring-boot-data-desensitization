package cn.cruder.sbdd.controller;

import cn.cruder.sbdd.dto.UserInfoDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: cruder
 * @Date: 2021/12/10/13:26
 */
@RestController
public class TestController {

    @GetMapping("/dd/test/userInfo")
    public UserInfoDto userInfoDto() {
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setUserName("Jack");
        userInfoDto.setUserPhone("17812345678");
        return userInfoDto;
    }
}
