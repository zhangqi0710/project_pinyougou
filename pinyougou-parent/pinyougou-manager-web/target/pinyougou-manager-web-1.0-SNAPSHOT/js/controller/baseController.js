app.controller("baseController",function ($scope) {
    //分页控件配置
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
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }

    //首先获取所有的选择的复选框,存储到数组中
    $scope.selectIds = [];
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {
            //复选框被选中,将id添加到数组中
            $scope.selectIds.push(id);
        } else {
            //复选框未被选中,获取未选中的复选框的索引
            var index = $scope.selectIds.indexOf(id);
            // 将id从数组中删除
            $scope.selectIds.splice(index, 1);
        }
    }
});