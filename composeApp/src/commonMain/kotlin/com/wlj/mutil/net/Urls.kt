package com.wlj.mutil.net

object Urls {

    /**
     * 固定配置
     */
    const val net = "https://www.itywnl.com"//官方网站
    const val download1 = "https://www.itywnl.com/download/"//下载

    const val bazi = "pages/index/bazi"//八字
    const val liuyao = "pages/index/sixYao"//六爻

    const val history = "app/ai/session/list"//会话列表
    const val clearHistory = "app/ai/session/delete"//删除会话（传sessionId 删除该会话 不传则删除该用户所有会话）
    const val updateTitle = "app/ai/session/update/title"//修改会话标题

    const val oneKeylogin = "app/user/login/key/login"//用户登录注册
    const val loginMail = "app/user/login/email"//邮箱用户登录&注册
    const val login = "app/user/login/phone"//用户登录注册
    const val emailCode = "app/user/email/code"//发送邮件验证码
    const val getVerifyCode = "/app/user/message/code"//获取短信验证码
    const val selfinfo = "app/user/selfinfo" //用户个人信息
    const val areaCode = "app/message/areacode/list" //app获取区号
    const val memberlist = "app/member/list" //获取会员数据
    const val createOrder = "app/order/create" //创建订单
    const val appversion = "app/config/version/update" //版本

    const val area = "user/app/region"//用户获取居住城市
    const val logoff = "app/user/writeoff"//用户注销账号


}