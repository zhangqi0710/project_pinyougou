app.controller('indexController',function($scope,loginService) {
    $scope.login = function() {
        loginService.login().success(
            function(response) {
                $scope.loginName = response.loginName;
            }
        );
    }
});