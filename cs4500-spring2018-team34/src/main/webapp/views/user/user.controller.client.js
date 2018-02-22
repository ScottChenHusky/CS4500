(function () {
    angular
        .module('webapp')
        .controller('HomeController', HomeController)
        .controller('LoginController', LoginController)
        .controller('RegisterController', RegisterController)
        .controller('userProfileController', userProfileController)
        .controller('userProfileController', userProfileController)
        
    function HomeController ($http) {
        var vm = this;
        vm.name = "Test model!!!!";
        vm.addMovieToDB = addMovieToDB;

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
    
    function LoginController($http, $location) {
    		var vm = this;
    		vm.login = login;
    		
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
    				.then(response, error)
    			function response (res) {
    				$location.url("/user/" + res.data.id);			
                return;
            }
    			
    			function error(err) {
    				vm.error = err.data.message;
    				return;
    			}
    		}
    		return;
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
    				.then(response, error)
    			function response (res) {
    				console.log(res);
    				$location.url("/user/" + res.data.id);
                return;
            }
    				
    			function error(err) {
    				vm.error = err.data.message;
    				return;
    			}	
    		}
    		return;
    }
    
    function userProfileController($routeParams) {
    		var vm = this;
    		vm.userId = $routeParams.userId;
    		vm.initProfile = initProfile;
    		
    		function initProfile() {
                var url = '/api/getUserById';
                var userId = {
                    userId: vm.userId
                };
                return $http.post(url, userId)
                    .then(response)
                function response(res) {
                		//...
                		//...
                	    //...
                }
            return;
        }
        initProfile();
    	
    }
})();
