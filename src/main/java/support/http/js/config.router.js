'use strict';

/**
 * Config for the router
 */
angular.module('app')
  .run(
    [          '$rootScope', '$state', '$stateParams',
      function ($rootScope,   $state,   $stateParams) {
          $rootScope.$state = $state;
          $rootScope.$stateParams = $stateParams;        
      }
    ]
  )
  .config(
    [          '$stateProvider', '$urlRouterProvider',
      function ($stateProvider,   $urlRouterProvider) {

          $urlRouterProvider
              .otherwise('/app/dashboard')
              .rule(function($injector,$location){
                  var path = $location.path(),normalized = path.toLowerCase();
              });
          $stateProvider
              .state('app', {
                  abstract: true,
                  url: '/app',
                  templateUrl: 'view/app.html'
              })
              .state('app.dashboard', {
                  url: '/dashboard',
                  templateUrl: 'view/app_dashboard.html',
                  resolve: {
                    deps: ['$ocLazyLoad','uiLoad',
                      function( $ocLazyLoad,uiLoad ){
                        return uiLoad.load(['vendor/jquery/charts/highcharts/highcharts.js','vendor/jquery/charts/sparkline/jquery.sparkline.min.js']).then(
                            function(){
                                return $ocLazyLoad.load(['js/controllers/dashboard.js']);
                            }
                        );
                    }]
                  }
              })
              .state('app.settings', {
                  url: '/settings',
                  template: '<div ui-view class="fade-in-up"></div>'
              })
              .state('app.settings.action', {
                  url: '/action',
                  templateUrl: 'view/settings_action.html',
                  resolve: {
                      deps: ['uiLoad','$ocLazyLoad',
                          function( uiLoad,$ocLazyLoad ){
                              return $ocLazyLoad.load('toaster').then(
                                  function(){
                                      return uiLoad.load( ['js/controllers/settings/action.js',
                                          'js/controllers/settings/action-service.js',
                                          'vendor/libs/moment.min.js']);
                                  }
                              );
                          }]
                  }
              })
              .state('app.ui', {
                  url: '/ui',
                  template: '<div ui-view class="fade-in-up"></div>'
              })
              .state('app.ui.buttons', {
                  url: '/buttons',
                  templateUrl: 'view/ui_buttons.html'
              })
              .state('app.ui.icons', {
                  url: '/icons',
                  templateUrl: 'view/ui_icons.html'
              })
              .state('app.ui.grid', {
                  url: '/grid',
                  templateUrl: 'view/ui_grid.html'
              })
              .state('app.ui.widgets', {
                  url: '/widgets',
                  templateUrl: 'view/ui_widgets.html'
              })          
              .state('app.ui.bootstrap', {
                  url: '/bootstrap',
                  templateUrl: 'view/ui_bootstrap.html'
              })
              .state('app.ui.sortable', {
                  url: '/sortable',
                  templateUrl: 'view/ui_sortable.html'
              })
              .state('app.ui.portlet', {
                  url: '/portlet',
                  templateUrl: 'view/ui_portlet.html'
              })
              .state('app.ui.timeline', {
                  url: '/timeline',
                  templateUrl: 'view/ui_timeline.html'
              })
              .state('app.ui.tree', {
                  url: '/tree',
                  templateUrl: 'view/ui_tree.html',
                  resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad ){
                          return $ocLazyLoad.load('angularBootstrapNavTree').then(
                              function(){
                                 return $ocLazyLoad.load('js/controllers/tree.js');
                              }
                          );
                        }
                      ]
                  }
              })
              .state('app.ui.toaster', {
                  url: '/toaster',
                  templateUrl: 'view/ui_toaster.html',
                  resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad){
                          return $ocLazyLoad.load('toaster').then(
                              function(){
                                 return $ocLazyLoad.load('js/controllers/toaster.js');
                              }
                          );
                      }]
                  }
              })
              .state('app.table', {
                  url: '/table',
                  template: '<div ui-view></div>'
              })
              .state('app.table.static', {
                  url: '/static',
                  templateUrl: 'view/table_static.html'
              })
              .state('app.table.datatable', {
                  url: '/datatable',
                  templateUrl: 'view/table_datatable.html'
              })
              .state('app.table.footable', {
                  url: '/footable',
                  templateUrl: 'view/table_footable.html'
              })
              .state('app.table.grid', {
                  url: '/grid',
                  templateUrl: 'view/table_grid.html',
                  resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad ){
                          return $ocLazyLoad.load('ngGrid').then(
                              function(){
                                  return $ocLazyLoad.load('js/controllers/grid.js');
                              }
                          );
                      }]
                  }
              })
              // pages
              .state('app.page', {
                  url: '/page',
                  template: '<div ui-view class="fade-in-down"></div>'
              })
              .state('app.page.profile', {
                  url: '/profile',
                  templateUrl: 'view/page_profile.html'
              })
              .state('app.page.post', {
                  url: '/post',
                  templateUrl: 'view/page_post.html'
              })
              .state('app.page.search', {
                  url: '/search',
                  templateUrl: 'view/page_search.html'
              })
              .state('app.page.invoice', {
                  url: '/invoice',
                  templateUrl: 'view/page_invoice.html'
              })
              .state('app.page.price', {
                  url: '/price',
                  templateUrl: 'view/page_price.html'
              })
              .state('app.docs', {
                  url: '/docs',
                  templateUrl: 'view/docs.html'
              })
              // others
              .state('lockme', {
                  url: '/lockme',
                  templateUrl: 'view/page_lockme.html'
              })
              .state('access', {
                  url: '/access',
                  template: '<div ui-view class="fade-in-right-big smooth"></div>'
              })
              .state('access.signin', {
                  url: '/signin',
                  templateUrl: 'view/page_signin.html',
                  resolve: {
                      deps: ['uiLoad',
                        function( uiLoad ){
                          return uiLoad.load( ['js/controllers/signin.js'] );
                      }]
                  }
              })
              .state('access.signup', {
                  url: '/signup',
                  templateUrl: 'view/page_signup.html',
                  resolve: {
                      deps: ['uiLoad',
                        function( uiLoad ){
                          return uiLoad.load( ['js/controllers/signup.js'] );
                      }]
                  }
              })
              .state('access.forgotpwd', {
                  url: '/forgotpwd',
                  templateUrl: 'view/page_forgotpwd.html'
              })
              .state('access.404', {
                  url: '/404',
                  templateUrl: 'view/page_404.html'
              })
              .state('apps', {
                  abstract: true,
                  url: '/apps',
                  templateUrl: 'view/layout.html'
              })
      }
    ]
  );