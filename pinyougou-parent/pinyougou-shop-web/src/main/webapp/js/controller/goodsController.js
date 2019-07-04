 //控制层 
app.controller('goodsController' ,function($scope,$controller,$location,uploadService,goodsService,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(){
		//location.search()获得页面上的所有参数,并且封装到数组当中
		var id = $location.search()['id'];
		if (id == null){
			return;
		}
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;
				//向富文本编辑器添加商品介绍
				editor.html($scope.entity.tbGoodsDesc.introduction);
				//显示图片列表
				$scope.entity.tbGoodsDesc.itemImages= JSON.parse($scope.entity.tbGoodsDesc.itemImages);
				//回显扩扎属性,注意:如果扩展属性值并没有读取出来,那是因为和添加商品扩展属性时产生冲突
				$scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse($scope.entity.tbGoodsDesc.customAttributeItems);
				//回显规格属性
				$scope.entity.tbGoodsDesc.specificationItems=JSON.parse($scope.entity.tbGoodsDesc.specificationItems);
				//遍历SKU列表规格集合,并转换为json对象
				for(var i=0;i<$scope.entity.itemList.length;i++){
					$scope.entity.itemList[i].spec = JSON.parse( $scope.entity.itemList[i].spec);
				}
			}
		);				
	}
	//根据规格名称和选项名称返回是否被勾选
	$scope.checkAttributeValue=function(specName,optionName){
		//$scope.entity.tbGoodsDesc.specificationItems为上面字符串转JSON对象的结果
		var items= $scope.entity.tbGoodsDesc.specificationItems;
		var object= $scope.searchObjectByKey(items,'attributeName',specName);
		if(object==null){
			//如果没有查到对应的AttributeName,返回false
			return false;
		}else{
			//如果查到对应的AttributeName,再查询AttributeValue的值
			if(object.attributeValue.indexOf(optionName)>=0){
				return true;
			}else{
				return false;
			}
		}
	}
	
	//保存 
	$scope.save=function(){
		//提取文本编辑器的值
		$scope.entity.tbGoodsDesc.introduction=editor.html();
		var serviceObject;//服务层对象  				
		if($scope.entity.tbGoods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					alert('保存成功');
					//保存成功后,跳转到商品列表页
					location.href="goods.html";
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}


	$scope.searchEntity={};//定义搜索对象
	//条件搜索+分页
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	//保存商品
	$scope.add = function () {
		//获取富文本编辑框内容
		$scope.entity.tbGoodsDesc.introduction=editor.html();
		goodsService.add($scope.entity).success(
			function (response) {
				if (response.success){
					//添加成功,清空添加框
					alert(response.message);
					$scope.entity = {};
					//清空富文本编辑框
					editor.html("");
				} else {
					alert(response.message);
				}
			}
		);
	}

	//上传图片
	$scope.uploadFile = function () {
		uploadService.uploadFile().success(
			function (response) {
				if (response.success){
					//影响我们页面的值
					$scope.image_entity.url = response.message;
				} else {
					alert(response.message);
				}
			}
		);
	}

	//初始化
	$scope.entity = {tbGoodsDesc:{itemImages:[],specificationItems:[]}};
	//将当前上传的图片实体存入到图片列表
	$scope.add_image_entity = function () {
		$scope.entity.tbGoodsDesc.itemImages.push($scope.image_entity);
	}


	//列表中移除图片
	$scope.remove_image_entity=function(index){
		$scope.entity.tbGoodsDesc.itemImage.splice(index,1);
	}


	//实现一级下拉列表
	$scope.itemCat1List = function () {
		itemCatService.findByGrand(0).success(
			function (response) {
				$scope.itemCat1List = response;
			}
		);
	}

	//实现二级下拉列表
	$scope.$watch('entity.tbGoods.category1Id',function (newValue, oldValue) {
		itemCatService.findByGrand(newValue).success(
			function (response) {
				$scope.itemCat2List = response;
			}
		);
	});


	//实现三级下拉列表
	$scope.$watch('entity.tbGoods.category2Id',function (newValue, oldValue) {
		itemCatService.findByGrand(newValue).success(
			function (response) {
				$scope.itemCat3List = response;
			}
		);
	});

	//选择模板
	$scope.$watch('entity.tbGoods.category3Id',function (newValue, oldValue) {
		itemCatService.findOne(newValue).success(
			function (response) {
				$scope.entity.tbGoods.typeTemplateId = response.typeId;
			}
		);
	});

	//获取品牌下拉列表
	$scope.$watch('entity.tbGoods.typeTemplateId',function (newValue, oldValue) {
		typeTemplateService.findOne(newValue).success(
			function (response) {
				//获取typeTemplate模型对象数据
				$scope.typeTemplate = response;
				//查询将typeTemplate对象中的brandIds属性的json字符串数据转换为json对象
				$scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);

				if($location.search()['id'] == null){
					//查询将typeTemplate对象中的customAttributeItems属性的json字符串数据转换为json对象
					$scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
				}
			}
		);

		//扩展属性
		typeTemplateService.findSpecList(newValue).success(
			function (response) {
				$scope.specList = response;
		});
	});

	//初始化规格和规格选项(见上)...
	//$scope.entity = {tbGoods:{},tbGoodsDesc:{itemImage:[],specificationItems:[{"attributeName":name,"attributeValue":[value]}]}};
	$scope.updateSpecAttribute = function ($event,name,value) {
		//根据属性名判断对象是否为空
		var obj = $scope.searchObjectByKey($scope.entity.tbGoodsDesc.specificationItems, 'attributeName', name);
		if (obj == null) {
			//如果规格obj为null,那么就初始化$scope.entity.tbGoodsDesc.specificationItem
			//将上述对象添加到集合中
			$scope.entity.tbGoodsDesc.specificationItems.push({"attributeName": name, "attributeValue": [value]});
		} else {
			if ($event.target.checked) {
				//如果obj不为null,那么就直接给$scope.entity.tbGoodsDesc.specificationItem添加属性值
				obj.attributeValue.push(value);
			} else {
				obj.attributeValue.splice(obj.attributeValue.indexOf(value), 1);
				if (obj.attributeValue.length == 0) {
					$scope.entity.tbGoodsDesc.specificationItems.splice($scope.entity.tbGoodsDesc.specificationItems.indexOf(obj), 1);
				}
			}
		}
	}

		/**
		 *  构建SKU集合,获取商品的规格,规格选项等信息
		 */
		$scope.selectSpecList = function () {
			//初始化entity中的itemList属性,即SKU集合
			$scope.entity.itemList = [{spec:{},price:0,num:99999,status:'0',isDefault:'0'}];
			//获取选中的规格及规格选项集合
			var items = $scope.entity.tbGoodsDesc.specificationItems;
			//遍历规格集合
			for (var i = 0; i < items.length; i++) {
				//获取规格及对应的规格选项
				var specName = items[i].attributeName;
				var specValue = items[i].attributeValue;
				//初始化新的规格及规格选项集合
				var newItemList = [];
				//遍历entity中的itemList属性,即SKU集合
				for (var j = 0; j < $scope.entity.itemList.length; j++) {
					//获取每一个原始的SKU对象
					var oldSKU = $scope.entity.itemList[j];
					//遍历规格选项集合specValue
					for (var k = 0; k < specValue.length; k++) {
						//获取规格选项中的每一个元素
						var specValueElement = specValue[k];
						//克隆原始的SKU对象,获取新的SKU对象
						var newSKU = JSON.parse(JSON.stringify(oldSKU));
						//给新的SKU对象添加规格和规格选项属性
						newSKU.spec[specName] = specValueElement;
						//将新的SKU对象添加到newItemList集合中
						newItemList.push(newSKU);
					}
				}
				//将新的规格及规格选项集合覆盖entity对象中原有的items(即覆盖)
				$scope.entity.itemList = newItemList;
			}
		}

		//对商品显示状态进行处理,使之更加一目了然
		$scope.status = ['未审核','已审核','审核未通过','关闭'];

		//对商品等级分类进行处理,使之更加一目了然
		$scope.itemCatList=[];//商品分类列表
		//加载商品分类列表
		$scope.findItemCatList=function(){
			//获取所有的等级分类列表
			itemCatService.findAll().success(
				function(response){
					for(var i=0;i<response.length;i++){
						$scope.itemCatList[response[i].id]=response[i].name;
					}
				}
			);
		}

});
