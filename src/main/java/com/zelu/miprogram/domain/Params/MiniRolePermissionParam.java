package com.zelu.miprogram.domain.Params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author wangqiang
 * @Date: 2021/8/11 14:44
 */
@Data
@ApiModel("管理员权限参数")
public class MiniRolePermissionParam {
    private String roleNo;
    private String roleName;
    private List<String> permissions;
}
