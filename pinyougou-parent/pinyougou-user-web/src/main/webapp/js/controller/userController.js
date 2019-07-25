 //控制层 
app.controller('userController' ,function($scope,$controller,userService){
	
    $scope.register = function () {
        if($scope.entity.password!=$scope.password) {
            alert("两次输入的密码不一致，请重新输入");
            return ;
        }
        userService.add($scope.entity,$scope.code).success(
            function (response) {
                alert(response.message);
        });
    }

    //发送信息
    $scope.sendSms = function (phone) {
        if ($scope.entity.phone == ""){
            alert("请输入手机号码");
            return;
        }
        userService.sendSms($scope.entity.phone).success(
            function (respones) {
                alert(respones.message);
            }
        );
    }
});	
