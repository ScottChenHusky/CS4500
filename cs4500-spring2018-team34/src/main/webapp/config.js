(function () {
    angular
        .module('webapp')
        .config(Config)
        
    function Config ($routeProvider) {
        $routeProvider
                .when('/', {
                    templateUrl: 'views/user/homepage.html',
                    controller: 'HomeController',
                    controllerAs: 'model'
                })
    }

})();
