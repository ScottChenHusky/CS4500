(function () {
    angular
        .module('webapp')
        .config(Config)
        
    function Config ($routeProvider) {
        $routeProvider
                //User pages
                .when('/', {
                    templateUrl: 'homepage.html',
                    controller: 'indexcontroller',
                    controllerAs: 'model'
                })
    }

})();
