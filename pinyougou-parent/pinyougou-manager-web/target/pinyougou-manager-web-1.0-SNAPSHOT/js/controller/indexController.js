app.controller('indexController',function ($scope, loginService) {
    $scope.login = function () {
        loginService.loginName().success(
            function (response) {
                $scope.loginName = response.loginName;
            }
        );
    }
});