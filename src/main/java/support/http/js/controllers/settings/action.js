/**
 * Created by czy on 2016/5/3.
 */
app.controller('ActionCtrl', ['$scope','toaster', function($scope,toaster) {
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $(document).on('dblclick','.td-edit',function(e){
        var $this = $(this),text = $this.text(),
            $input = $('<input type="text" />');
        $this.data('text',text);
        $input.val(text).blur(function(){
            var val = $(this).val(),$parent = $(this).parent(),preVal = $parent.data('text'),$a = $parent.parent().find('td:last>a[modify]');

//            $(this).parent().data('text',val).text(val).parent().find('td:last>a').removeClass('active').css('cursor','pointer');
            $parent.data('text',val).text(val);
            if(val != preVal){
                $a.removeClass('active').css('cursor','pointer');
            }
        });
        $this.text('').append($input);
        $input.focus();
    });
    $scope.modify = function(url,$event){
        if($($event.target).parents('a').hasClass('active')) return;
        var arr = $($event.target).parents('tr').find('td.td-edit'),
            params = {},
            $operA = $('tr[data-url="'+url+'"] td:last>a[modify]');
        $.each(arr,function(i,obj){
            var $obj = $(obj);
            params[$obj.attr('data-name')] = $obj.text();
        });
        if(!$.isEmptyObject(params)){
            params['url'] = url;
            $.post('api/modifyActionInfo.json',params).then(function () {
                $operA.addClass('active').css('cursor','default');
                toaster.pop('info', '提示', '修改成功');
            },function(){
                //error
                toaster.pop('error', '提示', '修改失败');
            });
        };
    };
}]);
/**
 * 请求列表
 */
app.controller('ActionListCtrl', ['$scope', 'actions', '$stateParams', function($scope, actions, $stateParams) {
    $scope.fold = $stateParams.fold;
    $scope.allCheck = '';
    actions.all().then(function(actions){
        $scope.actions = actions;
    });
    $scope.refresh = function(){
        actions.refresh($scope);
    };
    $scope.checkAll = function(e){
        $scope.allCheck = $scope.allCheck==''?'check':'';
    }
    $scope.pageSearch = function(){
        var val = $scope.requestActionSearch;
        if(val){
            $('#actionList tr:contains('+val+')').siblings().hide();
            $('#actionList tr:contains('+val+')').show();
        }
        else
            $('#actionList tr').show();
    }

}]);
app.controller('ModalInstanceCtrl', ['$scope', '$modalInstance', 'modal', function($scope, $modalInstance, modal) {
    $scope.modal = modal;
    $scope.ok = function () {
        $modalInstance.close();
    };
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
}])
/**
 * 配置文件读写展示
 */
app.controller('ActionConfCtrl', ['$scope', '$http','$modal', function($scope, $http, $modal) {
    $http.get('api/read/urlmapping.json').then(function(response){
        if ( response.data.statusCode == '200' ) {
            var data = response.data.object,content = data['content']||'' ,pro = data['pro'],proArr = [];

            pro = pro || [];
            $.each(pro,function(k,v){
                proArr.push({url: k,text: v});
            });
            $scope.proList = proArr;
            $scope.confContent = content;
        }
    },function(x){
        var obj = JSON.parse('{"statusCode":"200","message":null,"_":null,"object":{"content":"#Save [\/reportMgr\/]-[\\u9996\\u9875]\r\n#Fri May 06 14:40:15 CST 2016\r\n\/reportMgr\/=\\u9996\\u9875\r\n","pro":{"\/reportMgr\/":"首页"}}}'.replace(/\r\n/g,'<br/>'));
        var data = obj.object,content = data['content'] ,pro = data['pro'],proArr = [];

        pro = pro || [];
        $.each(pro,function(k,v){
            proArr.push({url: k,text: v});
        });
        $scope.proList = proArr;
        $scope.confContent = content;
    });

    $scope.openConfContent = function(){
        var modalInstance = $modal.open({
            templateUrl: 'modal.html',
            controller: 'ModalInstanceCtrl',
            resolve: {
                modal: function () {
                    return {
                        title: '配置内容',
                        content: $scope.confContent
                    };
                }
            }
        });
    }
}]);