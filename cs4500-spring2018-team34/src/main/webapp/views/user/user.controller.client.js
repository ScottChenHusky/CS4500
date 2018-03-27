(function() {
	angular.module('webapp').controller('HomeController', HomeController)
			.controller('LoginController', LoginController).controller(
					'RegisterController', RegisterController).controller(
					'SearchController', SearchController).controller(
					'userProfileController', userProfileController);

	function HomeController($http) {
		var vm = this;
		vm.search = search;

		function search(searchTerm) {
			SearchController.search(searchTerm);
		}
//        vm.addMovieToDB = addMovieToDB;

        // var id_prefix = "tt0";
        // var id_end = 848228;
        //
        
        
        // function addMovieToDB() {
        // 	for(var i =6; i < 100; i++){
        // 		id_end += 1;
        // 		var id = id_prefix + id_end;
        // 		var url_front = "http://www.omdbapi.com/?i=";
        // 		var url_end = "&apikey=a65196c5";
        // 		var url = url_front + id + url_end;
        //
        //
        //         $.getJSON(url ,
        //                   function(data) {
        //                       return $http.post("/api/movie/addMovieFromOMDB", data)
        //                   });
			// }
        //
        //
        // }
        //
        // addMovieToDB();
    }
    
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
    				username: username,
    				password: password
    			};
    			return $http.post(url, user)
    				.then(response, error);
    			function response (res) {
    				var userId = res.data.id;
    				url = '/api/user?id=' + userId;

    				$http.get(url)
						.then(response, error);

    				function response(res) {
                        sessionStorage.setItem("currentUserId", userId);
                        sessionStorage.setItem("currentUserLevel", res.data.result[0].level);
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
                    loggedInUserId: userId,
                    userId: userId
				};

    			return $http.post(url, user)
					.then(response);

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
                    username: username,
                    email: email,
                    phone: phone
				};

                return $http.post(url, user)
                    .then(response, error);

                function response (res) {
                    $location.url("/user/" + res.data.id);
                    sessionStorage.setItem("currentUserId", res.data.id);
                }

                function error(err) {
                    vm.error = err.data.message;

                }
			}

    		
    		function register(username, email, phone, password1, password2, adminCode) {
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
    				username: username,
    				email: email,
    				phone: phone,
    				password: password1,
					adminCode: adminCode
    			};
    			
    			return $http.post(url, user)
    				.then(response, error);
    			function response (res) {
    				$location.url("/user/" + res.data.id);
    				sessionStorage.setItem("currentUserId", res.data.id);

            	}
    				
    			function error(err) {
    				vm.error = err.data.message;

    			}	
    		}

    }
    
    function userProfileController($http, $routeParams, $location) {
		var vm = this;
		vm.isOwner = false;
		vm.currentUserLevel = sessionStorage.getItem("currentUserLevel");
		vm.isFollowing = false;
		vm.userId = $routeParams.uid;
		vm.initProfile = initProfile;
		vm.follow = follow;
		vm.unfollow = unfollow;
		vm.updatePassword = updatePassword;
		// For Admin
		vm.deleteUser = deleteUser;
		vm.deleteMovie = deleteMovie;

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
				loggedInUserId: sessionStorage.getItem("currentUserId"),
				userId: vm.userId,
				oldPassword: old,
				newPassword: new1
			};

			return $http.post(url, user)
				.then(response, error);

			function response(res) {
                initProfile();
			}

			function error(err) {
				vm.error = err.data.message;
				return;
			}
		}
		
		function deleteMovie(movieId) {
            var url = "/api/deleteMovie";
            var movie = {
                loggedInUserId: sessionStorage.getItem("currentUserId"),
                movieId: movieId
            };

            $http.post(url, movie)
                .then(response, error);

            function response(res) {

            }

            function error(err) {
                vm.error = err.data.message;
            }
        }
		
		function deleteUser(userId) {

		    var url = "/api/deleteUser";
            var user = {
                loggedInUserId: sessionStorage.getItem("currentUserId"),
                userId: userId
            };

            $http.post(url, user)
                .then(response, error);

            function response(res) {

            }

            function error(err) {
                vm.error = err.data.message;
            }

        }
		
		$(".profile-section").hide(); // Hide all content
		$(".profile-section").hide();
		$(".action-section").hide();
		$(".follower-section").hide();
		$(".following-section").hide();
		$(".dash-section").hide();
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
		});

		// Show Follower Section
		$("div ul li.follower").click(function() {
			$(".movie-section").hide();
			$(".profile-section").hide();
			$(".action-section").hide();
			$(".following-section").hide();
			$(".follower-section").show();
			$(".dash-section").hide();
		});

		// Cancel Button
		$("section button.cancel").click(function() {
			window.location.reload();
//			$("ul.tabs li").removeClass("cur");
//			$(".movie-section").show();
//			$(".profile-section").hide();
//			$(".action-section").hide();
//			$(".follower-section").hide();
//			$(".following-section").hide();
//			$(".dash-section").hide();
		});

		function initProfile() {
        	
            var url = '/api/user?id=' + vm.userId;
            
            $http.get(url, vm.userId)
                .then(response);
            
            function response(res) {
            		vm.user = res.data.result[0];
            		
            		url = '/api/user/following/' + vm.userId;
            		$http.get(url)
            			.then(following_response);
            		
            		function following_response(res) {
            			vm.following = res.data.result;
            		}
            		
            		url = '/api/user/followers/' + vm.userId;
            		$http.get(url)
            			.then(followers_response);
            		
            		function followers_response(res) {
            			vm.follower = res.data.result;
            			//
            			if ($location.path().includes("/user/" + sessionStorage.getItem("currentUserId"))) {
            				vm.isOwner = true;
            			} else {
            				vm.isOwner = false;
            				vm.isFollowing = false;
            				for (var i = 0; i < vm.follower.length; i++) {
            					if (vm.follower[i].follower == sessionStorage.getItem("currentUserId")) {
            						vm.isFollowing = true;
            					}
            				}
            			}
            		}
            }
            
			

        }
        initProfile();
        
        function follow() {
        		var url = "/api/user/follow";
        		var obj = {
                    loggedInUserId: sessionStorage.getItem("currentUserId"),
        			from: sessionStorage.getItem("currentUserId"),
        			to: vm.userId
        		};
        		$http.post(url, obj)
        			.then(response, error);
        		
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
                    loggedInUserId: sessionStorage.getItem("currentUserId"),
        			from: sessionStorage.getItem("currentUserId"),
        			to: vm.userId
        		};
        		$http.post(url, obj)
    				.then(response, error);
    		
        		function response(res) {
        			initProfile();
        			return;
        		}
    		
        		function error(err) {
        			initProfile();
        			return;
        		}
        }
	}

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
                loggedInUserId: sessionStorage.getItem("currentUserId"),
                movieId: movieId
            };

            $http.post(url, movie)
                .then(response, error);

            function response(res) {

            }

            function error(err) {
                vm.error = err.data.message;
            }
        }
		
		function deleteUser(userId) {

		    var url = "/api/deleteUser";
            var user = {
                loggedInUserId: sessionStorage.getItem("currentUserId"),
                userId: userId
            };

            $http.post(url, user)
                .then(response, error);

            function response(res) {

            }

            function error(err) {
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

			// Movie Search Results
			$http.get(mUrl).then(function(response) {
				// number of movies
				vm.movieNum = 0;
				if (response.data != undefined) {
					vm.test = response.data;
					for (m in response.data) {
						if (m != "message" && m != "movie") {
							vm.movieNum++;
							vm.sum++;
						}
					}
					if (vm.movieNum != 0) {
						vm.movies = response.data;
						// vm.movies = response.data.movie.Search;
						// console.log(vm.movies);
						// console.log("message: " + vm.movies.message);
					} else {
						console.log("here!!!")
						if (response.data.movie.Response = "True") {
							if (response.data.movie.Search == undefined) {
								vm.movieNum = 0;
							} else {
								vm.movies = response.data.movie.Search;
								vm.movieNum = vm.movies.length;
							}
						}
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
})();
