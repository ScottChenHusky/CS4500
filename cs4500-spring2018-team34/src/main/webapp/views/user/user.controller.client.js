(function () {
    angular
        .module('webapp')
        .controller('HomeController', HomeController)
    function HomeController () {
        var model = this;
        model.name = "Test model!!!!";
    }
})();
