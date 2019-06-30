app.controller('indexController',function ($scope,$controller,loginService) {
    $scope.login = function () {
        loginService.loginName().success(
            function (response) {
                $scope.loginName = response.loginName;
            }
        );
    }
});