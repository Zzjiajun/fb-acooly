package com.acooly.showcase.facade.customer.dto;

import com.acooly.core.common.facade.InfoBase;
import com.acooly.showcase.common.enums.CustomerType;
import com.acooly.showcase.common.enums.IdcardType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author qiuboboy@qq.com
 * @date 2018-03-20 16:12
 */
@Getter
@Setter
public class CustomerDto extends InfoBase {
  @NotBlank private String username;

  @NotNull
  @Min(0)
  @Max(100)
  private Integer age;

  private Date birthday;
  @NotNull private Integer gender;
  private String realName;
  private IdcardType idcardType;
  private String idcardNo;
  private String mobileNo;
  private String mail;
  private String subject;
  private CustomerType customerType;
  private Double fee;

  private Long salary;

  private String comments;
}
