 //控制层 
app.controller('typeTemplateController',function($scope,$controller,typeTemplateService,brandService,specificationService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		typeTemplateService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		typeTemplateService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体
	// 修改模板,首先要回线数据
	$scope.findOne=function(id){				
		typeTemplateService.findOne(id).success(
			function(response){
				$scope.entity= response;
				$scope.entity.specIds = JSON.parse($scope.entity.specIds);
				$scope.entity.brandIds = JSON.parse($scope.entity.brandIds);
				$scope.entity.customAttributeItems = JSON.parse($scope.entity.customAttributeItems);
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=typeTemplateService.update( $scope.entity ); //修改  
		}else{
			serviceObject=typeTemplateService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){
		if (confirm("你确定要删除吗?")) {
			//获取选中的复选框
			typeTemplateService.dele($scope.selectIds).success(
				function(response){
					if(response.success){
						//刷新列表
						$scope.reloadList();
						$scope.selectIds=[];
					}
				}
			);
		}
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		typeTemplateService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	//自定义数据源(后期数据从数据库获取)
	$scope.brandList = {data:[]};
	//查询所有的品牌类表
	$scope.findBrandList = function () {
		brandService.selectOptionList().success(
			function (response) {
				//select2的数据格式必须为:{id:1,data:[{id:1,text:'华为'},{id:2,text:'联想'}]}
				$scope.brandList = {data:response};
			}
		);
	}

	//查询所有的规格类表
	$scope.specList = {data:[]};
	$scope.findSpecList = function () {
		specificationService.selectOptionList().success(
			function (respones) {
				$scope.specList = {data:respones};
			}
		);
	}

	//新增扩展属性表格
	//$scope.entity={customAttributeItems:[]};
	$scope.addTableRow = function () {
		$scope.entity.customAttributeItems.push({});
	}

	//删除扩展属性表格
	$scope.deleTableRow = function (index) {
		$scope.entity.customAttributeItems.splice(index,1);
	}
});	
