package com.zelu.miprogram.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.domain.MiniManagerUser;
import com.zelu.miprogram.domain.MiniPermission;
import com.zelu.miprogram.domain.Params.MiniModelPermissionParam;
import java.util.List;
import java.util.Map;

public interface MiniPermissionService extends IService<MiniPermission> {

    ServerResponse<List<MiniModelPermissionParam>> permissionList();

    ServerResponse<List<String>> rolePermissionList(String roleNo);

    ServerResponse<List<Map<String, List<MiniPermission>>>> userPermissionList(String managerNo);
}