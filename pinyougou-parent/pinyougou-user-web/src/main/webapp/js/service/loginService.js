app.service('loginService',function ($http) {
    //页面显示用户名
    this.showName = function () {
        return $http.get('../login/name.do');
    }
})