// config

var app =  
angular.module('app')
  .config(
    [        '$controllerProvider', '$compileProvider', '$filterProvider', '$provide',
    function ($controllerProvider,   $compileProvider,   $filterProvider,   $provide) {
        
        // lazy controller, directive and service
        app.controller = $controllerProvider.register;
        app.directive  = $compileProvider.directive;
        app.filter     = $filterProvider.register;
        app.factory    = $provide.factory;
        app.service    = $provide.service;
        app.constant   = $provide.constant;
        app.value      = $provide.value;
    }
  ])
  .config(['$translateProvider', function($translateProvider){
    // Register a loader for the static files
    // So, the module will search missing translation tables under the specified urls.
    // Those urls are [prefix][langKey][suffix].
    $translateProvider.useStaticFilesLoader({
      prefix: 'l10n/',
      suffix: '.js'
    });
    // Tell the module what language to use by default
    $translateProvider.preferredLanguage('en');
    // Tell the module to store the language in the local storage
    $translateProvider.useLocalStorage();
  }])
  .config(['$httpProvider',function($httpProvider){
        $httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded';
        $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

        $httpProvider.defaults.transformRequest = [function(data){
            /**
             * 转换序列
             * @type {string}
             */
            var param = function(obj){
                var query = '';
                var name,value,fullSubName,subName,subValue,innerObj,i;

                for(name in obj){
                    value = obj[name];

                    if(value instanceof Array){
                        for(i = 0;i<value.length;i++){
                            subValue = value[i];
                            fullSubName = name + '[' + i + ']';
                            innerObj = {};
                            innerObj[fullSubName] = subValue;
                            query += param(innerObj) + '&';
                        }
                    }else if(value instanceof Object){
                        for(subName in value){
                            subValue = value[subName];
                            fullSubName = name + '[' + subName + ']';
                            innerObj = {};
                            innerObj[fullSubName] = subValue;
                            query += param[innerObj] + '&';
                        }
                    }else if(value != undefined && value != null){
                        query += encodeURIComponent(name) + '=' +encodeURIComponent(value) + '&';
                    }
                }
                return query.length?query.substr(0,query.length - 1):query;
            };
            return angular.isObject(data) && String(data) !== '[object File]' ? param(data) : data;
        }];

    }]);