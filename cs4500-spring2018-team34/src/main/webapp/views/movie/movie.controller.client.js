(function() {
	angular.module('webapp').controller('movieDetailsController',
			movieDetailsController);

	function movieDetailsController($http, $routeParams, $scope) {
		var vm = this;
		vm.movieId = $routeParams.mid;
		vm.initMovie = initMovie;

		function initMovie() {
            var url = '/api/movie/get?id=' + vm.movieId;
            return $http.get(url, vm.movieId)
				.then(response, error);
			function response(res) {
				vm.movie = res.data.movie;

			}

			function error(err) {
				vm.error = err.data.message;

			}
		}
		
		
		$scope.rating = 0;
		$scope.summary = "";
		$scope.review = "";
		
		$scope.postReview = function() {
			var url = '/api/movie/addComment'
			var rR = {movieId:vm.movieId, rating:$scope.rating, summary:$scope.summary, review:$scope.review};
			console.log(rR);
			var data = JSON.stringify(rR);
			console.log(data);
			$http.post(url, data).then(function(response) {
				if(response.message == "Already comment") {
					alert("You've already reviewed this movie");
				} else {
					initMovie();
				}
			});
		}
		
		initMovie();
	}
})();
