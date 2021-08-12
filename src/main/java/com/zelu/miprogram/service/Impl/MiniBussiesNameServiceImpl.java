package com.zelu.miprogram.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.dao.MiniBussiesNameMapper;
import com.zelu.miprogram.dao.MiniUserMapper;
import com.zelu.miprogram.domain.MiniBussiesName;
import com.zelu.miprogram.domain.MiniManagerUser;
import com.zelu.miprogram.domain.MiniUser;
import com.zelu.miprogram.domain.Params.MiniBussiesNameParam;
import com.zelu.miprogram.service.MiniBussiesNameService;
import com.zelu.miprogram.utils.StringUtils;
import com.zelu.miprogram.utils.toolkitUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * @author wangqiang
 * @Date: 2021/8/6 14:09
 */
@Service
public class MiniBussiesNameServiceImpl extends ServiceImpl<MiniBussiesNameMapper, MiniBussiesName> implements MiniBussiesNameService {

    //===================后台接口=============================
    @Autowired
    private MiniUserMapper userMapper;
    @Override
    public ServerResponse<String> Create_BussiesName(MiniBussiesNameParam param) {
        if(StringUtils.isNull(param)){
            return ServerResponse.createByError("参数不能为空");
        }
        if(StringUtils.isBlank(param.getBussiesName())){
            return ServerResponse.createByError("企业名字不能为空");
        }
        //检查企业名字是否存在
        Map<String,Object> map=new HashMap<>();
        map.put("bussies_name",param.getBussiesName());
        final List<MiniBussiesName> bussies = this.baseMapper.selectByMap(map);
        if(bussies.size()>0){
            return ServerResponse.createByError("企业名字已存在");
        }
        //有效期时间和失效日期
        if(param.getStartTime().before(new Date())){
            return ServerResponse.createByError("企业的生效日期要晚于当前日期");
        }
        if(param.getEndTime().before(param.getStartTime())){
            return ServerResponse.createByError("企业的结束日期要晚于生效日期");
        }
        MiniBussiesName bussiesName=new MiniBussiesName();
        BeanUtils.copyProperties(param,bussiesName);
        bussiesName.setBussinesNo(toolkitUtils.getStringId());
        int count=this.baseMapper.insert(bussiesName);
        if(count==0){
            return ServerResponse.createByError("企业创建失败");
        }
        return ServerResponse.createBySuccess("企业创建成功");
    }

    @Override
    public ServerResponse<String> Update_BussiesName(MiniBussiesNameParam param) {
        if(StringUtils.isNull(param)){
            return ServerResponse.createByError("参数不能为空");
        }
        if(StringUtils.isBlank(param.getBussiesNo())){
            return ServerResponse.createByError("企业编号不能为空");
        }
        //检查企业是否存在
        Map<String,Object> map=new HashMap<>();
        map.put("bussies_no",param.getBussiesNo());
        final List<MiniBussiesName> bussies = this.baseMapper.selectByMap(map);
        if(bussies.size()==0){
            return ServerResponse.createByError("企业不存在");
        }
        if(!StringUtils.equals(param.getBussiesName(),bussies.get(0).getBussiesName())){
            //检查企业名字是否存在重复
            Map<String,Object> map1=new HashMap<>();
            map.put("bussies_name",param.getBussiesName());
            final List<MiniBussiesName> bussiess = this.baseMapper.selectByMap(map1);
            if(bussiess.size()>0){
                return ServerResponse.createByError("企业名字已存在");
            }
        }
        if(param.getStartTime()!=null){
            //有效期时间和失效日期
            if(param.getStartTime().before(new Date())){
                return ServerResponse.createByError("企业的生效日期要晚于当前日期");
            }
        }
        if(param.getEndTime()!=null){
            if(param.getEndTime().before(param.getStartTime())){
                return ServerResponse.createByError("企业的结束日期要晚于生效日期");
            }
        }
        BeanUtils.copyProperties(param,bussies.get(0));
        int count=this.baseMapper.updateById(bussies.get(0));
        if(count==0){
            return ServerResponse.createByError("企业信息更新失败");
        }
        return ServerResponse.createBySuccess("企业信息更新成功");
    }

    @Override
    public ServerResponse<String> Delete_BussiesName(String businessNo) {
        //检查一下企业编号是否存在
        if(StringUtils.isBlank(businessNo)){
            return ServerResponse.createByError("企业编号不能为空");
        }
        Map<String,Object>map=new HashMap<>();
        map.put("business_no",businessNo);
        List<MiniBussiesName> bussiesNames = this.baseMapper.selectByMap(map);
        if(bussiesNames.size()==0){
            return ServerResponse.createByError("企业编号不存在");
        }
        //检查一下企业下面是否存在用户
        List<MiniUser> miniUsers = userMapper.selectByMap(map);
        if(miniUsers.size()>0){
            return ServerResponse.createByError("企业下存在用户不能删除");
        }
        int count=this.baseMapper.deleteByMap(map);
        if(count==0){
            return ServerResponse.createByError("企业删除失败");
        }
        return ServerResponse.createBySuccess("企业删除成功");
    }

    @Override
    public ServerResponse<IPage<MiniBussiesName>> List_BussiesName(MiniBussiesNameParam param) {
        Page<MiniBussiesName> itempage= new Page<>(param.getPageIndex(),param.getPageSize());
        QueryWrapper<MiniBussiesName> queryWrapper=new QueryWrapper<>();
        if(StringUtils.isNotBlank(param.getBussiesName())){
            queryWrapper.like("bussies_name",param.getBussiesName());
        }
        IPage<MiniBussiesName> recordIPage = this.baseMapper.selectPage(itempage, queryWrapper);
        return ServerResponse.createBySuccess(recordIPage);
    }

    @Override
    public ServerResponse<List<MiniBussiesNameParam>> List_All_BussiesName() {
        final Subject subject = SecurityUtils.getSubject();
        if(toolkitUtils.isAdmin(subject)){
            Map<String,Object>map=new HashMap<>();
            List<MiniBussiesName> miniBussiesNames = this.baseMapper.selectByMap(map);
            List<MiniBussiesNameParam>params=new ArrayList<>();
            for(MiniBussiesName miniBussiesName:miniBussiesNames){
                MiniBussiesNameParam param=new MiniBussiesNameParam();
                param.setBussiesName(miniBussiesName.getBussiesName());
                param.setBussiesNo(miniBussiesName.getBussinesNo());
                params.add(param);
            }
            return ServerResponse.createBySuccess(params);
        }else{
            MiniManagerUser managerUser=(MiniManagerUser)subject.getPrincipal();
            Map<String,Object>map=new HashMap<>();
            map.put("bussines_no",managerUser.getBussinesNo());
            List<MiniBussiesName> miniBussiesNames = this.baseMapper.selectByMap(map);
            List<MiniBussiesNameParam>params=new ArrayList<>();
            for(MiniBussiesName miniBussiesName:miniBussiesNames){
                MiniBussiesNameParam param=new MiniBussiesNameParam();
                param.setBussiesName(miniBussiesName.getBussiesName());
                param.setBussiesNo(miniBussiesName.getBussinesNo());
                params.add(param);
            }
            return ServerResponse.createBySuccess(params);
        }
    }
}
