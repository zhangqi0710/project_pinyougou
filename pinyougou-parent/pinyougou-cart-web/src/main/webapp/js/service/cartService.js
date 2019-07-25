app.service('cartService',function($http){
    //购物车列表
    this.findCartList=function(){
        return $http.get('cart/findCartList.do');
    }

    //增加减少商品数量
    this.addGoodsToCartList=function (itemId,num) {
        return $http.get('cart/addGoodsToCartList.do?itemId='+itemId+'&num='+num);
    }

    //后去购物车列表的所有商品数量和总价
    this.sum=function(cartList) {
        var totalValue={totalNum:0, totalMoney:0.00 };//合计实体
        for (var i = 0; i < cartList.length; i++) {
            var cart = cartList[i];
            for (var j = 0; j < cart.orderItemList.length; j++) {
                //遍历购物车对象中的购物项
                var orderItem = cart.orderItemList[j];
                totalValue.totalNum+= orderItem.num;
                totalValue.totalMoney+= orderItem.totalFee;
            }
        }
        return totalValue;
    }

    //获取登录用户的地址列表
    this.findAddressListByUserId=function () {
        return $http.get('address/findAddressListByUserId.do');
    }

    //保存订单
    this.submitOrder=function(order){
        return $http.post('order/add.do',order);
    }
});