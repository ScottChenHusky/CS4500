(function() {
	angular.module('webapp').controller('movieDetailsController',
			movieDetailsController);

	function movieDetailsController($http, $routeParams, $scope) {
		var vm = this;
		vm.movieId = $routeParams.mid;
		vm.userId = sessionStorage.getItem("currentUserId");

		vm.initMovie = initMovie;
		// post review
		vm.review = review;
		//get the user info for each comment
		vm.getUserForComment = getUserForComment;

		vm.comments = [];

		function initMovie() {
			var url = '/api/movie/get?id=' + vm.movieId;
			return $http.get(url, vm.movieId).then(response, error);
			function response(res) {
				vm.movie = res.data.movie;
				vm.comments = res.data.comment;
			}
			function error(err) {
				vm.error = err.data.message;
			}
		}
		initMovie();
		getUserForComment(vm.userId);

		// post review
		function review(rating, review) {
			var url = '/api/movie/addComment';
			console.log("rating: " + rating);
			if (rating == null || rating == undefined) {
				vm.reviewError = "Please give a rate."
			}
			if (review == null || review == undefined) {
				vm.reviewError = "Please give some comments."
			} else {
				var rR = {
					movieId : vm.movieId,
					customerId : vm.userId,
					score : "" + rating + "",
					review : review
				};
				var data = JSON.stringify(rR);
				$http.post(url, data).then(function(response) {
					if (response.data.message === "exist") {
						vm.error = "You have reviewed this movie!";
					} else {
						initMovie();
					}
				});
			}
		}

		//get the user info for each comment
		function getUserForComment(userId) {
			console.log("here")
			var url = '/api/user?id=' + userId;
			return $http.get(url, vm.userId).then(response);
			function response(res) {
				vm.user = res.data.result[0];
			}
			vm.username = vm.user.username;
		}
		
		
	}
})();
