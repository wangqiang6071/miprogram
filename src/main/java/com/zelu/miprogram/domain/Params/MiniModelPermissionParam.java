package com.zelu.miprogram.domain.Params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author wangqiang
 * @Date: 2021/8/11 14:44
 */
@Data
@ApiModel("管理员权限参数")
public class MiniModelPermissionParam {

    private String modeNo;
    private String permissionNo;
    private String permissionName;
}
