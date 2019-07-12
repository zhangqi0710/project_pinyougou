app.controller('contentController',function ($scope,contentService) {
    //初始化广告几何数组
    $scope.contentList = [];
    //根据categoryId查询广告
    $scope.findContentByCategoryId = function (categoryId) {
        contentService.findContentByCategoryId(categoryId).success(
            function (response) {
                alert(response);
                $scope.contentList[categoryId]=response;
            }
        );
    }
});

