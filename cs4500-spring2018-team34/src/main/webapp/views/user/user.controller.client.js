(function () {
    angular
        .module('webapp')
        .controller('HomeController', HomeController)
        .controller('LoginController', LoginController)
        .controller('RegisterController', RegisterController)
        .controller('SearchController', SearchController)
        .controller('userProfileController', userProfileController);
        
    function HomeController ($http) {
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
    				$location.url("/user/" + res.data.id);
    				sessionStorage.setItem("currentUserId", res.data.id);
            }
    			
    			function error(err) {
    				vm.error = err.data.message;
    			}
    			
    		}
    		
    		function logout() {
    			sessionStorage.removeItem("currentUserId");
    		}
    		
    		logout();

    }
    
    function RegisterController($http, $location) {
    		var vm = this;
    		vm.register = register;
//    		vm.phoneNumberValidation = phoneNumberValidation;
//    		
//    		function phoneNumberValidation(phone) {
//    			console.log("...........");
//    			if (phone === "") {
//    				return true;
//    			}
//    			
//    			if (phone.length !== 12) {
//    				console.log("!!!");
//    				return false;
//    			}
//
//    			var front = phone.substr(0, 3);
//    			var middle = phone.substring(4, 7);
//    			var rear = phone.substring(8, 12);
//    			
//    			console.log((!isNaN(parseInt(front, 10))) 
//    			&& (!isNaN(parseInt(middle, 10))) 
//    			&& (!isNaN(parseInt(rear, 10))) 
//    			&& (phone.charAt(3) === "-")
//    			&& (phone.charAt(7) === "-"));
//    			
//    			return (!isNaN(parseInt(front, 10))) 
//    			&& (!isNaN(parseInt(middle, 10))) 
//    			&& (!isNaN(parseInt(rear, 10))) 
//    			&& (phone.charAt(3) === "-")
//    			&& (phone.charAt(7) === "-");
//    			
//    		}
    		
    		function register(username, email, phone, password1, password2, admin) {
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
    				password: password1
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
    
    function userProfileController($http, $routeParams) {
		var vm = this;
		vm.userId = $routeParams.uid;
		vm.initProfile = initProfile;
		vm.follow = follow;
		vm.unfollow = unfollow;
		
		vm.fo = false;
		
        $(".profile-section").hide(); //Hide all content  
        $(".profile-section").hide();
   	    $(".action-section").hide();
   	    $(".follower-section").hide();
   	    $(".following-section").hide();
//        $("ul.tabs li:first").addClass("cur").show(); //Activate first tab  
//        $(".movie-section:first").show(); //Show first tab content  
          
        //On Click Event  
        // Show Movie Section
        $("ul.tabs li").click(function() {  
            $("ul.tabs li").removeClass("cur");
            $(this).addClass("cur");
            $(".movie-section").hide(); 
            $(".profile-section").hide();
            $(".action-section").hide();
            $(".follower-section").hide();
            $(".following-section").hide();
            var activeTab = $(this).find("a").attr("href"); 
            $(activeTab).fadeIn();
            return false;  
        });
        
        // Show All Movie Sections
        $("ul li.block").click(function() {
        	 $("ul.tabs li").removeClass("cur");
        	 $(".movie-section").show(); 
        	 $(".profile-section").hide();
        	 $(".action-section").hide();
        	 $(".follower-section").hide();
         $(".following-section").hide();
        });
        
        // Show Following Section
        $("div ul li.following").click(function() {
        	 $(".movie-section").hide(); 
        	 $(".profile-section").hide();
        	 $(".action-section").hide();
       	 $(".follower-section").hide();
        });
        
        // Show Follower Section
        $("div ul li.follower").click(function() {
       	 $(".movie-section").hide(); 
       	 $(".profile-section").hide();
       	 $(".action-section").hide();
      	 $(".following-section").hide();
        });
        
        // Cancel Button
        $("section button.btn-danger").click(function() {
        	 $("ul.tabs li").removeClass("cur");
        	 $(".movie-section").show(); 
        	 $(".profile-section").hide();
        	 $(".action-section").hide();
        	 $(".follower-section").hide();
         $(".following-section").hide();
        })

		function initProfile() {
            var url = '/api/user?id=' + vm.userId;
            return $http.get(url, vm.userId)
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
            		}
            		
            		
            }
        }
        initProfile();
        
        function follow() {
        		vm.fo = true;
        }
        
        function unfollow() {
    			vm.fo = false;
        }
	}

    function SearchController($http, $routeParams) {
		var vm = this;
		
		// Count Result numbers
		
		vm.userNum = 0;
		vm.movieNum = 0;
		vm.sum = 0;
		
		vm.term = $routeParams['term']; 
		vm.search = search;
		if(vm.term !== undefined && vm.term != '') {
			search(vm.term);
		}
		
		
		
		function search(searchTerm) {
			vm.keyword = searchTerm;
			var mUrl = "api/movie/search?name=" + searchTerm;
			var uUrl = "api/user?username=" + searchTerm;	
			vm.defaultView = false;
			vm.hasMResults = false;
			vm.hasUResults = false;
			vm.movies = [{poster: ''}];
			vm.users = [{image: ''}];
			
			$http.get(mUrl).then(function(response) {
				if(response.data != undefined) {		
					for (m in response.data) {
						if(m != "message") {
							vm.movieNum++;
							vm.sum++;
						} 
					}
					vm.movies = response.data;
					if(vm.movies.Movie0 != null) {
						vm.hasMResults = true;
					}	
				}
			});
			
			$http.get(uUrl).then(function(response) {
				if(response.data != undefined) {
					vm.users = response.data.result;
					vm.userNum = vm.users.length;
					vm.sum = vm.sum + vm.userNum;

					if(vm.users[0] != null) {
						for (var n = 0; n < vm.users.length; n++) {
							vm.users[n].image = "../../assets/images/user-photo.png";
						}
						vm.hasUResults = true;						
					}
					
				} 
			});
			
			//FOR TESTING
			//vm.hasMResults = true;
			//vm.hasUResults = true;
			//vm.movies[0].name = searchTerm;
			//vm.users[0].name = searchTerm;
		}
	}
})();
