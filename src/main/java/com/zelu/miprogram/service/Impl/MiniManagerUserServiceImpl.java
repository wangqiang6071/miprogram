package com.zelu.miprogram.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.dao.MiniBussiesNameMapper;
import com.zelu.miprogram.dao.MiniManagerUserMapper;
import com.zelu.miprogram.domain.MiniBussiesName;
import com.zelu.miprogram.domain.MiniManagerUser;
import com.zelu.miprogram.domain.Params.MiniManageUserParam;
import com.zelu.miprogram.emus.ManagerUserEmus;
import com.zelu.miprogram.excelmethod.ExcelUtil;
import com.zelu.miprogram.exceptions.StringException;
import com.zelu.miprogram.service.MiniManagerUserService;
import com.zelu.miprogram.shiro.token.EasyTypeToken;
import com.zelu.miprogram.utils.StringUtils;
import com.zelu.miprogram.utils.toolkitUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangqiang
 * @Date: 2021/8/9 10:18
 */
@Service
public class MiniManagerUserServiceImpl extends ServiceImpl<MiniManagerUserMapper, MiniManagerUser> implements MiniManagerUserService {
    @Value("${shiro.password.salt}")
    private String salt;
    @Value("${shiro.password.random}")
    private int random;

    @Autowired
    private MiniBussiesNameMapper bussiesNameMapper;

    @Override
    public ServerResponse<String> Add_Manager_User(MiniManageUserParam param) {
        if(StringUtils.isNull(param)){
            return ServerResponse.createByError("传入的参数为空");
        }
        if(StringUtils.isBlank(param.getUserName())){
            return ServerResponse.createByError("传入的用户名字不能为空");
        }
        if(StringUtils.isBlank(param.getAccount())){
            return ServerResponse.createByError("传入的账号不能为空");
        }
        if(StringUtils.isBlank(param.getPassword())){
            return ServerResponse.createByError("传入的密码不能为空");
        }
        if(StringUtils.isBlank(param.getBussinesNo())){
            return ServerResponse.createByError("传入的企业编号不能为空");
        }
        //账号类型
        if(StringUtils.isBlank(ManagerUserEmus.getEnumType(param.getType()))){
            return ServerResponse.createByError("传入的账号类型不正确");
        }
        //账号是否存在
        QueryWrapper<MiniManagerUser>queryWrapper1=new QueryWrapper<>();
        queryWrapper1.eq("account",param.getAccount());
        MiniManagerUser managerUser1 = this.baseMapper.selectOne(queryWrapper1);
        if(managerUser1!=null){
            return ServerResponse.createByError("账号已存在");
        }
        //企业是否存在
        QueryWrapper<MiniBussiesName>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("bussines_no",param.getBussinesNo());
        MiniBussiesName bussiesName = bussiesNameMapper.selectOne(queryWrapper);
        if(bussiesName==null){
            return ServerResponse.createByError("企业编号不存在");
        }
        //检验密码和账号的合法性以及密码的加密
        if(!toolkitUtils.validateStrEnglish(param.getAccount())){
            return ServerResponse.createByError("账号不合法");
        }
        if(!toolkitUtils.validateStrEnglish(param.getPassword())){
            return ServerResponse.createByError("密码不合法");
        }
        MiniManagerUser managerUser=new MiniManagerUser();
        BeanUtils.copyProperties(param,managerUser);
        managerUser.setPassword(toolkitUtils.addMd5Hash(param.getPassword(),random,salt));
        managerUser.setManagerNo(toolkitUtils.getStringId());
        int count=this.baseMapper.insert(managerUser);
        if(count==0){
            return ServerResponse.createByError("管理员账号创建失败");
        }
        return ServerResponse.createBySuccess("管理员账号创建成功");
    }


    @Override
    public ServerResponse<String> Update_Manager_User(MiniManageUserParam param) {
        if(StringUtils.isNull(param)){
            return ServerResponse.createByError("传入的参数为空");
        }
        if(StringUtils.isBlank(param.getUserName())){
            return ServerResponse.createByError("传入的用户名字不能为空");
        }
        if(StringUtils.isBlank(param.getAccount())){
            return ServerResponse.createByError("传入的账号不能为空");
        }
        if(StringUtils.isBlank(param.getPassword())){
            return ServerResponse.createByError("传入的密码不能为空");
        }
        if(StringUtils.isBlank(param.getBussinesNo())){
            return ServerResponse.createByError("传入的企业编号不能为空");
        }
        //账号类型
        if(StringUtils.isBlank(ManagerUserEmus.getEnumType(param.getType()))){
            return ServerResponse.createByError("传入的账号类型不正确");
        }
        if(StringUtils.isBlank(param.getManagerNo())){
            return ServerResponse.createByError("传入的管理员编号不能为空");
        }
        //检验密码和账号的合法性以及密码的加密
        if(!toolkitUtils.validateStrEnglish(param.getAccount())){
            return ServerResponse.createByError("账号不合法");
        }
        if(!toolkitUtils.validateStrEnglish(param.getPassword())){
            return ServerResponse.createByError("密码不合法");
        }
        String md5=toolkitUtils.addMd5Hash(param.getPassword(),random,salt);
        //账号编号是否存在
        Map<String,Object> map1=new HashMap<>();
        map1.put("manager_no",param.getManagerNo());
        List<MiniManagerUser> managerUsers = this.baseMapper.selectByMap(map1);
        if(managerUsers.size()==0){
            return ServerResponse.createByError("传入的管理员编号不存在");
        }
        if(!StringUtils.equals(managerUsers.get(0).getAccount(),param.getAccount())){
            Map<String,Object> map2=new HashMap<>();
            map2.put("account",param.getAccount());
            List<MiniManagerUser> managerUsers2 = this.baseMapper.selectByMap(map2);
            if(managerUsers2.size()>0){
                return ServerResponse.createByError("账号已存在");
            }
        }
        //企业是否存在
        Map<String,Object> map3=new HashMap<>();
        map3.put("bussines_no",param.getManagerNo());
        List<MiniBussiesName> bussiesNames = bussiesNameMapper.selectByMap(map3);
        if(bussiesNames.size()==0){
            return ServerResponse.createByError("企业编号不存在");
        }
        BeanUtils.copyProperties(param,managerUsers.get(0));
        managerUsers.get(0).setPassword(md5);
        int count= this.baseMapper.updateById(managerUsers.get(0));
        if(count==0){
            return ServerResponse.createByError("账号更新失败");
        }
        return ServerResponse.createBySuccess("账号更新成功");
    }

    @Override
    public ServerResponse<String> Delete_Manager_User(String manager_no) {
        if(StringUtils.isBlank(manager_no)){
            return ServerResponse.createByError("传入的编号为空");
        }
        Map<String,Object>map=new HashMap<>();
        map.put("manager_no",manager_no);
        int count=this.baseMapper.deleteByMap(map);
        if(count==0){
            return ServerResponse.createByError("账号删除失败");
        }
        return ServerResponse.createBySuccess("账号删除成功");
    }

    @Override
    public ServerResponse<IPage<MiniManagerUser>> Select_Manager_User(MiniManageUserParam param) {
        Page<MiniManagerUser> itempage= new Page<>(param.getPageIndex(),param.getPageSize());
        QueryWrapper<MiniManagerUser> queryWrapper=new QueryWrapper<>();
        if(StringUtils.isNotBlank(param.getAccount())){
            queryWrapper.like("account",param.getAccount());
        }
        if(StringUtils.isNotBlank(param.getPhone())){
            queryWrapper.like("phone",param.getPhone());
        }
        if(StringUtils.isNotBlank(ManagerUserEmus.getEnumType(param.getType()))){
            queryWrapper.eq("type",param.getType());
        }
        final Subject subject = SecurityUtils.getSubject();
        if(!toolkitUtils.isAdmin(subject)){
            MiniManagerUser managerUser=(MiniManagerUser)subject.getPrincipal();
            queryWrapper.eq("bussines_no",managerUser.getBussinesNo());
        }
        IPage<MiniManagerUser> recordIPage = this.baseMapper.selectPage(itempage, queryWrapper);
        return ServerResponse.createBySuccess(recordIPage);
    }

    @Override
    @Transactional
    public ServerResponse<String> Batch_Manager_User(MultipartFile file) throws Exception {
        if(StringUtils.isNull(file)){
            return ServerResponse.createByError("请上传excel文件");
        }
        ExcelUtil<MiniManagerUser> util=new ExcelUtil<>(MiniManagerUser.class);
        List<MiniManagerUser> subjectLists=util.importExcel(file.getInputStream());
        for (MiniManagerUser subjectList:subjectLists){
            if(StringUtils.isNull(subjectList)){
                throw  new StringException("传入的参数为空");
            }
            if(StringUtils.isBlank(subjectList.getAccount())){
                throw  new StringException("传入的账号不能为空");
            }
            if(StringUtils.isBlank(subjectList.getPassword())){
                throw  new StringException("传入的密码不能为空");
            }
            if(StringUtils.isBlank(subjectList.getBussinesNo())){
                throw  new StringException("传入的企业编号不能为空");
            }
            //账号类型
            if(StringUtils.isBlank(ManagerUserEmus.getEnumType(subjectList.getType()))){
                throw  new StringException("传入的账号类型不正确");
            }
            //账号是否存在
            Map<String,Object> map1=new HashMap<>();
            map1.put("account",subjectList.getAccount());
            List<MiniManagerUser> managerUsers = this.baseMapper.selectByMap(map1);
            if(managerUsers.size()>0){
                throw  new StringException("账号已存在");
            }
            //企业是否存在
            Map<String,Object> map2=new HashMap<>();
            map2.put("bussines_no",subjectList.getManagerNo());
            List<MiniBussiesName> bussiesNames = bussiesNameMapper.selectByMap(map2);
            if(bussiesNames.size()==0){
                throw  new StringException("企业编号不存在");
            }
            //检验密码和账号的合法性以及密码的加密
            if(!toolkitUtils.validateStrEnglish(subjectList.getAccount())){
                throw  new StringException("账号不合法");
            }
            if(!toolkitUtils.validateStrEnglish(subjectList.getPassword())){
                throw  new StringException("密码不合法");
            }
            subjectList.setPassword(toolkitUtils.addMd5Hash(subjectList.getPassword(),random,salt));
            subjectList.setManagerNo(toolkitUtils.getStringId());
        }
        if (!this.saveBatch(subjectLists)){
            throw new StringException("批量导入失败");
        }
        return ServerResponse.createBySuccess("管理员批量导入成功");
    }

    @Override
    public ServerResponse<Map<String, Object>> user_login(String account, String password) {
        EasyTypeToken token=new EasyTypeToken(account,password);
        Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            throw new UnknownAccountException("账号错误");
        } catch (IncorrectCredentialsException e) {
            throw new IncorrectCredentialsException("密码错误");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (subject.isAuthenticated()) {
            map.put("authToken", subject.getSession().getId());
            return ServerResponse.createBySuccess("登陆成功", map);
        }
        return ServerResponse.createByError("登陆失败", map);
    }

    @Override
    public ServerResponse<MiniManagerUser> Select_One(String manager_no) {
        if(StringUtils.isBlank(manager_no)){
            return ServerResponse.createByError("传入的用户编号不能为空");
        }
        QueryWrapper<MiniManagerUser>queryWrapper=new QueryWrapper<>();
        MiniManagerUser managerUser = this.baseMapper.selectOne(queryWrapper);
        if(managerUser==null){
            return ServerResponse.createByError("当前用户不存在");
        }
        return ServerResponse.createBySuccess(managerUser);
    }
}
