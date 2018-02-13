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
    }

})();
