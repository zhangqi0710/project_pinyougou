app.controller('payController', function ($scope, payService,$location) {
    //本地生成二维码
    $scope.createNative = function () {
        payService.createNative().success(
            function (response) {
                //金额,单位为分
                $scope.money = (response.total_fee / 100).toFixed(2);
                //订单号
                $scope.out_trade_no = response.out_trade_no;
                //二维码
                var qr = new QRious({
                    element: document.getElementById('qrious'),
                    size: 250,
                    level: 'H',
                    value: response.code_url
                });
                //无限循环该方法,查询支付状态
                $scope.queryPayStatus(response.out_trade_no);
            }
        );
    }

    //查询支付状态
    $scope.queryPayStatus = function (out_trade_no) {
        payService.queryPayStatus(out_trade_no).success(
            function (response) {
                if (response.success) {
                    //支付成功,跳转支付成功页面,页面传递参数,成功支付页面显示支付金额
                    location.href = "paysuccess.html#?money="+$scope.money;
                } else {
                    if (response.message == '二维码超时') {
                        //支付超时,跳转取消订单页面
                        location.href="payTimeOut.html";
                    } else {
                        //支付失败,跳转支付失败页面
                        location.href="payfail.html";
                    }
                }
            }
        );
    }

    //获取总金额,使用 angularJS 的页面传参
    $scope.getMoney=function(){
        return $location.search()['money'];
    }
});