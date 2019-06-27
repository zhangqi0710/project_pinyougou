//创建服务层,主要负责和后台进行数据交互
app.service("brandService",function ($http) {
    this.findAll = function () {
        return $http.get('../brand/findAll.do');
    }
    this.findPage = function (page, size) {
        return $http.get('../brand/findPage.do?page=' + page + '&size=' + size);
    }
    this.findOne = function (id) {
        return $http.get('/brand/findOne.do?id=' + id);
    }
    this.add = function (entity) {
        return $http.post('../brand/add.do', entity);
    }
    this.update = function (entity) {
        return $http.post('../brand/update.do', entity);
    }
    this.remove = function (selectIds) {
        return $http.post("../brand/delete.do",selectIds);
    }
    this.search = function (page,size,ExampleEntity) {
        return $http.post('../brand/findByExample.do?page='+page+'&size='+size,ExampleEntity);
    }
    //查询所有的品牌类表
    this.selectOptionList=function(){
        return $http.get('../brand/selectOptionList.do');
    }
})
