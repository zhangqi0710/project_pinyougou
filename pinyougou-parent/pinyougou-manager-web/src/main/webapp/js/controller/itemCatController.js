 //控制层 
app.controller('itemCatController' ,function($scope,$controller,itemCatService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	/*$scope.save=function(){
		var serviceObject;//服务层对象
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改
		}else{
			serviceObject=itemCatService.add( $scope.entity  );//增加
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
	}*/
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}

	$scope.searchEntity={};//定义搜索对象
	
	//搜索
	$scope.search=function(page,rows){
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    //为保存做准备
    $scope.parentId = 0;
	//根据商品等级查询所有商品
	$scope.findByGrand = function (parentId) {
	    //保存该parentId作为存储时的父id
        $scope.parentId = parentId;
		itemCatService.findByGrand(parentId).success(
			function (response) {
				$scope.list=response;
			}
		);
	}

	$scope.save = function(parentId){
	    //定义服务层对象
        var serviceObject;
        if($scope.entity.id!=null){
            //如果新增的类别商品没有id,则实为新增商品
            serviceObject=itemCatService.update($scope.entity);
        }else{
            //如果有新增类别商品有ID,则进行的是修改操作
            //给其parentId赋予上级ID
            $scope.entity.parentId=$scope.parentId;
            serviceObject=itemCatService.add($scope.entity);
        }
        serviceObject.success(
            function (response) {
                if (response.success){
                    //添加完毕后,重新查询对应级别的商品列表
                    $scope.findByGrand($scope.parentId);
                } else {
                    //添加失败,提示信息
                    alert(response.message);
                }

        });
    }


    $scope.grade=1;//默认为1级
    //设置级别
    $scope.setGrade=function(value){
        $scope.grade=value;
    }
    //判断商品等级
    $scope.selectList=function (p_entity) {
        if ($scope.grade == 1) {
            $scope.entity_1 = null;
            $scope.entity_2 = null;
        }
        if ($scope.grade == 2) {
            $scope.entity_1 = p_entity;
            $scope.entity_2 = null;
        }
        if ($scope.grade == 3) {
            $scope.entity_2 = p_entity;
        }
        //进行下级查询
        $scope.findByGrand(p_entity.id);
    }
});	
