//app模版  pagination是分页插件
var mymodule=angular.module("myapp",["pagination"]);

//service层
mymodule.service("uploadService",function ($http) {

    this.upload=function (agent,paymentType,remark,money) {
        //基于html5中的对象获取(追加)上传文件 通过FormData构造函数创建一个空对象
        var formData = new FormData();
        //参数一：后端接收文件的参数名称 参数二：获取文件，其中file代表<input type="file" id="file" />中的id
        formData.append("file",file.files[0]);
        //经办人 (如果没值需要初始化一个值,不然后台会爆undefined)
        formData.append("agent",(agent==undefined?"":agent));
        //收入/支出类型 (如果没值需要初始化一个值,不然后台会爆undefined)
        formData.append("paymentType",(paymentType==undefined?"":paymentType));
        //备注 (如果没值需要初始化一个值,不然后台会爆undefined)
        formData.append("remark",(remark==undefined?"":remark));
        //价钱 (如果没值需要初始化一个值,不然后台会爆undefined)
        formData.append("money",(money==undefined?"":money));
        return $http({
            method:"post",
            url : "../uploadController/uploadImages",
            data : formData,
            headers : {'Content-Type' : undefined}, //上传文件必须是这个类型，默认text/plain  作用:相当于设置enctype="multipart/form-data"
            transformRequest : angular.identity  //对整个表单进行二进制序列化
        })};
});

//controller层
// 时间格式转换成字符串 首先要引入$filter过滤器，然后调用filter的方法
mymodule.controller("accountController",function ($scope,$http,$filter,uploadService) {

    //初始化新增实体类
    $scope.entity={};

    //初始化查询对象
    $scope.findAccount={};

    //初始化修改对象
    $scope.findEntity={};

    //新增收入支出账目
    $scope.addAccount=function(){
        //调用service方法,并且代入所需参数
        uploadService.upload($scope.entity.agent,$scope.entity.paymentType,$scope.entity.remark,$scope.entity.money).success(function(response){
            if(response.success){//上传到linux成功
                $scope.entity={};//新增成功后,清空数据
                alert(response.message); //把返回来的图片地址赋值给广告图片地址
                $scope.reloadList();//重新加载分页查询
            }else {
                alert(response.message);//弹窗提示
            }}
        ).error(function (response) {//错误异常
            alert(response.message);//弹窗提示
        })};

    //解决分页插件二次触发的问题
    $scope.reload = true;

    //分页查询配置 (这个对象配置原本就是已经配置好的,不是我们写的)一进页面就查询第一页.
    $scope.paginationConf={
        currentPage:1,//当前页码
        totalItems:50,//总记录条数
        itemsPerPage:50,//每页记录数
        perPageOptions:[100,200,350,500,3000],//分页选项,下拉选择一页多少条记录
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

    //查询所有账目
    $scope.findAccountAll=function(pageNumber,pageSize){
        $http.post("../accountController/findAccountAll?pageNumber="+pageNumber+"&"+"pageSize="+pageSize).success(function (response){
            if(response.success){
                //总记录条数--设置分页配置里面的参数$scope.paginationConf
                $scope.paginationConf.totalItems=response.pageTotal;
                $scope.list=response.list;
            }}).error(function (response) {
            alert(response.message);
        })};

    // 如果数据变更就重新加载  分页查询方法(请求/响应)
    $scope.reloadList=function(){
        ////分页查询方法(请求/响应)参数1:当前页码参数2:当前页有多少条数据
        $scope.findAccountAll($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage)
    };


    //定义经办人
    $scope.agentList=["曾利健","罗康涛","叶锐"];

    //定义查看的支付类型
    $scope.paymentTypeList=["收入","支出"];

    //定义支付类型
    $scope.findPaymentTypeList=["全部","收入","支出"];

    //定义时间类型
    $scope.deteTimeList=["全部","今天","昨天","当月","上月"];

    //根据id删除账目
    $scope.deleteAccount=function (id) {
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


    //根据id查找账目
    $scope.findAccountOne=function (id) {
            $http.post("../accountController/findAccountOne?id="+id).success(function (response) {
                if(response.success){
                    console.log("根据id查找账目:"+response);
                    $scope.findEntity=response.account;//查询出来的对象赋值
                }else {
                    alert(response.message);//弹窗提示失败
                }}).error(function (response) {//如果出现错误
                alert(response.message);//弹窗提示错误原因
            })};


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
            var filename="账目单"+$scope.timeString+".xls";//文件名+当前时间.后缀名
            a.setAttribute('download', filename);//设置下载
            a.click();
            URL.revokeObjectURL(objectUrl);
        }).error(function (data, status, headers, config) {
            alert(data.message);//弹窗提示错误原因
        })};

    //html回显的图片 ==> 销毁预览的图片
    $scope.destroyImage=function () {
            console.log("执行了这个方法前:"+file.files[0]);
            var files = document.getElementById("file");
            files.outerHTML=file.outerHTML;
            console.log("执行了这个方法后:"+file.files[0]);
            //获取img标签信息
            var img = document.getElementById("imgName");
            //赋地址值
            img.src ="";
            //显示图片
            img.style.display="none";
            //获取div属性 imgNameDiv
            var div = document.getElementById("imgNameDiv");
            //显示
            div.style.display="none";
            //获取br属性 imgNameBr
            var br = document.getElementById("imgNameBr");
            //显示
            br.style.display="none";
    }

});




//html页面回显预览图片
function previewImage(file) {
    /*
    * file：file控件
    */
    var tip = "格式有误,只支持上传jpg/png/gif的格式文件!"; // 设定弹窗提示信息
    var filters = {
        "jpeg": "/9j/4",
        "gif": "R0lGOD",
        "png": "iVBORw"
    }

    if (window.FileReader) { // html5方案
        for (var i = 0, f; f = file.files[i]; i++) {
            var fr = new FileReader();
            fr.onload = function (e) {
                var src = e.target.result;
                if (!validateImg(src)) {
                    alert(tip)
                } else {
                    showPrvImg(src); //展示图片
                }
            }
            fr.readAsDataURL(f);
        }
    } else { // 降级处理
        if (!/\.jpg$|\.png$|\.gif$/i.test(file.value)) {
            alert(tip);
        } else {
            showPrvImg(file.value); //展示图片
        }
    }

    //校验图片格式
    function validateImg(data) {
        var pos = data.indexOf(",") + 1;
        for (var e in filters) {
            if (data.indexOf(filters[e]) === pos) {
                return e;
            }
        }
        return null;
    }

    //html页面回显预览图片
    function showPrvImg(src) {
            //获取img标签信息
            var img = document.getElementById("imgName");
            //赋地址值
            img.src = src;
            //显示图片
            img.style.display = "block";
            //获取div属性 imgNameDiv
            var div = document.getElementById("imgNameDiv");
            //显示
            div.style.display="block";
            //获取br属性 imgNameBr
            var br = document.getElementById("imgNameBr");
            //显示
            br.style.display="block";
    }
}