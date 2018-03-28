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
                    		loggedIn: checkLoggedIn
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
            		.when('/join_us', {
                		templateUrl: 'views/user/register.view.admin.html',
                		controller: 'RegisterController',
                		controllerAs: 'model'
            		})
        			.when('/search/:term', {
        				templateUrl: 'views/user/search.html',
        				controller: 'SearchController',
        				controllerAs: 'model',
                        resolve: {
                            loggedIn: checkLoggedIn
                        }
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
        				templateUrl: 'views/movie/newMovieDetails.html',
            			controller: 'movieDetailsController',
            			controllerAs: 'model',
            			resolve: {
                            loggedIn: checkLoggedIn
                    	}
        			});
  
        
        function checkLoggedIn($http, $location, $q, $rootScope) {

            var deferred = $q.defer();
            
            if (sessionStorage.getItem("currentUserId") == null) {
            		$rootScope.currentUserId = null;
            		if ($location.path() == "/") {
            			deferred.resolve();
            		} else {
            			deferred.reject();
            			$location.url("/login");
            		}
            } else {
            		$rootScope.currentUserId = sessionStorage.getItem("currentUserId");
                deferred.resolve();
            }
            
            return deferred.promise;
        }
        
    }
})();
