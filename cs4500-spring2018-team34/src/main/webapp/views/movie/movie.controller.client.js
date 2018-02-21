(function () {
    angular
        .module('webapp')
        .controller('movieDetailsController', movieDetailsController)

function movieDetailsController($http, $routeParams) {
	var vm = this;
	vm.movieId = $routeParams.mid;
		vm.initMovie = initMovie;

		function initMovie() {
            var url = '/api/movie/get/' + vm.movieId;
            return $http.get(url, vm.movieId)
                .then(response)
            function response(res) {
            		vm.movie = res.data.movie;
            }           
        return;
    }
    initMovie();
	}
})();
