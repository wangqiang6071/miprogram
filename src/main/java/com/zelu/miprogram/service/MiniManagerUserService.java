package com.zelu.miprogram.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.domain.MiniManagerUser;
import com.zelu.miprogram.domain.Params.MiniManageUserParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author wangqiang
 * @Date: 2021/8/9 10:14
 */
public interface MiniManagerUserService extends IService<MiniManagerUser> {
    ServerResponse<String> Add_Manager_User(MiniManageUserParam param);

    ServerResponse<String> Update_Manager_User(MiniManageUserParam param);

    ServerResponse<String> Delete_Manager_User(String manager_no);

    ServerResponse<IPage<MiniManagerUser>> Select_Manager_User(MiniManageUserParam param);

    ServerResponse<String> Batch_Manager_User(MultipartFile file) throws Exception;

    ServerResponse<Map<String, Object>> user_login(String account, String password);

    ServerResponse<MiniManagerUser> Select_One(String manager_no);
}
