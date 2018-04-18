(function() {
	angular.module('webapp').controller('HomeController', HomeController)
			.controller('LoginController', LoginController).controller(
					'RegisterController', RegisterController).controller(
					'SearchController', SearchController).controller(
					'userProfileController', userProfileController);

	//**************************** STARTS HOME CONTROLLER **************************//
	function HomeController($http) {
		var vm = this;
		vm.search = search;

		// get Lists of movies
		vm.init = init;

		// control the length of movie names
		vm.cutString = cutString;
		vm.len = 28;

		// Lists of movies
		vm.newMovies = [];
		vm.numberOfNewMovies = 0;

		vm.hotMovies = [];
		vm.numberOfHotMovies = 0;

		vm.recomMovies = [];
		vm.numberOfRecomMovies = 0;

		vm.currentUserId = getUserId();

		function getUserId(){
            return sessionStorage.getItem("currentUserId");
		}

		function search(searchTerm) {
			SearchController.search(searchTerm);
		}

		function cutString(str) {
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

		function init() {
			// get hot movie list
			var hot_url = 'api/movie/init?name=top';
//			var hot_url = '/api/movies/hotMovies';
			$http.get(hot_url).then(function(res) {
				if (res.data != undefined) {
					for (var m in res.data) {
						if (m == "top") {
							vm.hotMovies = res.data.top;
							vm.numberOfHotMovies = vm.hotMovies.length;
						}
					}
				}
			});

			// get new movie list
			var new_url = '/api/movie/init?name=new';
//			var new_url = '/api/movies/newMovies';
			$http.get(new_url).then(function(res) {
				if (res.data != undefined) {
					for (var m in res.data) {
						if (m == "new") {
							vm.newMovies = res.data.new;
							vm.numberOfNewMovies = vm.newMovies.length;
						}
					}
				}
			});


			// get recommend movie list
			var recom_url = '/api/movie/recommend';
			if(sessionStorage.getItem("currentUserId") != null){
                var userR = {
                    userId : sessionStorage.getItem("currentUserId")
                };
                $http.post(recom_url, userR).then(function(res) {
                    if (res.data != undefined) {
                        for (m in res.data) {
                            if (m == "recommend") {
                                vm.recomMovies = res.data.recommend;
                                vm.numberOfRecomMovies = vm.recomMovies.length;
                            }
                        }
                    }
                });
			}


		}
		init();

		// html style control
		$("section a.new-movies").click(
				function() {
					var eachLine = Math
							.floor($("ul.new_movies").width() / 155.97);
					var lines = 1 + Math
							.floor(($("ul.new_movies").height() - 212) / 254);
					if (vm.numberOfNewMovies / eachLine > lines) {
						$("ul.new_movies").css("height", function() {
							return $(this).height() + 258 + "px";
						});
					}
				});

		$("section a.hot-movies").click(
				function() {
					var eachLine = Math
							.floor($("ul.hot_movies").width() / 155.97);
					var lines = 1 + Math
							.floor(($("ul.hot_movies").height() - 212) / 254);
					if (vm.numberOfHotMovies / eachLine > lines) {
						$("ul.hot_movies").css("height", function() {						
							return $(this).height() + 255 + "px";
						});
					}
				});

		$("section a.recom-movies")
				.click(
						function() {
							var eachLine = Math.floor($("ul.recom_movies")
									.width() / 155.97);
							var lines = 1 + Math.floor(($("ul.recom_movies")
									.height() - 212) / 254);
							if (vm.numberOfRecomMovies / eachLine > lines) {
								$("ul.recom_movies").css("height", function() {
									return $(this).height() + 255 + "px";
								});
							}
						});
	}
	//**************************** ENDS HOME CONTROLLER **************************//

	//**************************** STARTS LOGIN CONTROLLER **************************//
	function LoginController($http, $location, $rootScope) {
		var vm = this;
		vm.login = login;
		vm.logout = logout;

		function login(username, password) {

			if (!username || !password) {
				vm.error = "Please Enter Username & Password";
				return;
			}

			var url = "/api/login";
			var user = {
				username : username,
				password : password
			};
			return $http.post(url, user).then(response, error);
			function response(res) {
				var userId = res.data.id;
				url = '/api/user?id=' + userId;

				$http.get(url).then(response, error);

				function response(res) {
					sessionStorage.setItem("currentUserId", userId);
					sessionStorage.setItem("currentUserLevel",
							res.data.result[0].level);
					$location.url("/user/" + userId);
				}

				function error(err) {
					vm.error = err.data.message;
				}

			}

			function error(err) {
				vm.error = err.data.message;
			}

		}

		function logout() {
			var userId = sessionStorage.getItem("currentUserId");

			var url = "/api/logout";
			var user = {
				loggedInUserId : userId,
				userId : userId
			};

			return $http.post(url, user).then(response);

			function response(res) {
				sessionStorage.removeItem("currentUserId");
				sessionStorage.removeItem("currentUserLevel");
				return;
			}
		}

		if (sessionStorage.getItem("currentUserId")) {
			logout();
		}

	}
	//**************************** ENDS LOGIN CONTROLLER **************************//
	
	//**************************** STARTS REGISTER CONTROLLER **************************//
	function RegisterController($http, $location) {
		var vm = this;
		vm.register = register;
		vm.applyAdminCode = applyAdminCode;

		function applyAdminCode(username, email, phone) {
			if (!username) {
				vm.error = "Please Enter Username";
				return;
			} else if (!email) {
				vm.error = "Please Enter Email";
				return;
			} else if (!phone) {
				vm.error = "Please Enter Phone Number";
				return;
			}

			var url = "/api/applyAdminCode";
			var user = {
				username : username,
				email : email,
				phone : phone
			};

			return $http.post(url, user).then(response, error);

			function response(res) {
				vm.error = null;
				vm.success = "Code Has Been Sent to Your Email."
			}

			function error(err) {
				vm.success = null;
				vm.error = err.data.message;

			}
		}

		function register(username, email, phone, password1, password2,
				adminCode) {
			if (!username) {
				vm.error = "Please Enter Username";
				return;
			} else if (!email) {
				vm.error = "Please Enter Email";
				return;
			} else if (!password1) {
				vm.error = "Please Enter Password";
				return;
			} else if (!password2) {
				vm.error = "Please Enter Verified Password";
				return;
			} else if (password1 !== password2) {
				vm.error = "Passwords don't match!";
				return;
			}

			var url = "/api/register";
			var user = {
				username : username,
				email : email,
				phone : phone,
				password : password1,
				adminCode : adminCode
			};

			return $http.post(url, user).then(response, error);
			function response(res) {
				sessionStorage.setItem("currentUserId", res.data.id);
				sessionStorage.setItem("currentUserLevel", res.data.level);
				$location.url("/user/" + res.data.id);

			}

			function error(err) {
				vm.error = err.data.message;

			}
		}

	}
	//**************************** ENDS REGISTER CONTROLLER **************************//

	//**************************** STARTS USER PROFILE CONTROLLER **************************//
	function userProfileController($http, $routeParams, $location, $scope) {
		var vm = this;
		vm.isOwner = false;
		vm.currentUserLevel = sessionStorage.getItem("currentUserLevel");
		vm.currentUserId = sessionStorage.getItem("currentUserId");
		vm.user = null;
		vm.friendRecomMovies = [];

		// functions
		vm.initProfile = initProfile;
		vm.initMovieLists = initMovieLists;
		vm.isFollowing = false;
		vm.userId = $routeParams.uid;
		vm.follow = follow;
		vm.unfollow = unfollow;
		vm.updatePassword = updatePassword;
		vm.createPlayList = createPlayList;
		vm.updateUserProfile = updateUserProfile;
		vm.deletePlaylist = deletePlaylist;
		// control capacity of each list
		// vm.viewMore = viewMore;
		// vm.capacity = 40;
		// control the length of movie names
		vm.cutString = cutString;
		vm.len = 28;
		vm.testName = "Movie Section Starts Movie Section Starts";

		// For Admin
		//======================== STARTS DASHBOARD ==========================//
		vm.allUser = [];
		vm.allMovie = [];
		vm.userSum = 0;
		vm.movieSum = 0;

		vm.deleteUser = deleteUser;
		vm.deleteMovie = deleteMovie;
		vm.dividePages = dividePages;

		$("ul li.system_man").click(function() {
			var url = '/api/system';

			$http.get(url).then(response);

			function response(res) {
				vm.overallInfo = res.data;
				vm.userSum = vm.overallInfo['User Count'];
				vm.movieSum = vm.overallInfo['Movie Count'];
				vm.root = vm.overallInfo.Root[0];
			}
		});
		

		$("ul li.user_man").click(function() {
			// get All users
			var uUrl = "api/getAllUsers/" + vm.userId;
			$http.get(uUrl).then(function(response) {
				if (response.data != undefined) {
					vm.allUser = response.data.result;
					$scope.sumU = vm.allUser.length;
					
					dividePages('user', vm.allUser, $scope.sumU);
				}
			});
		});
		
		
		$("ul li.movie_man").click(function() {
			// get all movies
			var mUrl = "/api/movie/init?name=all";
			$http.get(mUrl).then(function(response) {
				if (response.data != undefined) {
					vm.allMovie = response.data.all;
					$scope.sumM = vm.allMovie.length;
					dividePages('movie', vm.allMovie, $scope.sumM);
				}
			});
		});


		function updateUserProfile(email, phone) {
			var url = "/api/updateUser"
			var newProfile = {
                loggedInUserId : vm.currentUserId,
                userId: vm.currentUserId,
                email : email,
                phone: phone
			};

			$http.post(url, newProfile).then(response, error);

			function response(res) {
                initProfile();
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

		function createPlayList(name, description) {
            var url = "/api/createPlaylist";
            var playList = {
                loggedInUserId : vm.currentUserId,
				userId: vm.currentUserId,
                name : name,
                description: description
            };

            $http.post(url, playList).then(response, error);

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
		
		function dividePages(type, data, sum) {
			// Make page tabs
	        $scope.pageSize = 7;
	        $scope.pages = Math.ceil(sum / $scope.pageSize);
	        $scope.newPages = $scope.pages > 5 ? 5 : $scope.pages;
	        $scope.pageList = [];
	        $scope.selPage = 1;
	        
	        // set each page
	        $scope.setData = function () {
	            $scope.uItems = data.slice(($scope.pageSize * ($scope.selPage - 1)), ($scope.selPage * $scope.pageSize));
	            $scope.mItems = data.slice(($scope.pageSize * ($scope.selPage - 1)), ($scope.selPage * $scope.pageSize));
	        }
	        
	        if (type == 'movie') {
	        		$scope.mItems = data.slice(0, $scope.pageSize);
	        } 
	        if (type == 'user') {
	        		$scope.uItems = data.slice(0, $scope.pageSize);
	        }
	        
//	        $scope.items = data.slice(0, $scope.pageSize);
	        // repeat page numbers
	        for (var i = 0; i < $scope.newPages; i++) {
	            $scope.pageList.push(i + 1);
	        }
	        $scope.selectPage = function (page) {
	            if (page < 1 || page > $scope.pages) return;
	            if (page > 2) {
	                var newpageList = [];
	                for (var i = (page - 3) ; i < ((page + 2) > $scope.pages ? $scope.pages : (page + 2)) ; i++) {
	                    newpageList.push(i + 1);
	                }
	                $scope.pageList = newpageList;
	            }
	            $scope.selPage = page;
	            $scope.setData();
	            $scope.isActivePage(page);
	        };
	        $scope.isActivePage = function (page) {
	            return $scope.selPage == page;
	        };
	        $scope.Previous = function () {
	            $scope.selectPage($scope.selPage - 1);
	        }
	        $scope.Next = function () {
	            $scope.selectPage($scope.selPage + 1);
	        };
		}
		
	

		// for user manage dashboard
		$scope.userHead = {
			id : "ID",
			username : "Name",
			email : "Email",
			phone : "Phone",
			isOnline : "Online",
			level : "Level",
			lastLogin : "Last Login",
		};
		$scope.userSort = {
			column : 'id',
			descending : '+'
		};
		$scope.userOrder = $scope.userSort.descending + $scope.userSort.column;
		$scope.selectedUserCls = function(column) {
			return column == $scope.userSort.column
					&& $scope.userSort.descending;
		};

		// for movie manage dashboard
		$scope.movieHead = {
			id : "ID",
			name : "Name",
			score : "Rate",
			level : "Level",
			date : "Year",
			time: "Duration",
			language : "Language",
			country: "Country"
		};
		$scope.movieSort = {
			column : 'id',
			descending : '+'
		};
		$scope.movieOrder = $scope.movieSort.descending + $scope.movieSort.column;
		$scope.selectedmovieCls = function(column) {
			return column == $scope.movieSort.column
					&& $scope.movieSort.descending;
		};

		$scope.changeSorting = function(type, column) {
			if (type == 'user') {
				if ($scope.userSort.column = column) {
					if ($scope.userSort.descending == '+') {
						$scope.userSort = {
							column : column,
							descending : '-'
						}
					} else {
						$scope.userSort = {
							column : column,
							descending : '+'
						}
					}
				} else {
					$scope.userSort = {
						column : column,
						descending : '+'
					}
				}
				return $scope.userOrder = $scope.userSort.descending
						+ $scope.userSort.column;
			}
			if (type == 'movie') {
				if ($scope.movieSort.column = column) {
					if ($scope.movieSort.descending == '+') {
						$scope.movieSort = {
							column : column,
							descending : '-'
						}
					} else {
						$scope.movieSort = {
							column : column,
							descending : '+'
						}
					}
				} else {
					$scope.movieSort = {
						column : column,
						descending : '+'
					}
				}
				return $scope.movieOrder = $scope.movieSort.descending
						+ $scope.movieSort.column;
			}
		};
		
		function deleteMovie(movieId) {
			var url = "/api/deleteMovie";
			var movie = {
				loggedInUserId : sessionStorage.getItem("currentUserId"),
				movieId : movieId
			};

			$http.post(url, movie).then(response, error);

			function response(res) {

			}

			function error(err) {
				vm.error = err.data.message;
			}

		}

		function deleteUser(userId) {

			var url = "/api/deleteUser";
			var user = {
				loggedInUserId : sessionStorage.getItem("currentUserId"),
				userId : userId
			};

			$http.post(url, user).then(response, error);

			function response(res) {

			}

			function error(err) {
				vm.error = err.data.message;
			}
		}
		
		
		//======================== ENDS DASHBOARD ==========================//

		
		//======================== STARTS INITIAL ==========================//
		function initProfile() {
			var url = '/api/user?id=' + vm.userId;
			$http.get(url, vm.userId).then(response);

			function response(res) {
				// get current User Info
				vm.user = res.data.result[0];

				// get following info
				url = '/api/user/following/' + vm.userId;

				$http.get(url).then(following_response);
				function following_response(res) {
					vm.following = res.data.result;
				}
				// get followers info
				url = '/api/user/followers/' + vm.userId;
				$http.get(url).then(followers_response);

				function followers_response(res) {
					vm.follower = res.data.result;
			
					if ($location.path().includes(
							"/user/" + sessionStorage.getItem("currentUserId"))) {
						vm.isOwner = true;
					} else {
						vm.isOwner = false;
						vm.isFollowing = false;
						for (var i = 0; i < vm.follower.length; i++) {
							if (vm.follower[i].follower == sessionStorage
									.getItem("currentUserId")) {
								vm.isFollowing = true;
							}
						}
					}
				}

				// get Favorite Movie list
				// vm.favoriteMovies = res.data.result[0].favoriteList;
			}

		}
		initProfile();
		
		function initMovieLists() {
			// get recommend movie list
			var fri_url = '/api/getUserRecommendationOfMovies/' + vm.userId;
			$http.get(fri_url).then(function(res) {
				if (res.data.result != undefined) {
					vm.friendRecomMovies = res.data.result;
					vm.friendRecomMoviesNum = vm.friendRecomMovies.length;	
				}
			});
			
			// get userPlayLists:
            var url = "/api/getPlaylists/" + vm.userId;
            $http.get(url).then(function(res) {
            	if (res.data != undefined) {
            		vm.playLists = res.data.result;
            	}
			});
		}

        initMovieLists();

        function deletePlaylist(playlistId) {
            var url = "/api/deletePlaylist";
            var ob = {
                loggedInUserId : vm.currentUserId,
                userId: vm.currentUserId,
                playlistId : playlistId
            };

            $http.post(url, ob).then(response, error);

            function response(res) {
                var url = "/api/getPlaylists/" + vm.userId;
                $http.get(url).then(function(res) {
                    if (res.data != undefined) {
                        vm.playLists = res.data.result;
                    }
                });
                return;
            }

            function error(err) {
                vm.success = null;
                vm.error = err.data.message;
                return;
            }
        }

		//======================== ENDS INITIAL ==========================//
		
		//======================== STARTS PROFILE MODIFICATION ==========================//
		function updatePassword(old, new1, new2) {
			if (!old) {
				vm.error = "Please enter the old password";
				return;
			} else if (!new1) {
				vm.error = "Please enter the new password";
				return;
			} else if (new1 != new2) {
				vm.error = "New password don't match";
				return;
			}

			var url = "/api/updateUserPassword";
			var user = {
				loggedInUserId : sessionStorage.getItem("currentUserId"),
				userId : vm.userId,
				oldPassword : old,
				newPassword : new1
			};

			return $http.post(url, user).then(response, error);

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
		//======================== ENDS PROFILE MODIFICATION ==========================//
		
		
		//======================== STARTS FOLLOW/UNFOLLOW ==========================//
		function follow() {
			var url = "/api/user/follow";
			var obj = {
				loggedInUserId : sessionStorage.getItem("currentUserId"),
				from : sessionStorage.getItem("currentUserId"),
				to : vm.userId
			};
			$http.post(url, obj).then(response, error);

			function response(res) {
				initProfile();
				return;
			}

			function error(err) {
				initProfile();
				return;
			}
		}

		function unfollow() {
			var url = "/api/user/un-follow";
			var obj = {
				loggedInUserId : sessionStorage.getItem("currentUserId"),
				from : sessionStorage.getItem("currentUserId"),
				to : vm.userId
			};
			$http.post(url, obj).then(response, error);

			function response(res) {
				initProfile();
				return;
			}

			function error(err) {
				initProfile();
				return;
			}
		}
		//======================== ENDS FOLLOW/UNFOLLOW ==========================//
		
		//======================== STARTS LONG NAME CONTROL ==========================//
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
		//======================== ENDS LONG NAME CONTROL ==========================//

		//======================== STARTS HTML TABS CONTROL ==========================//
		$(".profile-section").hide(); // Hide all content
		$(".profile-section").hide();
		$(".action-section").hide();
		$(".follower-section").hide();
		$(".following-section").hide();
		$(".dash-section").hide();
		$(".movie-section").hide();
		// $("ul.tabs li:first").addClass("cur").show(); //Activate first tab
		// $(".movie-section:first").show(); //Show first tab content

		// On Click Event
		// Show Movie Section
		$("ul.tabs li").click(function() {
			$("ul.tabs li").removeClass("cur");
			$(this).addClass("cur");
			$(".movie-section").hide();
			$(".profile-section").hide();
			$(".action-section").hide();
			$(".follower-section").hide();
			$(".following-section").hide();
			$(".dash-section").hide();
			$(".movieSummary-section").hide();
			var activeTab = $(this).find("a").attr("href");
			$(activeTab).fadeIn();
			return false;
		});

		// Show Following Section
		$("div ul li.following").click(function() {
			$(".movie-section").hide();
			$(".profile-section").hide();
			$(".action-section").hide();
			$(".follower-section").hide();
			$(".dash-section").hide();
			$(".movieSummary-section").hide();
		});

		// Show Follower Section
		$("div ul li.follower").click(function() {
			$(".movie-section").hide();
			$(".profile-section").hide();
			$(".action-section").hide();
			$(".following-section").hide();
			$(".follower-section").show();
			$(".dash-section").hide();
			$(".movieSummary-section").hide();
		});

		// Cancel Button
		$("section button.cancel").click(function() {
			window.location.reload();
		});
		//======================== ENDS HTML TABS CONTROL ==========================//
	}
	//**************************** ENDS USER PROFILE CONTROLLER **************************//

	//**************************** STARTS SEARCH CONTROLLER **************************//
	function SearchController($http, $routeParams) {
		var vm = this;

		vm.term = $routeParams['term'];

		// Capacity of movie/user result lists
		vm.movieCap = 9;
		vm.movieCapTab = 19;
		vm.userCap = 4;
		vm.userCapTab = 9;

		// Functions
		vm.currentUserLevel = sessionStorage.getItem("currentUserLevel");
		vm.deleteUser = deleteUser;
		vm.deleteMovie = deleteMovie;

		vm.moreUsers = moreUsers;
		vm.moreUsersTab = moreUsersTab;
		vm.moreMovies = moreMovies;
		vm.moreMoviesTab = moreMoviesTab;

		// Count Result numbers

		vm.userNum = 0;
		vm.movieNum = 0;
		vm.sum = 0;

		vm.search = search;
		if (vm.term !== undefined && vm.term != '') {
			search(vm.term);
		}

		function deleteMovie(movieId) {
			var url = "/api/deleteMovie";
			var movie = {
				loggedInUserId : sessionStorage.getItem("currentUserId"),
				movieId : movieId
			};

			$http.post(url, movie).then(response, error);

			function response(res) {
				vm.error = null;
				vm.success = res.data.message;
				search(vm.term);
			}

			function error(err) {
				vm.success = null;
				vm.error = err.data.message;
			}
		}

		function deleteUser(userId) {

			var url = "/api/deleteUser";
			var user = {
				loggedInUserId : sessionStorage.getItem("currentUserId"),
				userId : userId
			};

			$http.post(url, user).then(response, error);

			function response(res) {
				vm.error = null;
				vm.success = res.data.message;
				search(vm.term);
			}

			function error(err) {
				vm.success = null;
				vm.error = err.data.message;
			}

		}

		function search(searchTerm) {
			vm.keyword = searchTerm;
			var mUrl = "api/movie/search?name=" + searchTerm;
			var uUrl = "api/user?username=" + searchTerm;

			vm.movies = [];
			vm.users = [];

			// number of all results
			vm.sum = 0;
			vm.movieNum = 0;

			// Movie Search Results
			$http.get(mUrl).then(function(response) {
				// number of movies
				if (response.data.Movie.message == "Not Found") {
					return;
				}
				for (m in response.data) {
					if (m == "Movie") {
						if (response.data.Movie.Results == undefined) {
							vm.exactMovie = response.data.Movie;
						} else {
							vm.exactMovie = response.data.Movie.Results;
						}
					}
					if (m == "Name") {
						vm.movies = response.data.Name;
						vm.movieNum = vm.movieNum + vm.movies.length;
						vm.sum = vm.sum + vm.movies.length;
					}
				}
			});

			// User Search Results
			$http
					.get(uUrl)
					.then(
							function(response) {
								vm.userNum = 0;
								if (response.data != undefined) {
									vm.users = response.data.result;
									vm.userNum = vm.users.length;
									vm.sum = vm.sum + vm.userNum;

									if (vm.users[0] != null) {
										for (var n = 0; n < vm.users.length; n++) {
											vm.users[n].image = "../../assets/images/user-photo.png";
										}
										vm.hasUResults = true;
									}

								}
							});
		}

		function moreMovies() {
			vm.movieCap = vm.movieCap + 10;
		}

		function moreMoviesTab() {
			vm.movieCapTab = vm.movieCapTab + 10;
		}

		function moreUsers() {
			vm.userCap = vm.userCap + 5;
		}

		function moreUsersTab() {
			vm.userCapTab = vm.userCapTab + 10;
		}
	}
	//**************************** ENDS SEARCH CONTROLLER **************************//
})();