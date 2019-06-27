/*根据模块创建控制器*/
app.controller('brandController', function ($scope,brandService,$controller) {

    //继承baseController
    $controller("baseController",{$scope:$scope});

    //通过内置对象$scope定义一个方法
    $scope.findAll = function () {
        //发起ajax请求
        brandService.findAll().success(
            //使用response来接受后台返回的数据
            function (response) {
                //将就收的数据封装到list模型变量中
                $scope.list = response;
            });
    }

   /* //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            //重新加载
            $scope.reloadList();
        }
    };

    //重新加载列表 数据
    $scope.reloadList = function () {
        //切换页码
        $scope.findByExample($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }*/

    $scope.findPage = function (page, size) {
        brandService.findPage(page,size).success(
            function (response) {
                //获取当前页所有数据
                $scope.list = response.list;
                //获取总记录数
                $scope.paginationConf.totalItems = response.total;
            }
        );
    }

    /*//添加品牌
    $scope.add = function () {
        $http.post('../brand/add.do',$scope.entity).success(
                function (response) {
                    if (response.success){
                        //添加成功，异步刷新页面
                        $scope.reloadList();
                    }else {
                        //添加失败，弹出失败提示框
                        alert(response.message);
                    }
                }
        );
    }*/

    //根据id查询品牌
    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }


    //保存修改的数据
    $scope.save = function () {
        var object = null;
        if ($scope.entity.id != null) {
            object = brandService.update( $scope.entity);
        }else {
            object = brandService.add( $scope.entity);
        }
        object.success(
            function (response) {
                if (response.success) {
                    //修改成功,异步刷新页面
                    $scope.reloadList();
                } else {
                    //修改失败
                    alert(response.message);
                }
            });
    }


    //首先获取所有的选择的复选框,存储到数组中
    /*$scope.selectIds = [];
    $scope.selectChecked = function ($event, id) {
        if ($event.target.checked) {
            //复选框被选中,将id添加到数组中
            $scope.selectIds.push(id);
        } else {
            //复选框未被选中,获取未选中的复选框的索引
            var index = $scope.selectIds.indexOf(id);
            // 将id从数组中删除
            $scope.selectIds.splice(index, 1);
        }
    }*/
    //根据id删除品牌
    $scope.remove = function () {
        if (confirm("您确定要删除吗?")) {
            brandService.remove($scope.selectIds).success(
                function (response) {
                    if (response.success) {
                        //删除成功,异步刷新列表
                        $scope.reloadList();
                    } else {
                        //删除失败,给出提示信息
                        alert(response.message);
                    }
                }
            );
        }
    }

    //初始化ExampleEntity
    $scope.ExampleEntity = {};
    //条件模糊查询
    $scope.search = function (page,size) {
        brandService.search(page,size,$scope.ExampleEntity).success(
            function (response) {
                //获取当前页所有数据
                $scope.list = response.list;
                //获取总记录数
                $scope.paginationConf.totalItems = response.total;
            }
        );
    }

});