var exec = require('cordova/exec');
var platform = require('cordova/platform');

module.exports = {
    // TODO JS 中调用的 js方法，参数列表可根据业务需求定
    startService: function (message) {

        //TODO 第三个参数为 参数（回调方法,null,类名，方法名，[参数1，参数2，……]）
        exec(null,null, "ProgressDialogPlugin", "start", [message]);

    }
}