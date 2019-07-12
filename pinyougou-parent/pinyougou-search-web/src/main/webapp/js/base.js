/*创建模块*/
var app = angular.module('pinyougou', []);


//定义过滤器,'$sce'为引入的服务
app.filter('trustHtml',['$sce',function ($sce) {
    return function (data) {//data表示被过滤的内容
        return $sce.trustAsHtml(data);  //返回过滤后的内容(信任HTML的转换)
    }
}]);