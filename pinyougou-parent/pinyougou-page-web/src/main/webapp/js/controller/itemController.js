app.controller("itemController",function ($scope) {
	
	//数量加减
	$scope.addCollection = function(index){
		$scope.num += index;
		if($scope.num <1){
			$scope.num = 1;
		}		
	};
	
	//定义一个对象,用于存储用户选择的规格
	$scope.selectSpec = {};
	

	//判断某规格选项是否被用户选中
	$scope.isSelect = function(key,value){
		if($scope.selectSpec[key]==value){
			return true;
		}else{
			return false;
		}
	}
	
	
	//定义变量
	$scope.sku = {};
	//定义默认的SKU
	$scope.defaultSKU = function(){
		$scope.sku = itemList[0];
		$scope.selectSpec = JSON.parse(JSON.stringify($scope.sku.spec));
	}
	
	
	//将选择的规格添加到对象中
	$scope.selectSpecification = function(key,value){
		$scope.selectSpec[key]=value;
		//调用匹配方法
		$scope.searchSku();
	}
	
	
	//判断用户选择的规格匹配查询的itemList
	$scope.searchSku = function(){
		for(var i=0;i<itemList.length;i++){
			if($scope.matchSku(itemList[i].spec,$scope.selectSpec)){
				$scope.sku = itemList[i];
				return;
			}
		}
		$scope.sku={id:0,title:'--------',price:0};//如果没有匹配的
	}
	
	
	//定义一个方法,判断两个对象是否相等
	$scope.matchSku = function(map1,map2){
		for(var key in map1){
			if(map1[key] != map2[key]){
				return false;
			}
		}
		for(var key in map2){
			if(map2[key] != map1[key]){
				return false;
			}
		}
		return true;
	}
	
});