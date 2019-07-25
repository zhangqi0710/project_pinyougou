app.controller('indexController',function ($scope, loginService) {
    //页面显示登录用户名
    $scope.showName = function () {
        loginService.showName().success(
            function (response) {
                $scope.loginName=response.username;
            }
        );
    }
})