(function() {
	angular.module('webapp').controller('movieDetailsController',
			movieDetailsController);

	function movieDetailsController($sce, $http, $routeParams, $scope) {
		var vm = this;
		vm.movieId = $routeParams.mid;
		vm.userId = sessionStorage.getItem("currentUserId");
		vm.trailerId = "";
		vm.similarMovies = [];
		vm.similarMoviesNum = 0;

		vm.initMovie = initMovie;
		// post review
		vm.review = review;
		// get rate from stars
		vm.giveRate = giveRate;
		// get trail url
		vm.getUrl = getUrl;
		// control the length of movie names
		vm.cutString = cutString;
		// get similar movie
        vm.initsimilar = initsimilar;
		vm.len = 28;

		vm.comments = [];
		vm.actors = [];

		// html style control
		$("div a.more").click(
				function() {
					var eachLine = Math
							.floor($("div.movieList").width() / 155.97);
					var lines = 1 + Math
							.floor(($("div.movieList").height() - 212) / 254);
					if (vm.similarMoviesNum / eachLine > lines) {
						$("div.movieList").css("height", function() {
							return $(this).height() + 255 + "px";
						});
					}
				});

		function getUrl() {
			var url = "https://www.youtube.com/embed/" + vm.trailerId;
			return $sce.trustAsResourceUrl(url);
		}

		function initMovie() {
			// init current movie
			var url = '/api/movie/get';
			var package = {
				userId : vm.userId,
				movieId : vm.movieId
			};
			return $http.post(url, package).then(response, error);
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

		function initsimilar(){
            var url = '/api/movie/similar?id=' + vm.movieId;
            $http.get(url).then(function(res) {
                if (res.data != undefined) {
                	if(res.data.message == "found"){
                		console.log(res.data.similar);
                        vm.similarMovies = res.data.similar;
                        vm.similarMoviesNum = vm.similarMovies.length;
					}
                }
            });
        }
        initsimilar();
		// post review
		vm.rate = null;
		function giveRate(num) {
			vm.rate = num;
		}
		function review(review) {

			if (vm.rate == null || vm.rate == undefined) {
				vm.reviewError = "Please give a rate.";
				return;
			}
			if (review == null || review == undefined || review == "") {
				vm.reviewError = "Please give some comments.";
				return;
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

		function cutString(str) {
			if (str == undefined) {
				return "";
			}
			if (str.length * 2 <= vm.len) {
				return str;
			}
			var strlen = 0;
			var s = "";
			for (var i = 0; i < str.length; i++) {
				s = s + str.charAt(i);
				if (str.charCodeAt(i) > 128) {
					strlen = strlen + 2;
					if (strlen >= vm.len) {
						return s.substring(0, s.length - 1) + "…";
					}
				} else {
					strlen = strlen + 1;
					if (strlen >= vm.len) {
						return s.substring(0, s.length - 2) + "…";
					}
				}
			}
			return s;
		}
	}
})();
