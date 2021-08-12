package com.zelu.miprogram.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.domain.MiniUser;

import java.util.List;

import com.zelu.miprogram.domain.Params.MiniUserParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

@Mapper
public interface MiniUserMapper  extends BaseMapper<MiniUser> {

}