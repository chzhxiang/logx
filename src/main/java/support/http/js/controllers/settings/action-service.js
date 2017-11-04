/**
 * Created by czy on 2016/5/3.
 * factory for action
 */
app.factory('actions', ['$http', function ($http) {
    var path = 'api/getActions.json',
        endPath = 'api/endActions.json',
        taskId = null;

    var actions = $http.get(path,{params: {taskId: taskId,_d: new Date().getTime()}}).then(function (resp) {
        taskId = resp.data.object.id;
        return resp.data.object.list;
    },function(x){
        var data = JSON.parse('{"statusCode":"200","message":null,"_":null,"object":{"id":"1","list":[{"id":"1","count":3,"url":"\/authmgr\/rightManage\/getOrgInfoList.action"},{"id":"1","count":1,"url":"\/authmgr\/rightManage\/orgManagePage.action"},{"id":"1","count":1,"url":"\/authmgr\/user\/home.action"},{"id":"1","count":1,"url":"\/authmgr\/rightManage\/conditionsAction.action"},{"id":"1","count":1,"url":"\/authmgr\/rightManage\/userManagePage.action"}]}}');
        taskId = data.object.id;
        return data.object.list;
    });

    var factory = {};
    factory.all = function () {
        return actions;
    };
    factory.refresh = function(_scope){
        $http.get(path,{params: {taskId: taskId,_d: new Date().getTime()}}).then(function(resp){
            _scope.actions = resp.data.object.list;
        },function(x){
            var data = JSON.parse('{"statusCode":"200","message":null,"_":null,"object":{"id":"1","list":[{"id":"1","count":3,"url":"\/authmgr\/rightManage\/getOrgInfoList.action"},{"id":"1","count":1,"url":"\/authmgr\/rightManage\/orgManagePage.action"},{"id":"1","count":1,"url":"\/authmgr\/user\/home.action"},{"id":"1","count":1,"url":"\/authmgr\/rightManage\/conditionsAction.action"},{"id":"1","count":1,"url":"\/authmgr\/rightManage\/userManagePage.action"}]}}');
            _scope.actions = data.object.list;
        });
    };
    factory.start = function(){
      $http.get(path).then(function(resp){
          taskId = resp.data.object.id;
      },function(x){
          var data = JSON.parse('{"statusCode":"200","message":null,"_":null,"object":{"id":"1","list":[{"id":"1","count":3,"url":"\/authmgr\/rightManage\/getOrgInfoList.action"},{"id":"1","count":1,"url":"\/authmgr\/rightManage\/orgManagePage.action"},{"id":"1","count":1,"url":"\/authmgr\/user\/home.action"},{"id":"1","count":1,"url":"\/authmgr\/rightManage\/conditionsAction.action"},{"id":"1","count":1,"url":"\/authmgr\/rightManage\/userManagePage.action"}]}}');
          taskId = data.object.id;
      });
    };
    factory.end = function(){
        $http.get(endPath,{params: {taskId: taskId}}).then(function(resp){
        },function(x){
        });
    };
    factory.get = function (uri) {
        return actions.then(function(actions){
            for (var i = 0; i < mails.length; i++) {
                if (actions[i].id == uri) return actions[i];
            }
            return null;
        })
    };
    return factory;
}]);