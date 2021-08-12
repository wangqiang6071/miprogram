package com.zelu.miprogram.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zelu.miprogram.domain.MiniPaperUser;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface MiniPaperUserMapper  extends BaseMapper<MiniPaperUser> {

}