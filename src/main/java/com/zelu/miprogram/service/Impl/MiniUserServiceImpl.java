package com.zelu.miprogram.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.dao.*;
import com.zelu.miprogram.domain.*;
import com.zelu.miprogram.domain.Params.MiniBuessinessRankParam;
import com.zelu.miprogram.domain.Params.MiniSubjectListParam;
import com.zelu.miprogram.domain.Params.MiniSubjectParam;
import com.zelu.miprogram.domain.Params.MiniUserParam;
import com.zelu.miprogram.emus.PaperEmus;
import com.zelu.miprogram.emus.SubjectEmus;
import com.zelu.miprogram.emus.userPermission;
import com.zelu.miprogram.excelmethod.ExcelUtil;
import com.zelu.miprogram.exceptions.StringException;
import com.zelu.miprogram.service.MiniSubjectUserService;
import com.zelu.miprogram.service.MiniUserService;
import com.zelu.miprogram.shiro.token.EasyTypeToken;
import com.zelu.miprogram.utils.StringUtils;
import com.zelu.miprogram.utils.toolkitUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MiniUserServiceImpl extends ServiceImpl<MiniUserMapper, MiniUser> implements MiniUserService {

    @Autowired
    private MiniBussiesNameMapper bussiesNameMapper;
    @Autowired
    private MiniPaperMapper paperMapper;
    @Autowired
    private MiniPaperSubjectMapper paperSubjectMapper;
    @Autowired
    private MiniSubjectMapper subjectMapper;
    @Autowired
    private MiniPaperUserMapper paperUserMapper;
    @Autowired
    private MiniSubjectUserServiceImpl subjectUserMapper;
    @Override
    public ServerResponse<String> create_user(MiniUserParam param) {
        if(StringUtils.isBlank(userPermission.getEnumType(param.getType()))){
            return ServerResponse.createByError("??????????????????????????????");
        }
        //???????????????????????????????????????
        if(StringUtils.isBlank(param.getPhone())){
            return ServerResponse.createByError("?????????????????????");
        }
        QueryWrapper<MiniUser>users=new QueryWrapper<>();
        users.eq("phone",param.getPhone());
        MiniUser user = this.baseMapper.selectOne(users);
        if(user!=null){
            return ServerResponse.createByError("??????????????????");
        }
        //????????????
        if(StringUtils.equals(userPermission.getEnumType(param.getType()),userPermission.bussiness_user.getMsg())){
            final MiniManagerUser manageUser = toolkitUtils.getManageUser();
            MiniUser usernew=new MiniUser();
            BeanUtils.copyProperties(param,usernew);
            usernew.setUserNo(toolkitUtils.getStringId());
            usernew.setBussinesNo(manageUser.getBussinesNo());
            int insert = this.baseMapper.insert(usernew);
            if(insert==0){
                return ServerResponse.createByError("????????????????????????");
            }
            return ServerResponse.createBySuccess("????????????????????????");
        }
            MiniUser usernew=new MiniUser();
            BeanUtils.copyProperties(param,usernew);
            usernew.setUserNo(toolkitUtils.getStringId());
            int insert = this.baseMapper.insert(usernew);
            if(insert==0){
                return ServerResponse.createByError("????????????????????????");
            }
            return ServerResponse.createBySuccess("????????????????????????");
    }

    @Override
    @Transactional
    public ServerResponse<String> import_user(MultipartFile file) throws Exception {
        if(StringUtils.isNull(file)){
            return ServerResponse.createByError("?????????excel??????");
        }
        ExcelUtil<MiniUser> util=new ExcelUtil<>(MiniUser.class);
        List<MiniUser> userLists=util.importExcel(file.getInputStream());
        for(MiniUser userList:userLists){
            //???????????????????????????????????????
            if(StringUtils.isBlank(userList.getPhone())){
                throw new StringException("?????????????????????");
            }
            QueryWrapper<MiniUser>users=new QueryWrapper<>();
            users.eq("phone",userList.getPhone());
            MiniUser user = this.baseMapper.selectOne(users);
            if(user!=null){
                throw new StringException("??????????????????");
            }
            //????????????
            if(StringUtils.equals(userPermission.getEnumType(userList.getType()),userPermission.bussiness_user.getMsg())){
                final MiniManagerUser manageUser = toolkitUtils.getManageUser();
                userList.setBussinesNo(manageUser.getBussinesNo());
                userList.setUserNo(toolkitUtils.getStringId());
            }else if(StringUtils.equals(userPermission.getEnumType(userList.getType()),userPermission.tourist_user.getMsg())){
                userList.setUserNo(toolkitUtils.getStringId());
            }
        }
        //????????????
        if(!this.saveBatch(userLists)){
            throw new StringException("??????????????????");
        }
        return ServerResponse.createBySuccess("??????????????????");
    }

    @Override
    public ServerResponse<String> update_user(MiniUserParam param) {
        if(StringUtils.isNull(param)){
            return ServerResponse.createByError("???????????????????????????");
        }
        if(StringUtils.isBlank(param.getUserNo())){
            return ServerResponse.createByError("????????????????????????");
        }
        QueryWrapper<MiniUser>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_no",param.getUserNo());
        MiniUser user = this.baseMapper.selectOne(queryWrapper);
        if(StringUtils.isNull(user)){
           return ServerResponse.createByError("?????????????????????");
        }
        if(!StringUtils.equals(user.getPhone(),param.getPhone())){
            QueryWrapper<MiniUser>queryWrappers=new QueryWrapper<>();
            queryWrappers.eq("phone",param.getPhone());
            MiniUser users = this.baseMapper.selectOne(queryWrappers);
            if(!StringUtils.isNull(users)){
                return ServerResponse.createByError("????????????????????????");
            }
        }
        BeanUtils.copyProperties(param,user);
        int update = this.baseMapper.update(user, queryWrapper);
        if(update==0){
            return ServerResponse.createByError("??????????????????");
        }
        return ServerResponse.createBySuccess("??????????????????");
    }

    @Override
    public ServerResponse<String> delete_user(String userNo) {
        return null;
    }

    @Override
    public ServerResponse<IPage<MiniUser>> list_user(MiniUserParam param) {
        Page<MiniUser> itempage= new Page<>(param.getPageIndex(),param.getPageSize());
        QueryWrapper<MiniUser> queryWrapper=new QueryWrapper<>();
        if(StringUtils.isNotBlank(param.getNickName())){
            queryWrapper.like("nick_name",param.getNickName());
        }
        if(StringUtils.isNotBlank(param.getPhone())){
            queryWrapper.eq("phone",param.getPhone());
        }
        final Subject subject = SecurityUtils.getSubject();
        if(!toolkitUtils.isAdmin(subject)){
            MiniManagerUser managerUser=(MiniManagerUser)subject.getPrincipal();
            queryWrapper.eq("bussines_no",managerUser.getBussinesNo());
        }
        IPage<MiniUser> recordIPage = this.baseMapper.selectPage(itempage, queryWrapper);
        return ServerResponse.createBySuccess(recordIPage);
    }

    @Override
    public ServerResponse<String> user_login_out() {
        try {
            SecurityUtils.getSubject().logout();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return ServerResponse.createBySuccess("??????????????????");
    }

    @Override
    public ServerResponse<Map<String, Object>> user_login(String account) {
        EasyTypeToken token=new EasyTypeToken(account);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            throw new UnknownAccountException("????????????");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (subject.isAuthenticated()) {
            map.put("authToken", subject.getSession().getId());
            return ServerResponse.createBySuccess("????????????", map);
        }
        return ServerResponse.createByError("????????????", map);
    }

    @Override
    public ServerResponse<List<MiniPaper>> Wechat_Auth() {
        MiniUser login = toolkitUtils.isWxLogin();
        //????????????
        if(StringUtils.equals(userPermission.getEnumType(login.getType()),userPermission.bussiness_user.getMsg())){
            //??????????????????????????????
            QueryWrapper<MiniBussiesName>queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("bussines_no",login.getBussinesNo());
            MiniBussiesName bussiesName = bussiesNameMapper.selectOne(queryWrapper);
            //????????????????????????????????????
            if(new Date().after(bussiesName.getEndTime())){
                return ServerResponse.createByError("?????????????????????");
            }
            if(!StringUtils.isNull(bussiesName)){
                Map<String,Object>map=new HashMap<>();
                map.put("paper_range",userPermission.bussiness_user.getCode());
                map.put("paper_bussno",bussiesName.getBussinesNo());
                map.put("status",PaperEmus.Pager_Up.getCode());
                List<MiniPaper> papers = paperMapper.selectByMap(map);
                return ServerResponse.createBySuccess(papers);
            }
            //????????????
        }else if(StringUtils.equals(userPermission.getEnumType(login.getType()),userPermission.tourist_user.getMsg())){
            //???????????????????????????
            Map<String,Object>map=new HashMap<>();
            map.put("paper_range",userPermission.tourist_user.getCode());
            map.put("status",PaperEmus.Pager_Up.getCode());
            List<MiniPaper> papers = paperMapper.selectByMap(map);
            return ServerResponse.createBySuccess(papers);
        }
        throw new StringException("?????????????????????????????????????????????");
    }

    @Override
    public ServerResponse<List<MiniSubject>> Wechat_Subject(String paper_no) {
        if(StringUtils.isBlank(paper_no)){
            return ServerResponse.createByError("???????????????????????????");
        }
        QueryWrapper<MiniPaper>queryWrapper=new QueryWrapper();
        queryWrapper.eq("paper_no",paper_no);
        MiniPaper paper = paperMapper.selectOne(queryWrapper);
        if(StringUtils.isNull(paper)){
            return ServerResponse.createByError("????????????????????????");
        }
        //??????????????????????????????
        Map<String,Object>map=new HashMap<>();
        map.put("paper_no",paper_no);
        List<MiniPaperSubject> paperSubjects = paperSubjectMapper.selectByMap(map);
        List<MiniSubject>subjects=new ArrayList<>();
        //????????????
        if(StringUtils.equals(PaperEmus.getEnumType(paper.getPaperLevel()),PaperEmus.Random_Pager.getMsg())){
            for(MiniPaperSubject paperSubject:paperSubjects){
                QueryWrapper<MiniSubject>subjectquery=new QueryWrapper<>();
                subjectquery.eq("subject_no",paperSubject.getSubjectNo());
                MiniSubject subject = subjectMapper.selectOne(subjectquery);
                subjects.add(toolkitUtils.RandomSubject(subject));
            }
        }else{
            for(MiniPaperSubject paperSubject:paperSubjects){
                QueryWrapper<MiniSubject>subjectquery=new QueryWrapper<>();
                subjectquery.eq("subject_no",paperSubject.getSubjectNo());
                MiniSubject subject = subjectMapper.selectOne(subjectquery);
                subjects.add(subject);
            }
        }
        return ServerResponse.createBySuccess(subjects);
    }

    @Override
    @Transactional
    public ServerResponse<Integer> Wechat_Submit(MiniSubjectListParam subjects) {
        String uuid=toolkitUtils.getStringId();
        if(subjects.getSubjects().size()==0){
            throw new StringException("??????????????????:???????????????????????????");
        }
        if(StringUtils.isBlank(subjects.getPaperNo())){
            throw new StringException("??????????????????:???????????????????????????");
        }
        //????????????
        QueryWrapper<MiniPaper>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("paper_no",subjects.getPaperNo());
        MiniPaper paper = paperMapper.selectOne(queryWrapper);
        if(StringUtils.isNull(paper)){
            throw new StringException("??????????????????:?????????????????????????????????");
        }
        //?????????????????????
        List<MiniSubjectUser>userSubjects=new ArrayList<>();
        for(MiniSubjectParam subject:subjects.getSubjects()){
            //????????????????????????
            if(StringUtils.isBlank(subject.getSubjectNo())){
                throw new StringException("??????????????????:???????????????????????????");
            }
            QueryWrapper<MiniSubject>subjectQuery=new QueryWrapper<>();
            subjectQuery.eq("subject_no",subject.getSubjectNo());
            MiniSubject subject1 = subjectMapper.selectOne(subjectQuery);
            if(StringUtils.isNull(subject1)){
                throw new StringException("??????????????????:???????????????????????????=>"+subject.getSubjectNo());
            }
            MiniSubjectUser userSubject=new MiniSubjectUser();
            userSubject.setUserNo(subjects.getUser().getUserNo());
            userSubject.setSubjectNo(subject.getSubjectNo());//????????????
            userSubject.setSubjectAnswer(subject.getRightAnswer());//?????????????????????
            if(StringUtils.isBlank(subject.getSubjectChoice())){//???????????????
                userSubject.setUserAnswer(SubjectEmus.Subject_NotFinshed_Answer.getMsg());
                userSubject.setStatus(SubjectEmus.Subject_NotFinshed_Answer.getCode());
            }else{
                userSubject.setUserAnswer(SubjectEmus.Subject_Finshed_Answer.getMsg());
                userSubject.setStatus(SubjectEmus.Subject_Finshed_Answer.getCode());
            }
            if(StringUtils.equals(subject.getRightAnswer(),subject.getSubjectChoice())){//
                userSubject.setSorce(paper.getPeerSorce());
            }else{
                userSubject.setSorce(0);
            }
            userSubject.setDetailsUuid(uuid);
            userSubjects.add(userSubject);
        }
        //????????????
        if(!subjectUserMapper.saveBatch(userSubjects)){
            throw new StringException("??????????????????:?????????????????????????????????");
        }
        //???????????????
        int sum = userSubjects.stream().mapToInt(MiniSubjectUser::getSorce).sum();
        //????????????
        MiniPaperUser paperUser=new MiniPaperUser();
        paperUser.setUserNo(subjects.getUser().getUserNo());
        paperUser.setPaperNo(subjects.getPaperNo());
        paperUser.setDetailsUuid(uuid);
        paperUser.setSocre(sum);
        paperUser.setTestTime(subjects.getTime());
        paperUser.setPaperLevel(paper.getPaperLevel());
        int insert = paperUserMapper.insert(paperUser);
        if(insert==0){
            throw new StringException("??????????????????:???????????????????????????");
        }
        subjects.getUser().setIntegral(subjects.getUser().getIntegral()+sum);
        int count=this.baseMapper.updateById(subjects.getUser());
        if(count==0){
            throw new StringException("??????????????????:????????????????????????");
        }
        return ServerResponse.createBySuccess("??????????????????",sum);
    }

    @Override
    public ServerResponse<List<MiniBuessinessRankParam>> Wechat_Rank(int index, int size) {
        MiniUser wxLogin = toolkitUtils.isWxLogin();
        if(StringUtils.isNotBlank(wxLogin.getBussinesNo())){
            QueryWrapper<MiniBussiesName>queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("bussines_no",wxLogin.getBussinesNo());
            MiniBussiesName bussiesName = bussiesNameMapper.selectOne(queryWrapper);
            if(StringUtils.isNull(bussiesName)){
                return ServerResponse.createByError("????????????????????????????????????");
            }else{
                Page<MiniUser> page= new Page<>(index,size);
                QueryWrapper<MiniUser> pagequery=new QueryWrapper<>();
                pagequery.eq("bussines_no",wxLogin.getBussinesNo());
                IPage<MiniUser> recordIPage = this.baseMapper.selectPage(page, pagequery);
                List<MiniBuessinessRankParam>params=new ArrayList<>();
                for(MiniUser user:recordIPage.getRecords()){
                    MiniBuessinessRankParam param=new MiniBuessinessRankParam();
                    param.setUserNo(user.getUserNo());
                    param.setIntegral(user.getIntegral());
                    param.setBussinessName(bussiesName.getBussiesName());
                    param.setHeadUrl(user.getHeadimgUrl());
                    param.setNickName(user.getNickName());
                    params.add(param);
                }
                params = params.stream().sorted(Comparator.comparing(MiniBuessinessRankParam::getIntegral).reversed()).collect(Collectors.toList());
                return ServerResponse.createBySuccess(params);
            }
        }else{
            return ServerResponse.createByError("???????????????");
        }
    }
}