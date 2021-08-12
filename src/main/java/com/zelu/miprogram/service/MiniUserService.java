package com.zelu.miprogram.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.domain.MiniPaper;
import com.zelu.miprogram.domain.MiniSubject;
import com.zelu.miprogram.domain.MiniUser;
import com.zelu.miprogram.domain.Params.MiniBuessinessRankParam;
import com.zelu.miprogram.domain.Params.MiniSubjectListParam;
import com.zelu.miprogram.domain.Params.MiniUserParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

public interface MiniUserService extends IService<MiniUser> {
    ServerResponse<String> create_user(MiniUserParam param);

    ServerResponse<String> import_user(MultipartFile file) throws Exception;

    ServerResponse<String> update_user(MiniUserParam param);

    ServerResponse<String> delete_user(String userNo);

    ServerResponse<IPage<MiniUser>> list_user(MiniUserParam param);

    ServerResponse<String> user_login_out();

    ServerResponse<Map<String, Object>> user_login(String account);

    ServerResponse<List<MiniPaper>> Wechat_Auth();

    ServerResponse<List<MiniSubject>> Wechat_Subject(String paper_no);

    ServerResponse<Integer> Wechat_Submit(MiniSubjectListParam subjects);

    ServerResponse<List<MiniBuessinessRankParam>> Wechat_Rank(int index, int size);
}