app.service('uploadService',function ($http) {
    //上传文件
    this.uploadFile = function () {
        //HTML5新增的类,用于文件提交,意识就是一个文件表单,为文件二进制的封装
        var formData = new FormData();
        //'file'指的是name的属性值
        //file为文件上传的name,file.files[0]意思为取得第一个文件上传框,指的是id为file的标签
        formData.append('file',file.files[0]);
        return $http({
            url:'../upload.do',
            method:'Post',
            data:formData,
            //当你上传的是文件,必须制定上传格式为此格式,默认是Json格式
            headers:{'Content-Type':undefined},
            transformRequest:angular.identity

        });
    }
});