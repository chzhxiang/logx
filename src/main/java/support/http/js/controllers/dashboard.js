'use strict';

/* Controllers */

app
  .controller('DashboardChartCtrl', ['$scope','$http', function($scope,$http) {
    $scope.d = [ [1,6.5],[2,6.5],[3,7],[4,8],[5,7.5],[6,7],[7,6.8],[8,7],[9,7.2],[10,7],[11,6.8],[12,7] ];

    $scope.d1_1 = [ [10, 120], [20, 70], [30, 70], [40, 60] ];

    $scope.d1_2 = [ [10, 50],  [20, 60], [30, 90],  [40, 35] ];

    $scope.d1_3 = [ [10, 80],  [20, 40], [30, 30],  [40, 20] ];

    $scope.d2 = [];

    for (var i = 0; i < 20; ++i) {
      $scope.d2.push([i, Math.sin(i)]);
    }

    $scope.d3 = [
      { label: "iPhone5S", data: 40 },
      { label: "iPad Mini", data: 10 },
      { label: "iPad Mini Retina", data: 20 },
      { label: "iPhone4S", data: 12 },
      { label: "iPad Air", data: 18 }
    ];

    $scope.getRandomData = function() {
      var data = [],
      totalPoints = 150;
      if (data.length > 0)
        data = data.slice(1);
      while (data.length < totalPoints) {
        var prev = data.length > 0 ? data[data.length - 1] : 50,
          y = prev + Math.random() * 10 - 5;
        if (y < 0) {
          y = 0;
        } else if (y > 100) {
          y = 100;
        }
        data.push(y);
      }
      // Zip the generated y values with the x values
      var res = [];
      for (var i = 0; i < data.length; ++i) {
        res.push([i, data[i]])
      }
      return res;
    }

    $scope.d4 = $scope.getRandomData();

    $scope.today = function() {
        $scope.logDate = moment().format('YYYY-MM-DD');
    };
    $scope.today();
    $scope.now = moment().format('YYYY-MM-DD');
    $scope.clear = function () {
        $scope.logDate = null;
    };

    // Disable weekend selection
    $scope.disabled = function(date, mode) {
        return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
    };

    $scope.toggleMin = function() {
        $scope.minDate = $scope.minDate ? null : new Date();
    };
    $scope.toggleMin();

    $scope.open = function($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.opened = true;
    };

    $scope.dateOptions = {
        formatYear: 'yyyy',
        startingDay: 1,
        class: 'datepicker'
    };

    $scope.initDate = new Date();
    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate','yyyy-MM-dd'];
    $scope.format = $scope.formats[4];

    $scope.queryLogdate = function(){
        $scope.loadBrowsers();
    }
    /********************************************** 浏览器 ***************************************************/
    $scope.browserData = [
    ];

    $scope.versionsData = [
    ];
    $scope.browserChange = true;
    $scope.browserOpts = {
        chart: {
            type: 'pie'
        },
        title: {
            text: ''
        },
        plotOptions:{
            pie: {
                cursor: 'pointer',
                allowPointSelect: true,
                shadow: false,
                center: ['50%', '50%'],
                point: {
                    events: {
                        click: function(e) {
                            var searchVal = e.point.name,dt = $('#browserTable').DataTable();
                            if(e.point.state == 'select') searchVal = '';
                            $('#browserTable_wrapper [type=search]').val(searchVal);
                            dt.search(searchVal);
                            dt.draw();
                        }
                    }
                }
            }
        },
        tooltip: {
            valueSuffix: ''
        },
        series: [{
            name: '浏览器',
            data: $scope.browserData,
            size: '50%',
            dataLabels: {
                formatter: function() {
                    return this.y>0?this.point.name:null;
                },
                color: 'white',
                distance: -30
            }
        }, {
            name: '版本',
            data:  $scope.versionsData,
            size: '90%',
            innerSize: '68%',
            dataLabels: {
                formatter: function() {
                    return this.y > 0 ? '<b>'+ this.point.name +'：</b> '+ this.y +'人'  : null;
                }
            }
        }]
    };
    $scope.platformOpts = {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {text: ''},
        plotOptions:{
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                shadow: false,
                center: ['50%', '50%'],
                point: {
                    events: {
                        click: function(e) {
                            var searchVal = e.point.name,dt = $('#browserTable').DataTable();
                            if(e.point.state == 'select') searchVal = '';
                            $('#browserTable_wrapper [type=search]').val(searchVal);
                            dt.search(searchVal);
                            dt.draw();
                        }
                    }
                }
            }
        },
        series: [{
            type: 'pie',
            name: 'platform',
            data: []
        }
        ]
    };
    /**
     * 查询缓存的session点
     */
    var sessionChart,sessionChartRQERRCount=0,nodeNum = 20,periodTime = 5000,sessionChartInterval;
    var initSessionChart = function(loginData,allData){
        sessionChart = $('#sessionChart').highcharts({
            chart: {
                type: 'spline',
                marginRight: 10,
                events: {
                    load: function(){
                        var chart = this;
                        sessionChartInterval = setInterval(function(){
                            if(sessionChartRQERRCount > 10||$('#sessionChart').length == 0)
                                clearInterval(sessionChartInterval);
                            pushSessionData(chart);
                        },5000);
                    }
                }
            },
            title: {
                text: ''
            },
            xAxis: {
                type: 'datetime',
                tickPixelInterval: 150
            },
            yAxis: {
                title: {
                    text: '人数'
                },
                plotLines :[{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
            	//共享多条折线数据
            	shared : true,
                formatter: function(){
                    return '<b>时间</b><br/>'+
                        moment(this.x).format('YYYY-MM-DD HH:mm:ss')+'<br/>'+
                        '<b>所有连接个数</b><br/>'+
                        '<b>'+this.points[1].y+'</b>个<br/>'+
                        '<b>已登陆人数</b><br/>'+
                        '<b>'+this.points[0].y+'</b>个';
                }
            },
            legend: {
                enabled: true
            },
            exporting: {
                enabled: true
            },
            series: [{
                name: '已登陆人数统计',
                color: '#3A97DC',
                data: loginData
            },{
                name: '所有连接数统计',
                color: '#8BC5F0',
                data: allData
            }]
        });
    };
    var getCachSessionsInfo = function(){
    	$http.post('api/getCachSessionsInfo.json', {num: nodeNum, period: 5000})
            .then(function(response) {
                if ( response.data.statusCode == '200' ) {
            		var arr = response.data.object,loginData = [],allData = [];
                    if(arr.length<nodeNum){
                        var lastTime = arr[arr.length-1].time;
                        for(var i=1;i<=nodeNum-arr.length;i++){
                            loginData.push({x: lastTime - periodTime*i,y: -1});
                            allData.push({x: lastTime - periodTime*i,y: -1});
                        }
                    }
                    for(var i=arr.length - 1;i>-1;i--){
                        var obj = arr[i];
                        loginData.push({x: obj.time,y: obj['loginNum']});
                        allData.push({x: obj.time,y: obj['onLineNum']});
                    }
                    initSessionChart(loginData,allData);
                }
            }, function(x) {
                var data = JSON.parse('{"statusCode":"200","message":"","_":0,"object":[{"time":1461830980518,"loginNum":5,"onLineNum":49},{"time":1461830975518,"loginNum":5,"onLineNum":49},{"time":1461830970518,"loginNum":5,"onLineNum":48},{"time":1461830965518,"loginNum":5,"onLineNum":48},{"time":1461830960518,"loginNum":5,"onLineNum":43},{"time":1461830955518,"loginNum":5,"onLineNum":41},{"time":1461830950518,"loginNum":5,"onLineNum":45},{"time":1461830945518,"loginNum":5,"onLineNum":45},{"time":1461830940518,"loginNum":5,"onLineNum":45},{"time":1461830935518,"loginNum":5,"onLineNum":45},{"time":1461830930518,"loginNum":5,"onLineNum":45},{"time":1461830925518,"loginNum":5,"onLineNum":45},{"time":1461830920518,"loginNum":5,"onLineNum":45},{"time":1461830915518,"loginNum":5,"onLineNum":45},{"time":1461830910518,"loginNum":5,"onLineNum":45},{"time":1461830905518,"loginNum":5,"onLineNum":45},{"time":1461830900518,"loginNum":5,"onLineNum":45},{"time":1461830895518,"loginNum":5,"onLineNum":45},{"time":1461830890518,"loginNum":4,"onLineNum":46},{"time":1461830885518,"loginNum":4,"onLineNum":46}]}');
                var arr = data.object,loginData = [],allData = [];;
                if(arr.length<nodeNum){
                    var lastTime = arr[arr.length-1].time;
                    for(var i=1;i<=nodeNum-arr.length;i++){
                        loginData.push({x: lastTime - periodTime*i,y: -1});
                        allData.push({x: lastTime - periodTime*i,y: -1});
                    }
                }

                for(var i=arr.length - 1;i>-1;i--){
                    var obj = arr[i];
                    loginData.push({x: obj.time,y: obj['loginNum']});
                    allData.push({x: obj.time,y: obj['onLineNum']});
                }
                initSessionChart(loginData,allData);
        	});
    }
    getCachSessionsInfo();
    /**
     * 插入session数据
     */
    var initTime = 1461830980518;
    var pushSessionData = function(chart){
    	$http.post('api/getCachSessionsInfo.json', {num: 1, period: 5000})
        .then(function(response) {
            var data = response.data;
            if ( data && data.statusCode == '200' && data.object.length == 1) {
                var obj = data.object[0],x = obj.time,y1 = obj['loginNum'],y2 = obj['onLineNum'];
                chart.series[0].addPoint([x,y1],true,true);
                chart.series[1].addPoint([x,y2],true,true);
            }
        }, function(x) {
            initTime = initTime+5000;
            sessionChartRQERRCount++;
            var data = JSON.parse('{"statusCode":"200","message":null,"_":null,"object":[{"time":'+ (initTime) +',"loginNum":12,"onLineNum":52}]}');
            var obj = data.object[0],x = obj.time,y1 = obj['loginNum'],y2 = obj['onLineNum'];
            chart.series[0].addPoint([x,y1],true,true);
            chart.series[1].addPoint([x,y2],true,true);
    	});

    };

    /**
     * 查找登录地址
     */
    var loadLoginURL = function(){
        $http.post('api/getLoginURL.json', {})
            .then(function(response) {
                if ( response.data.statusCode == '200' ) {
                    $scope.loginURL = response.data.object || '';
                }
            }, function(x) {
        	});
    };
    loadLoginURL();
    /**
     * 加载日志内容
     */
    $scope.loadLoglist = function(){
        $('#browserTable').DataTable().ajax.url("api/queryLog.json?logVersion=eq_1&requestURI="+($scope.loginURL||'')+"&startTime="+ moment($scope.logDate).format('YYYY-MM-DD')+"&endTime="+ moment($scope.logDate).format('YYYY-MM-DD'))
        $('#browserTable').DataTable().ajax.reload();
    };
    /**
     * 处理浏览器结果
     * @param resultObj
     */
    var handleBrowserResult = function(resultObj){
        var  browserData = [],versionsData = [];
        if (resultObj) {
            var arr = resultObj,browserObj = {},colors = ['#5D9CEC','#62C87F','#F15755','#6D51D2'],platformObj = {},platformData = [];
            if(arr && arr.length>0){
                for(var i=0;i<arr.length;i++){
                    /**
                     * 浏览器
                     */
                    var obj = arr[i],browserVer = obj['explorerVer'],platform = obj['platform'];
                    if(browserVer == null) continue;
                    var browser = browserVer.split('/')[0];
                    if(browserObj[browser] == undefined){
                        browserObj[browser] = {y: 1,ver: {},verLen: 1};
                        browserObj[browser].ver[browserVer] = 1;
                    }else{
                        browserObj[browser].y =   browserObj[browser].y + 1;
                        if( browserObj[browser].ver[browserVer] == undefined){
                            browserObj[browser].ver[browserVer] = 1;
                            browserObj[browser].verLen = browserObj[browser].verLen + 1;
                        }else{
                            browserObj[browser].ver[browserVer] = browserObj[browser].ver[browserVer] + 1;
                        }
                    }
                    /**
                     * 操作系统
                     * [
                     ['IE', 33.0],
                     ['Firefox', 13.0],
                     ['Chrome', 53.0]
                     ]
                     */
                    if(platformObj[platform] == undefined){
                        platformObj[platform] = 1;
                    }else{
                        platformObj[platform] = platformObj[platform] + 1;
                    }
                }
                /**
                 * 浏览器
                 * @type {number}
                 */
                var i = 0;
                for(var browserName in browserObj){
                    var y = browserObj[browserName].y, versions = browserObj[browserName].ver,verLen = browserObj[browserName].verLen,color = colors[i++%3],j = 0;
                    browserData.push({name: browserName,y: y,color: color});
                    for(var ver in versions){
                        var brightness = 0.2 - (j++ / verLen) / 5;
                        versionsData.push({name: ver,y: versions[ver],color: Highcharts.Color(color).brighten(brightness).get()});
                    }
                }
                /**
                 * 操作系统
                 */
                for(var platform in platformObj){
                    platformData.push([platform, platformObj[platform]]);
                }
            }
        }
        $scope.browserOpts.series[0].data=browserData;
        $scope.browserOpts.series[0].data=browserData;
        $scope.browserOpts.series[1].data= versionsData;
        $scope.platformOpts.series[0].data= platformData;
        $('#browserChart').highcharts( $scope.browserOpts);
        $('#platformChart').highcharts( $scope.platformOpts);
    }
    /**
     * 获取浏览器信息
     */
    $scope.loadBrowsers = function() {
        $scope.loadLoglist();
        $scope.platformData = [];
        // request
        $http.post('api/queryLog.json', {startTime: moment($scope.logDate).format('YYYY-MM-DD'),endTime: moment($scope.logDate).format('YYYY-MM-DD'),requestURI: $scope.loginURL,logVersion: 'eq_1'})
            .then(function(response) {
                if ( response.data.statusCode == '200' ) {
                    handleBrowserResult(response.data.object);
                }
            }, function(x) {
                var data = JSON.parse('{"statusCode":"200","message":null,"_":null,"object":[{"explorerVer":"chrome\/41","platform":"win7","logVersion":"1","logActionTime":"20160425095807090","executeTime":29,"sessionid":"57552F30B61A9E667EB186938A88BC9E","ip":"0:0:0:0:0:0:0:1","logActionId":"ed69ee62ff494368a27e78bee0be7f99","items":[{"explorerVer":"chrome\/41","platform":"win7","logVersion":"1","logActionId":"ed69ee62ff494368a27e78bee0be7f99","logActionTime":"20160425095807090","requestURI":"\/reportMgr\/login","parameters":"username=admin`password=admin","sessionid":"57552F30B61A9E667EB186938A88BC9E","ip":"0:0:0:0:0:0:0:1"},{"explorerVer":"chrome\/41","platform":"win7","logVersion":"1","username":"admin","logActionId":"ed69ee62ff494368a27e78bee0be7f99","logActionTime":"20160425095807119","requestURI":"\/reportMgr\/login","parameters":"username=admin`password=admin","sessionid":"57552F30B61A9E667EB186938A88BC9E","ip":"0:0:0:0:0:0:0:1"}],"logStartTime":"20160425095807090","requestURI":"\/reportMgr\/login","logStartTimeF":"2016-04-25 09:58:07.090","parameters":"username=admin`password=admin","itemCount":2}]}');
                handleBrowserResult(data.object);
            });
    };

  }]);