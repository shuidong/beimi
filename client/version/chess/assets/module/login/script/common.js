var beiMiCommon = require("BeiMiCommon");
cc.Class({
    extends: beiMiCommon,

    // use this for initialization
    onLoad: function () {
        /**
         * 游客登录，无需弹出注册对话框，先从本地获取是否有过期的对话数据，如果有过期的对话数据，则使用过期的对话数据续期
         * 如果没有对话数据，则重新使用游客注册接口
         */
        // this.loginFormPool = new cc.NodePool();
        // this.loginFormPool.put(cc.instantiate(this.prefab)); // 创建节点
        this.encrypt = require('encryptjs');
    },
    login:function(){
        this.io = require("IOUtils");
        this.loadding();
        if(this.io.get("user") == null){
            //发送游客注册请求
            var xhr = cc.beimi.http.httpGet("/api/guest", this.sucess , this.error , this);
        }else{
            //通过ID获取 玩家信息
            var decrypted = this.encrypt.decrypt(this.io.get("user"),cc.beimi.seckey,256) ;
            var data = JSON.parse(decrypted) ;
            if(data.token != null){     //获取用户登录信息
                var xhr = cc.beimi.http.httpGet("/api/guest?token="+data.token.id, this.sucess , this.error , this);
            }
        }
		// this.dialog = this.loginFormPool.get();
		// this.dialog.parent = this.root ;
	},
    reset:function(data , result){
        //放在全局变量
        cc.beimi.authorization = data.token.id ;
        cc.beimi.user = data.data ;
        var encrypted = this.encrypt.encrypt(result,cc.beimi.seckey,256);
        this.io.put("user" ,encrypted );
    },
    sucess:function(result , object){
        var data = JSON.parse(result) ;
        if(data!=null && data.token!=null && data.data!=null){
            //放在全局变量
            object.reset(data , result);
            //预加载场景
            setTimeout(function(){
                object.scene("defaulthall" , object) ;
            } , 1000);
        }
    },
    error:function(object){
        object.closeloadding(object.loaddingDialog);
        object.alert("网络异常，服务访问失败");
    }

    // called every frame, uncomment this function to activate update callback
    // update: function (dt) {

    // },
});
