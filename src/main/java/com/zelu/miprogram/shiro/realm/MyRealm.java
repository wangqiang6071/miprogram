package com.zelu.miprogram.shiro.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zelu.miprogram.dao.MiniManagerUserMapper;
import com.zelu.miprogram.dao.MiniUserMapper;
import com.zelu.miprogram.domain.MiniManagerUser;
import com.zelu.miprogram.domain.MiniUser;
import com.zelu.miprogram.emus.ShiroLoginType;
import com.zelu.miprogram.emus.userPermission;
import com.zelu.miprogram.exceptions.StringException;
import com.zelu.miprogram.shiro.token.EasyTypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

//shiro身份校验核心类
@Slf4j
public class MyRealm extends AuthorizingRealm {

	@Value("${shiro.password.salt}")
	private String salt;

	@Autowired
	private MiniUserMapper userMapper;
	@Autowired
	private MiniManagerUserMapper managerUserMapper;

	public String getName() {
		return "MyRealm";
	}
	// 授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//		SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
//		SrcUserList account=(SrcUserList)principals.getPrimaryPrincipal();
//		SrcUserList user= userList.selectRoleListByUserAccount(account.getAccount());
//		List<SrcRoleList> roleLists = user.getRoleLists();
//		for(SrcRoleList roleList:roleLists){
//			info.addRole(roleList.getRoleName());
//			//角色下有多少个权限点
//			info.addStringPermission(roleListMapper.selectPermissonByRoleId(roleList.getId()).getPermissionKey());
//		}
		return null;
	}
	// 用户信息认证.(身份验证) 是用来验证用户身份
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 用户名：Principal 密码：Credentials
		EasyTypeToken EasyToken = (EasyTypeToken) token;
		String OnlyKey=EasyToken.getUsername();
		if(EasyToken.getType().equals(ShiroLoginType.NOPASSWD)){
			QueryWrapper<MiniUser> query=new QueryWrapper<>();
			query.eq("phone",OnlyKey);
			MiniUser user = userMapper.selectOne(query);
			if(user==null){
				throw new StringException("账号不存在");
			}
			if(user.getStatus()==userPermission.forbidden.getCode()){
				throw new DisabledAccountException("帐号已被禁止登录");
			}
			return new SimpleAuthenticationInfo(user,new ByteSourceUtils(salt), getName());
		}
		//登陆的账号是唯一的
		QueryWrapper<MiniManagerUser> query=new QueryWrapper<>();
		query.eq("account",OnlyKey);
		MiniManagerUser users=managerUserMapper.selectOne(query);
		if(users==null){
			throw new StringException("账号不存在");
		}
		if(users.getStatus()==userPermission.forbidden.getCode()){
			throw new DisabledAccountException("帐号已被禁止登录");
		}
		//身份认证成功：登录成功
		// 参数1：数据库中的对象，参数2:数据库的密码，参数3:密码加密的加盐值，参数4:MyRealm名字
		return new SimpleAuthenticationInfo(users, users.getPassword(),new ByteSourceUtils(salt), getName());
	}
}