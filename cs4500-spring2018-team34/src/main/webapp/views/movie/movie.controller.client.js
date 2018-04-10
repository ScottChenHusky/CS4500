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
        // show modal
        vm.showModal = showModal;
        // recommend this movie To a Friend
        vm.recommend = recommend;
        // add a movie to a playlist
        vm.addToPlaylist = addToPlaylist;
        
        vm.updateNavPosition = updateNavPosition;
        vm.stopDefault = stopDefault;

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

		function addToPlaylist(playListId) {
		    var url = "/api/addMovieToPlaylist";
		    var ob = {
                loggedInUserId: vm.userId,
                userId: vm.userId,
                playlistId: playListId,
                movieId: vm.movieId
            }

            $http.post(url, ob).then(response, error);

		    function response(res) {
                vm.error = null;
                vm.success = res.data.message;
                return;
            }

            function error(err) {
                vm.success = null;
                vm.error = err.data.message;
                return;
            }

        }

		function recommend(toUserId) {
			var url = "/api/recommendMovieToUser";
			var ob = {
                loggedInUserId: vm.userId,
				from: vm.userId,
				to: toUserId,
                movieId: vm.movieId
			};
			$http.post(url, ob)
				.then(response, error);

			function response(res) {
                vm.error = null;
                vm.success = res.data.message;
                return;
			}

			function error(err) {
                vm.success = null;
                vm.error = err.data.message;
                return;
			}
        }

		function showModal(tag, btn, closeBtn) {
            // get followers info
			if (tag === "recommendToFriends") {
                var url = '/api/user/followers/' + vm.userId;
                $http.get(url).then(followers_response);

                function followers_response(res) {
                    vm.follower = res.data.result;
                }
			} else if (tag === "addToPlayLists") {
				var url = "/api/getPlaylists/" + vm.userId;
				$http.get(url).then(playLists_response);

				function playLists_response(res) {
					vm.playLists = res.data.result;
				}
            }

            var modal = document.getElementById(tag);

			// Get the button that opens the modal
            var btn = document.getElementById(btn);

            // Get the <span> element that closes the modal
            var span = document.getElementsByClassName(closeBtn)[0];

			// When the user clicks the button, open the modal
            btn.onclick = function() {
                modal.style.display = "block";
            }

			// When the user clicks on <span> (x), close the modal
            span.onclick = function() {
                modal.style.display = "none";
            }

			// When the user clicks anywhere outside of the modal, close it
            window.onclick = function(event) {
                if (event.target == modal) {
                    modal.style.display = "none";
                }
            }
		}

		function initMovie() {
			// init current movie
			var url = '/api/movie/get?userId=' + vm.userId + '&movieId=' + vm.movieId;
			return $http.get(url).then(response, error);
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
		
		$(".slide").thumbnailImg({
			small_elem : ".small_list",
			left_btn : ".slide_left",
			right_btn : ".slide_right"
		});
		var viewSwiper, previewSwiper;
		$(document).ready(function() {
			var stepW = 30;
			var stars = $(".stars > li");
			$(".showb").css("width", 0);
			stars.each(function(i) {
				$(stars[i]).click(function(e) {
					var n = i + 1;
					$(".showb").css({
						"width" : stepW * n
					});
					$(this).find('a').blur();
					return stopDefault(e);
					return descriptionTemp;
				});
			});

			viewSwiper = new Swiper('.view .swiper-container', {
				// autoplay : 3000,
				loop : true,
				onSlideChangeStart : function() {
					updateNavPosition()
				}
			});

			previewSwiper = new Swiper('.preview .swiper-container', {
				visibilityFullFit : true,
				slidesPerView : 'auto',
				onlyExternal : true,
				onSlideClick : function() {
					viewSwiper.swipeTo(previewSwiper.clickedSlideIndex)
				}

			});
		});

		function updateNavPosition() {
			$('.preview .active-nav').removeClass('active-nav')
			var activeNav = $('.preview .swiper-slide').eq(
					viewSwiper.activeIndex - 1).addClass('active-nav')
			if (!activeNav.hasClass('swiper-slide-visible')) {
				if (activeNav.index() > previewSwiper.activeIndex) {
					var thumbsPerNav = Math.floor(previewSwiper.width
							/ activeNav.width()) - 1
					previewSwiper.swipeTo(activeNav.index() - thumbsPerNav)
				} else {
					previewSwiper.swipeTo(activeNav.index())
				}
			}

		}

		function stopDefault(e) {
			if (e && e.preventDefault)
				e.preventDefault();
			else
				window.event.returnValue = false;
			return false;
		};
	}
	
	
})();
