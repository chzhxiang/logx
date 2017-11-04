'use strict';

/* Controllers */

angular.module('app')
  .controller('AppCtrl', ['$scope', '$translate', '$localStorage', '$window', 
    function(              $scope,   $translate,   $localStorage,   $window ) {
      // add 'ie' classes to html
      var isIE = !!navigator.userAgent.match(/MSIE/i);
      isIE && angular.element($window.document.body).addClass('ie');
      isSmartDevice( $window ) && angular.element($window.document.body).addClass('smart');

      // config
      $scope.app = {
        name: '日志管理系统',
        version: '1.0.0',
        // for chart colors
        color: {
          primary: '#7266ba',
          info:    '#23b7e5',
          success: '#27c24c',
          warning: '#fad733',
          danger:  '#f05050',
          light:   '#e8eff0',
          dark:    '#3a3f51',
          black:   '#1c2b36'
        },
        settings: {
          themeID: 1,
          navbarHeaderColor: 'bg-black',
          navbarCollapseColor: 'bg-white-only',
          asideColor: 'bg-black',
          headerFixed: true,
          asideFixed: false,
          asideFolded: false,
          asideDock: false,
          container: false
        },
        util: {
            datatables: {
                oLanguage: {
                    sSearch:'搜索',
                    sLengthMenu:'每页显示_MENU_条记录',
                    sInfo:'显示_START_至 _END_条&nbsp;&nbsp;共_TOTAL_条',
                    sInfoFiltered:'(筛选自_MAX_条数据)',
                    sInfoEmtpy:'没有数据',
                    sEmptyTable:'数据为空',
                    sZeroRecords:'筛选数据结果为空',
                    sInfoEmpty:'显示第0到0条记录，共0条',
                    sProcessing:'处理中...',
                    sLoadingRecords:'数据加载中...',
                    oPaginate:{
                        sFirst:'首页',
                        sPrevious:'前一页',
                        sNext:'后一页',
                        sLast:'末页'
                    },
                    oAria:{
                        sSortAscending:'以升序排列此列',
                        sSortDescending:'以降序排列此列'
                    }
                }
            }
        }
      }

      // save settings to local storage
      if ( angular.isDefined($localStorage.settings) ) {
        $scope.app.settings = $localStorage.settings;
      } else {
        $localStorage.settings = $scope.app.settings;
      }
      $scope.$watch('app.settings', function(){
        if( $scope.app.settings.asideDock  &&  $scope.app.settings.asideFixed ){
          // aside dock and fixed must set the header fixed.
          $scope.app.settings.headerFixed = true;
        }
        // save to local storage
        $localStorage.settings = $scope.app.settings;
      }, true);

      // angular translate
      $scope.lang = { isopen: false };
      $scope.langs = {en:'English', zh:'中文'};
      $scope.selectLang = $scope.langs[$translate.proposedLanguage()] || "English";
      $scope.selectLangKey = $translate.proposedLanguage() || "en";
      $scope.setLang = function(langKey, $event) {
        // set the current lang
        $scope.selectLang = $scope.langs[langKey];
        $scope.selectLangKey = langKey;
        // You can change the language during runtime
        $translate.use(langKey);
        $scope.lang.isopen = !$scope.lang.isopen;
      };

      function isSmartDevice( $window )
      {
          // Adapted from http://www.detectmobilebrowsers.com
          var ua = $window['navigator']['userAgent'] || $window['navigator']['vendor'] || $window['opera'];
          // Checks for iOs, Android, Blackberry, Opera Mini, and Windows mobile devices
          return (/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/).test(ua);
      }

  }]);