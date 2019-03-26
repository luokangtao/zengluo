//app模版  pagination是分页插件
var mymodule=angular.module("myapp",["pagination"]);

//service层
mymodule.service("uploadService",function ($http) {
    //MultipartFile文件导入
    this.upload=function () {
        //基于html5中的对象获取(追加)上传文件
        var formData = new FormData();
        //参数一：后端接收文件的参数名称 参数二：获取文件，其中file代表<input type="file" id="file" />中的id
        formData.append("file",file.files[0]);

        return $http({
            method:"post",
            url : "../ExportController/importExcel",//上传地址
            data : formData,
            headers : {'Content-Type' : undefined}, //上传文件必须是这个类型，默认text/plain  作用:相当于设置enctype="multipart/form-data"
            transformRequest : angular.identity  //对整个表单进行二进制序列化
        })}
});

//controller层
// 时间格式转换成字符串 首先要引入$filter过滤器，然后调用filter的方法
mymodule.controller("accountController",function ($scope,$http,$filter,uploadService) {

    //新增实体类
    $scope.entity={};

    //新增收入支出账目
    $scope.addFriend=function () {
        $http.post("../accountController/addAccount",$scope.entity).success(function (response) {
            if(response.success){//如果为true
                $scope.entity={};//上传成功后清空数据
                alert(response.message);//弹窗提示成功
            }else {
                alert(response.message);//弹窗提示失败
            }}).error(function (response) {//如果出现错误
            alert(response.message);//弹窗提示错误原因
        })};

    //解决分页插件二次触发的问题
    $scope.reload = true;

    //分页查询配置 (这个对象配置原本就是已经配置好的,不是我们写的)一进页面就查询第一页.
    $scope.paginationConf={
        currentPage:1,//当前页码
        totalItems:10,//总记录条数
        itemsPerPage:10,//每页记录数
        perPageOptions:[10,20,30,40,50],//分页选项,下拉选择一页多少条记录
        onChange:function(){//更改页面时触发事件
            if(!$scope.reload) {
                return;
            }
            $scope.reloadList();//数据变更就重新加载分页查询
            $scope.reload = false;
            setTimeout(function() {
                $scope.reload = true;
            }, 200);
        }};

    //查询所有通讯录
    $scope.findAllFriend=function(pageNumber,pageSize){
        $http.post("../accountController/findAccountAll?pageNumber="+pageNumber+"&"+"pageSize="+pageSize).success(function (response){
            if(response.success){
                //总记录条数--设置分页配置里面的参数$scope.paginationConf
                $scope.paginationConf.totalItems=response.pageResult.total;
                $scope.list=response.pageResult.rows;
            }}).error(function (response) {
            alert(response.message);
        })};

    // 如果数据变更就重新加载  分页查询方法(请求/响应)
    $scope.reloadList=function(){
        ////分页查询方法(请求/响应)参数1:当前页码参数2:当前页有多少条数据
        $scope.findAllFriend($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage)
    };


    //定义经办人
    $scope.agentList=["曾利健","罗康涛"];

    //定义支付类型
    $scope.paymentTypeList=["收入","支出"];

    //根据id删除账目
    $scope.deleteFriend=function (id) {
        if(confirm("你确认删除?")){
            $http.post("../accountController/deleteAccount?id="+id).success(function (response) {
                if(response.success){
                    alert(response.message);
                    $scope.reloadList();//重新查询通信录
                }else {
                    alert(response.message);//弹窗提示失败
                }}).error(function (response) {//如果出现错误
                alert(response.message);//弹窗提示错误原因
            })}};


    //导出excel数据
    $scope.exportExcel=function () {
        $http({
            url: '../ExportController/exportExcel',
            method: "GET",//接口方法
            params: {
                //接口参数
            },
            headers: {
                'Content-type': 'application/json'
            },
            responseType: 'arraybuffer'
        }).success(function (data, status, headers, config) {
            var blob = new Blob([data], {type: "application/vnd.ms-excel"});
            var objectUrl = URL.createObjectURL(blob);
            var a = document.createElement('a');
            document.body.appendChild(a);
            a.setAttribute('style', 'display:none');
            a.setAttribute('href', objectUrl);
            $scope.today = new  Date();//获取当前时间
            $scope.timeString = $filter('date')($scope.today, 'yyyy-MM-dd HH:mm:ss');//当前时间格式转成字符串
            var filename="同学录"+$scope.timeString+".xls";//文件名+当前时间.后缀名
            a.setAttribute('download', filename);//设置下载
            a.click();
            URL.revokeObjectURL(objectUrl);
        }).error(function (data, status, headers, config) {
            alert(data.message);//弹窗提示错误原因
        })};


});