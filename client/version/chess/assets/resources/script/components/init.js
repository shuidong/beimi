cc.Class({
    extends: cc.Component,

    properties: {
        // foo: {
        //    default: null,      // The default value will be used only when the component attaching
        //                           to a node for the first time
        //    url: cc.Texture2D,  // optional, default is typeof default
        //    serializable: true, // optional, default is true
        //    visible: true,      // optional, default is true
        //    displayName: 'Foo', // optional
        //    readonly: false,    // optional, default is false
        // },
        // ...
        _progress:0.0,
        _splash:null,
        _isLoading:false,
        prefab: {
            default: null,
            type: cc.Prefab
        },
        canvas: {
            default: null,
            type: cc.Canvas
        }
    },

    // use this for initialization
    onLoad: function () {
        if(!cc.sys.isNative && cc.sys.isMobile){
            var canvas = this.node.getComponent(cc.Canvas);
            canvas.fitHeight = true;
            canvas.fitWidth = true;
        }
        this.initMgr();

        cc.beimi.audio.playBGM("bgMain.mp3");
        /**
        var xhr = cc.tools.http.httpPost("/tokens",{username:'admin',password:'123456'},function(ret){
            cc.tools.http.authorization = ret ;
            cc.tools.http.httpGet("/tokens",function(ret){
                console.log("Test BeiMi Infomation:"+ret);
                if(cc.tools.http.authorization !== "" && cc.tools.http.authorization !== "-1"){
                   
                }
            });
        });
        var socket = window.io.connect('http://192.168.1.155:9081/bm/system?token=123');
        
        socket.on("connect" , function(){
             cc.log("ttt");
        });
        **/
    },
    start:function(){        
        var self = this;
        var SHOW_TIME = 3000;
        var FADE_TIME = 500;
        /***
         * 
         * 控制登录界面或者广告首屏界面显示时间
         * 
         */
    },
    initMgr:function(){
        cc.beimi = {};
        cc.beimi.http = require("HTTP");

        var Audio = require("Audio");
        cc.beimi.audio = new Audio();
        cc.beimi.audio.init();
                
        if(cc.sys.isNative){
            window.io = SocketIO;
        }else{
            window.io = require("socket.io"); 
        }
    }

});
