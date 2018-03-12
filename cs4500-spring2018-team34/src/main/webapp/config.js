(function () {
    angular
        .module('webapp')
        .config(Config)
        
    function Config ($routeProvider) {
        $routeProvider
                .when('/', {
                    templateUrl: 'views/user/home.view.client.html',
                    controller: 'HomeController',
                    controllerAs: 'model'
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
        				templateUrl: 'views/user/search.html',
        				controller: 'SearchController',
        				controllerAs: 'model'
        			})
        			.when('/user/:uid', {
        				templateUrl: 'views/user/userProfile.view.client.html',
            			controller: 'userProfileController',
            			controllerAs: 'model'
        			})
        			.when('/movie/:mid', {
        				templateUrl: 'views/movie/movieDetails.view.client.html',
            			controller: 'movieDetailsController',
            			controllerAs: 'model'
        			});
    }
})();
