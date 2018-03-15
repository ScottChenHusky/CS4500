(function() {
	angular.module('webapp').controller('movieDetailsController',
			movieDetailsController);

	function movieDetailsController($http, $routeParams, $scope) {
		var vm = this;
		vm.movieId = $routeParams.mid;
		vm.initMovie = initMovie;
		vm.comments = [];
		
		function initMovie() {
            var url = '/api/movie/get?id=' + vm.movieId;
            return $http.get(url, vm.movieId)
				.then(response, error);
			function response(res) {
				vm.movie = res.data.movie;
				vm.comments = res.data.comment;
			}

			function error(err) {
				vm.error = err.data.message;

			}
		}
		
		
		$scope.rating = 0;
		$scope.summary = "";
		$scope.review = "";
		$scope.user = sessionStorage.getItem("currentUserId");
		
		$scope.postReview = function() {
			var url = '/api/movie/addComment';
			var rR = {movieId:vm.movieId, customerId:$scope.user, score:""+$scope.rating+"", review:$scope.review};	
			var data = JSON.stringify(rR);
			$http.post(url, data).then(function(response) {
				if(response.data.message === "exist") {
					vm.error = "You have reviewed this movie!";
				} else {
					initMovie();
				}
			});
		}
		
		initMovie();
	}
})();
