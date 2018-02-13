
(function () {
    angular
        .module('webapp')
        .controller('indexcontroller', indexcontroller)
    function indexcontroller () {
        var model = this;
        model.name = "Test model!";
    }
})();
