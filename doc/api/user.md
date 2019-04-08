

## 用户信息

#### 一、注册用户【待完善】

    POST /graduation/user/register
    
参数

    nickName [string] : 用户昵称
    realName [string] : 真实姓名
    telephone [string] : 电话号码
    password [string] : 密码
    country [string] : 国家
    province [string] : 省份
    city [string] : 城市
    position [string] : 详细地址
    latitude [string] : 经度
    longitude [string] : 纬度
    avatarUrl [string] : 头像URL
    description [string] : 描述
    gender [int] : 性别（0：保密 1：男 2：女）
    birthday [date] : 出生日期
    
    
返回

    {
        "code": 0,
        "msg": "请求成功",
        "data": null
    }
    

#### 二、根据密码登录

    POST /graduation/user/login/pwd
    
参数

    telephone [string] : 电话号码 【必填】
    password  [string] : 密码 【必填】
    
返回

    {
        "code": 0,
        "msg": "请求成功",
        "data": {
            "userInfo": {
                "id": 3,
                "gmtCreate": 1554282432000,
                "gmtModified": 1554282432000,
                "nickName": "chenlinghong",
                "realName": "chenlinghong",
                "telephone": "13008142306",
                "gender": 1,    // 性别（0：保密 1：男 2：女）
                "birthday": 1554220800000,
                "country": "China",
                "province": "Sichuan",
                "city": "Chengdu",
                "position": "xhu university",
                "latitude": "30.00",
                "longitude": "45.00",
                "avatarUrl": "test",
                "type": 0,  // 用户类型（0：普通用户 1：商家 2：管理员 3：超级管理员）
                "description": "test"
            }
        }
    }
    
#### 三、根据短信验证码登录【待完善】

    POST /graduation/user/login/sms
    
参数

    smsCode [string] : 短信验证码 【必填】
    
返回

    {
        "code": 0,
        "msg": "请求成功",
        "data": {
            "userInfo": {
                "id": 3,
                "gmtCreate": 1554282432000,
                "gmtModified": 1554282432000,
                "nickName": "chenlinghong",
                "realName": "chenlinghong",
                "telephone": "13008142306",
                "gender": 1,    // 性别（0：保密 1：男 2：女）
                "birthday": 1554220800000,
                "country": "China",
                "province": "Sichuan",
                "city": "Chengdu",
                "position": "xhu university",
                "latitude": "30.00",
                "longitude": "45.00",
                "avatarUrl": "test",
                "type": 0,  // 用户类型（0：普通用户 1：商家 2：管理员 3：超级管理员）
                "description": "test"
            }
        }
    }