app.controller('searchController',function ($scope, searchService) {
    $scope.search = function () {
        //处理类型装换(当前页字符串数据转换为Interger类型)
        var pageNo = parseInt($scope.searchMap.pageNo);
        $scope.searchMap.pageNo = pageNo;
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;

                $scope.buildPageLabel();
            }
        );
    }

    //初始化搜索对象
    $scope.searchMap={'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':40,
                        'sortField':'','sort':''};
    //添加复合搜索项
    $scope.addSearchItem=function(key,value){
        //如果点击的是分类或者是品牌
        if(key=='category' || key=='brand' || key == 'price'){
            $scope.searchMap[key]=value;
        }else{
            $scope.searchMap.spec[key]=value;
        }
        //再添加完条件之后,再执行搜索
        $scope.search();
    }

    //移除复合搜索条件
    $scope.removeSearchItem=function(key){
        //如果是分类或品牌
        if(key=="category" || key=="brand" || key == 'price'){
            $scope.searchMap[key]="";
        }else{
            //如果是规格,移除此属性
            delete $scope.searchMap.spec[key];
        }
        //在删除查询条件之后,执行查询操作
        $scope.search();
    }


    //构建分页标签
    $scope.buildPageLabel = function () {
        //获取总页数
        var end = $scope.resultMap.totalPages;
        //开始页码
        var begin = 1;
        //判断'...'
        $scope.point1 = true;
        $scope.point2 = true;
        //如果总页数大于5,考虑分页
        if($scope.resultMap.totalPages > 5){
            //如果当前页小于3,设置末页为5
            if ($scope.searchMap.pageNo <= 3){
                end = 5;
                $scope.point1 = false;
            }else if ($scope.searchMap.pageNo +2 >= $scope.resultMap.totalPages){
                //如果当前页大于总页数-2,那么设置起始页为总页数-4
                begin = $scope.resultMap.totalPages - 4;
                $scope.point2 = false;
            }else {
                //总页数小于5,不考虑分页
                begin = $scope.searchMap.pageNo - 2;
                end = $scope.searchMap.pageNo + 2;
            }
        }else {
            $scope.point1 = false;
            $scope.point2 = false;
        }
        //新增分页栏属性
        $scope.pageLabel=[];
        for (var i = begin; i <= end; i++) {
            $scope.pageLabel.push(i);
        }
    }

    //提交页码查询
    $scope.queryByPage = function (pageNo) {
        //如果查询的当前页码小于1或者大于总页数-2,则直接返回,不用查询
        if(pageNo<1 || pageNo > $scope.resultMap.totalPages){
            return;
        }
        //重新设置searchMap的pageNo属性值
        $scope.searchMap.pageNo = pageNo;
        //重新查询
        $scope.search();
    }


    //上一页/下一页样式设置
    $scope.pageNoIsTop = function () {
        if ($scope.searchMap.pageNo == 1){
            return true;
        }else {
            return false;
        }
    }
    $scope.pageNoIsDeep = function () {
        if ($scope.searchMap.pageNo == $scope.resultMap.totalPages){
            return true;
        }else {
            return false;
        }
    }


    //排序
    $scope.queryByPrice = function (sortField,sort) {
        $scope.searchMap.sortField = sortField;
        $scope.searchMap.sort = sort;
        $scope.search();
    }


    //当输入的关键字包含商品品牌的时候,直接隐藏品牌框
    $scope.hiddenBrand = function () {
        var brandList = $scope.resultMap.brandList;
        for (var i = 0; i < brandList.length; i++) {
            if ($scope.searchMap.keywords.indexOf(brandList[i].text)>=0){
                return true;
            }
        }
        return false;
    }

});