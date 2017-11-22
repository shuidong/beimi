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
        cc.beimi.game = {
            model : null ,
            playway : null,
            type:function(name){
                var temp ;
                if(cc.beimi.games !=null){
                    for(var i=0 ; i<cc.beimi.games.length ; i++){
                        var gamemodel = cc.beimi.games[i] ;
                        for(var inx = 0 ; inx < gamemodel.types.length ; inx++){
                            var  type = gamemodel.types[inx] ;
                            if(type.code == name){
                                temp = type ;
                            }
                        }
                    }
                }
                return temp ;
            }
        };
    },
    login:function(){
        this.io = require("IOUtils");
        this.loadding();
        if(this.io.get("userinfo") == null){
            //发送游客注册请求
            var xhr = cc.beimi.http.httpGet("/api/guest", this.sucess , this.error , this);
        }else{
            //通过ID获取 玩家信息
            var data = JSON.parse(this.io.get("userinfo")) ;
            if(data.token != null){     //获取用户登录信息
                var xhr = cc.beimi.http.httpGet("/api/guest?token="+data.token.id, this.sucess , this.error , this);
            }
        }
	},
    sucess:function(result , object){
        var data = JSON.parse(result) ;
        if(data!=null && data.token!=null && data.data!=null){
            //放在全局变量
            object.reset(data , result);
            cc.beimi.gamestatus = data.data.gamestatus ;
            /**
             * 登录成功后即创建Socket链接
             */
            object.connect();
            //预加载场景
            if(cc.beimi.gametype!=null && cc.beimi.gametype != ""){//只定义了单一游戏类型 ，否则 进入游戏大厅
                object.scene(cc.beimi.gametype , object) ;
            }else{
                /**
                 * 暂未实现功能
                 */
            }
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
