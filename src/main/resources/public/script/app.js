var app = angular.module('shippingApp', [
    'ngResource',
    'ngRoute'
]);

app.config(function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'views/rates.html',
        controller: 'ratesCtrl'
    }).otherwise({
        redirectTo: '/'
    })
});

app.controller('ratesCtrl', function($scope, $http) {

	$scope.request = {};
    $scope.rateList = [];
    $scope.retrievedData = false;
    
    //filtering
    $scope.searchRate = '';
        
    $scope.submit = function(request) {
    	console.log(request);
    	$http.post('/rates', request).then(function (data) {
            $scope.retrievedData = true;
            $scope.rateList = data.data;
        }, function (error) {
            console.log(error, 'that did not work');
        });
	}
});