var beiMiCommon = require("BeiMiCommon");
cc.Class({
    extends: beiMiCommon,

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
    },

    // use this for initialization
    onLoad: function () {
        /**
         * 适配屏幕尺寸
         */
        this.resize();
    },
    onClickDizhu:function(){
        this.loadding();
        let object = this ;
        setTimeout(function(){
            object.scene("dizhu" , object) ;
        } , 200);
    }

    // called every frame, uncomment this function to activate update callback
    // update: function (dt) {

    // },
});
