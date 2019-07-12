app.service('contentService',function ($http) {
    //根据categoryId查询广告
    this.findContentByCategoryId = function (categoryId) {
        return $http.get('content/findContentList.do?categoryId='+categoryId);
    }
});