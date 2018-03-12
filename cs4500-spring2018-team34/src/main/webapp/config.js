(function () {
    angular
        .module('webapp')
        .config(Config)
        
    function Config($routeProvider) {
        $routeProvider
                .when('/', {
                    templateUrl: 'views/user/home.view.client.html',
                    controller: 'HomeController',
                    controllerAs: 'model',
                    resolve: {
                    		CurrentUser: getCurrentUser
                    }
                })
                
                .when('/login', {
        				templateUrl: 'views/user/login.view.client.html',
        				controller: 'LoginController',
        				controllerAs: 'model'
        			})
        			
        			.when('/register', {
        				templateUrl: 'views/user/register.view.client.html',
        				controller: 'RegisterController',
        				controllerAs: 'model'
        			})
        			.when('/search/:term', {
        				templateUrl: 'views/user/search.view.client.html',
        				controller: 'SearchController',
        				controllerAs: 'model'
        			})
        			.when('/search/', {
        				templateUrl: 'views/user/search.view.client.html',
        				controller: 'SearchController',
        				controllerAs: 'model'
        			})
        			.when('/user/:uid', {
        				templateUrl: 'views/user/userProfile.view.client.html',
            			controller: 'userProfileController',
            			controllerAs: 'model',
            			resolve: {
                            loggedIn: checkLoggedIn
                    }
        			})
        			.when('/movie/:mid', {
        				templateUrl: 'views/movie/movieDetails.view.client.html',
            			controller: 'movieDetailsController',
            			controllerAs: 'model',
            			resolve: {
                            loggedIn: checkLoggedIn
                    }
        			});
        
        function getCurrentUser($http, $rootScope, $location, $q) {
        		var deferred = $q.defer();
        		$http.get("/api/isLoggedIn")
        			.then(response, error);
        		
        		function response(res) {
        			var user = res.data;
        			if(user == "") {
        				$rootScope.currentUser = null;
        			} else {
                    $rootScope.currentUser = user;
                }
        		}
        		
        		function error(err) {
    				$location.url("/login");
    			}
        		
        		deferred.resolve();
        		return deferred.promise;
        }
        
        function checkLoggedIn($http, $location, $q, $rootScope) {

            var deferred = $q.defer();
            
			$http.get("/api/isLoggedIn")
				.then(response, error);
			
			function response (res) {
				var user = res.data;
                if(user == "") {
                    $rootScope.currentUser = null;
                    deferred.reject();
                    $location.url("/login");
                } else {
                    $rootScope.currentUser = user;
                    deferred.resolve();
                }
			}
			
			function error(err) {
				$location.url("/login");
			}
            return deferred.promise;
        }
        
    }
})();
