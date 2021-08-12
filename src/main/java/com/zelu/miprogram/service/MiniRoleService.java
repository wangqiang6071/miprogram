package com.zelu.miprogram.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.domain.MiniManagerUser;
import com.zelu.miprogram.domain.MiniRole;
import com.zelu.miprogram.domain.Params.MiniManageUserInforParam;
import com.zelu.miprogram.domain.Params.MiniManageUserParam;
import com.zelu.miprogram.domain.Params.MiniRolePermissionParam;
import java.util.List;

public interface MiniRoleService extends IService<MiniRole> {

    ServerResponse<String> addRole(MiniRolePermissionParam param);

    ServerResponse<String> deleteRole(String roleNo);

    ServerResponse<String> updateRole(MiniRolePermissionParam param);

    ServerResponse<List<MiniRole>> roleList();

    ServerResponse<List<MiniManageUserInforParam>> selectUserByRole(String roleNo);

    ServerResponse<String> addAccountRole(MiniManageUserParam param);

    ServerResponse<List<MiniManageUserInforParam>> SelectUserByBussiness(String bussinessNo);
}