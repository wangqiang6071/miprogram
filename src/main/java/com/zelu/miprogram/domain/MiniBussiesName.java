package com.zelu.miprogram.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("企业名称")
@TableName("mini_bussies_name")
public class MiniBussiesName extends Model<MiniBussiesName> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String bussinesNo;

    private String bussiesName;

    private String contentUser;

    private String phone;

    /**生效日期**/
    private Date startTime;
    /**失效日期**/
    private Date endTime;

    private Date createTime;

    private Date updateTime;
}