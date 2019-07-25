app.controller('cartController',function ($scope, cartService) {
    //查询购物车列表
    $scope.findCartList = function () {
        cartService.findCartList().success(
            function (response) {
            $scope.cartList = response;
            //求商品总计数和总价,sum方法在cartService.js里面
            $scope.totalValue=cartService.sum($scope.cartList);
        })
    }

    //增加减少商品数量
    $scope.addGoodsToCartList=function (itemId, num) {
        cartService.addGoodsToCartList(itemId,num).success(
            function (response) {
                if(response.success){
                    // 刷新列表
                    $scope.findCartList();
                }else {
                    //弹出提示
                    alert(response.message);
                }
            }
        );
    }

    //获取登录用户的地址列表
    $scope.findAddressListByUserId=function () {
        cartService.findAddressListByUserId().success(
            function (response) {
                $scope.addressList = response;
                //遍历addressList
                for (var i = 0; i < $scope.addressList.length ; i++) {
                    if ($scope.addressList[i].isDefault == '1'){
                        $scope.address = $scope.addressList[i];
                        break;
                    }
                }
            }
        );
    }

    //将用户选择地址放到变量中
    $scope.selectAddress=function (address) {
        $scope.address = address;

    }
    //判断是否是当前选中的地址
    $scope.isSelected=function (address) {
        if(address == $scope.address){
            return true;
        } else{
            return false;
        }
    }

    //结算方式(微信,货到付款)
    $scope.order = {paymentType:'1'};//初始化结款方式
    $scope.paymentType=function (index) {
        $scope.order.paymentType == index;
        //alert(index);
    }

    //保存订单
    $scope.submitOrder=function () {
        //设置页尾的收货地点,收货人和电话
        $scope.order.receiverAreaName=$scope.address.address;//地址
        $scope.order.receiverMobile=$scope.address.mobile;//手机
        $scope.order.receiver=$scope.address.contact;//联系人
        cartService.submitOrder($scope.order).success(
            function (response) {
                if (response.success){
                    //保存成功
                    if($scope.order.paymentType=='1'){
                        //如果是微信支付，跳转到支付页面
                        location.href="pay.html";
                    }else{
                        //如果货到付款，跳转到支付成功页面
                        location.href="paysuccess.html";
                    }
                } else {
                    //保存失败,给出提示
                    alert(response.message);
                }
            }
        );
    }


})