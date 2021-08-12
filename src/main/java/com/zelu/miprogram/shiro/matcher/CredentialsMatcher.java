package com.zelu.miprogram.shiro.matcher;

import com.zelu.miprogram.emus.ShiroLoginType;
import com.zelu.miprogram.shiro.token.EasyTypeToken;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

//密码认证匹配
public class CredentialsMatcher extends HashedCredentialsMatcher{
	//重写HashedCredentialsMatcher中的方法
	@Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		//将系统中的token转化为重新的token
		EasyTypeToken tk = (EasyTypeToken) token;
        //如果是免密登录直接返回true
        if(tk.getType().equals(ShiroLoginType.NOPASSWD)){
            return true;
        }
        //不是免密登录，调用父类的方法:加密规则MD5
       return super.doCredentialsMatch(tk, info);
	}
}
