(function() {
	angular.module('webapp').controller('movieDetailsController',
			movieDetailsController);

	function movieDetailsController($sce, $http, $routeParams, $scope) {
		var vm = this;
		vm.movieId = $routeParams.mid;
		vm.userId = sessionStorage.getItem("currentUserId");
		vm.trailerId = "";

		vm.initMovie = initMovie;
		// post review
		vm.review = review;
		// get the user info for each comment
		vm.getUserForComment = getUserForComment;
		// get rate from stars
		vm.giveRate = giveRate;
		
		// get trail url
		vm.getUrl = getUrl;

		vm.comments = [];
		vm.actors = [];

		function getUrl() {
            var url = "https://www.youtube.com/embed/" + vm.trailerId;
            return $sce.trustAsResourceUrl(url);
        }
		
		function initMovie() {
			var url = '/api/movie/get?id=' + vm.movieId;
			return $http.get(url, vm.movieId).then(response, error);
			function response(res) {
				vm.movie = res.data.movie;
				vm.comments = res.data.comment;
				vm.trailerId = res.data.movie.t1;
				// give a list of actors
				var string = vm.movie.actors;
				if (string != undefined || string != null) {
					for (var i = 0; i < string.length; i++) {
						var j = string.indexOf(",");
						if (j != -1) {
							vm.actors.push(string.slice(0, j));
							string = string.slice(j + 2, string.length);
						} else {
							vm.actors.push(string);
							return;
						}
					}
				}
			}
			function error(err) {
				vm.error = err.data.message;
			}
		}
		initMovie();
		getUserForComment(vm.userId);

		// post review
		vm.rate = null;
		function giveRate(num) {
			vm.rate = num;
		}
		function review(review) {

			if (vm.rate == null || vm.rate == undefined) {
				vm.reviewError = "Please give a rate."
			}
			if (review == null || review == undefined) {
				vm.reviewError = "Please give some comments."
			} else {
				var rR = {
					movieId : vm.movieId,
					customerId : vm.userId,
					score : "" + vm.rate + "",
					review : review
				};
				var url = '/api/movie/addComment';
				var data = JSON.stringify(rR);
				$http.post(url, data).then(function(response) {
					if (response.data.message === "exist") {
						vm.reviewError = "You have reviewed this movie!";
					} else {
						initMovie();
						window.location.reload();
					}
				});
			}
		}

		// get the user info for each comment
		function getUserForComment(userId) {
			var url = '/api/user?id=' + userId;
			$http.get(url, vm.userId).then(response);
			function response(res) {
				vm.user = res.data.result[0];
			}
			vm.username = vm.user.username;
		}
	}
})();
