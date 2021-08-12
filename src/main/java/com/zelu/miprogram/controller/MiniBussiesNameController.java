package com.zelu.miprogram.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.domain.MiniBussiesName;
import com.zelu.miprogram.domain.Params.MiniBussiesNameParam;
import com.zelu.miprogram.service.MiniBussiesNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangqiang
 * @Date: 2021/8/6 15:11
 */
@RestController
@RequestMapping("bussies_name")
public class MiniBussiesNameController {

    @Autowired
    private MiniBussiesNameService bussiesNameService;
    //新建
    @ResponseBody
    @PostMapping("create")
    public ServerResponse<String> CreateBussiesName(@RequestBody MiniBussiesNameParam param){
        return bussiesNameService.Create_BussiesName(param);
    }
    //更新
    @ResponseBody
    @PostMapping("update")
    public ServerResponse<String> UpdateBussiesName(@RequestBody MiniBussiesNameParam param){
        return bussiesNameService.Update_BussiesName(param);
    }
    //删除
    @ResponseBody
    @PostMapping("delete")
    public ServerResponse<String> DeleteBussiesName(String businessNo){
        return bussiesNameService.Delete_BussiesName(businessNo);
    }
    //查询企业列表
    @ResponseBody
    @PostMapping("list_all")
    public ServerResponse<List<MiniBussiesNameParam>> ListAllBussiesName(){
        return bussiesNameService.List_All_BussiesName();
    }
    //查询
    @ResponseBody
    @PostMapping("list")
    public ServerResponse<IPage<MiniBussiesName>> ListBussiesName(@RequestBody MiniBussiesNameParam param){
        return bussiesNameService.List_BussiesName(param);
    }
}
