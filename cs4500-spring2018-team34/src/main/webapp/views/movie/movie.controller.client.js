(function() {
	angular.module('webapp').controller('movieDetailsController',
			movieDetailsController)

	function movieDetailsController($http, $routeParams) {
		var vm = this;
		vm.movieId = $routeParams.mid;
		vm.initMovie = initMovie;

		function initMovie() {
            var url = '/api/movie/get?id=' + vm.movieId;
            return $http.get(url, vm.movieId)
				.then(response, error)
			function response(res) {
				vm.movie = res;
				return;
			}

			function error(err) {
				vm.error = err.data.message;
				return;
			}
			return;
		}
		initMovie();
//		function initMovie() {
//            var url = '/api/user?id=' + vm.movieId;
//            return $http.get(url, vm.movieId)
//                .then(response)
//            function response(res) {
//            		vm.movie = res.data.result[0];
//            }            
//        return;
//    }
//		initMovie();
	}
})();
