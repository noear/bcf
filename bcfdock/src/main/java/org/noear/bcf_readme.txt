BCF = Basic control framework （信息系统）

::0.0.22:://规范化命名，加强分组概念
1.isExist(...)          改为：isUserExist(...)     //用户是否存在
2.isInGroup(...)        改为：isUserInGroup(...)   //用户是否在一个组里（例：角色、部署等...都为组）

3.modifyPassword(...)   改为：setUserPassword(...)
4.modifyState(...)      改为：setUserState(...)    //设置用户的通用状态//一般用于业务需求
5.disableUser(...)      改为：setUserDisabled(...) //设置用户的禁用状态

6.新增：setUserVisibled(...)       //设置用户的可见状态



::0.0.21:: //增加关系组与关系人的输出
1.getUserByName(...)改为：getUsersByName(...)  //获取一批用户并基于name过滤 //加s表达批的意思

2.新增：getUsersByGroup(...)           //获取组下面的用户  //一般应用为角色下的用户
3.新增：BcfUserModel.state 字段         //一般用于业务需求
4.新增：isInGroup(...)                 //用户是否在一个组里 //一般应用为角色检查

5.新增：getUserGroups(...)             //获取用户有关系的组
6.新增：getUserPersons(...)            //获取用户有关系的人 //一般应用为下属或队员



::0.0.18:: //增加基于纯角色的控制
1.getMyXxxx(...) 改为：getUserXxxx(...)        //基于用户获到权限资源 //更名后可与RoleXxxx对应起来
2.hasUrlpath(...)改为：hasUrlpathByUser(...)   //检查用户是否有url.path的权限
3.hasResource(...)改为：hasResourceByUser(...) //检查用户是否有资源的权限

4.新增：getRoleXxxx(...)                   //基于角色获取权限资源
5.新增：hasUrlpathByRole(...)              //检查角色是否有url.path的权限
6.新增：hasResourceByRole(...)             //检查角色是否有资源的权限



