package cn.cruder.sbdd.dto;

import cn.cruder.sbdd.annotation.Sensitive;
import cn.cruder.sbdd.senum.SensitiveTypeEnum;
import lombok.Data;

/**
 * @Author: cruder
 * @Date: 2021/12/10/13:22
 */
@Data
public class UserInfoDto {


    private String userName;


    @Sensitive(type = SensitiveTypeEnum.PHONE_NUM)
    private String userPhone;


}
