(function () {
    angular
        .module('webapp')
        .controller('HomeController', HomeController)
        .controller('LoginController', LoginController)
        .controller('RegisterController', RegisterController)
        .controller('SearchController', SearchController)
        .controller('userProfileController', userProfileController)
        
    function HomeController () {
        var vm = this;
        vm.name = "Test model!!!!";
        vm.search = search;
        function search(searchTerm) {
        	console.log("test");
			SearchController.search(searchTerm);
		}
    }
    
    function LoginController($http) {
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
    			return $http.post(url, user);		
    		}
    }
    
    function RegisterController($http) {
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
    			
    			return $http.post(url, user);
    		}
    		
    		
    }
    function SearchController($http, $routeParams) {
		var vm = this;
		vm.defaultView = true;
		vm.term = $routeParams['term'];
		console.log(vm.term);
		//placeholder data
		vm.movies = [{name: 'Movie1',image: "../../assets/images/Death_Note.jpg"},
			{name: 'Movie2', image: "../../assets/images/Death_Note.jpg"},
			{name: 'Movie3', image: "../../assets/images/Death_Note.jpg"}];
		vm.search = search;
		if(vm.term !== undefined && vm.term != '') {
			search(vm.term);
		}
		
		
		
		function search(searchTerm) {
			var mUrl = "api/movie/search?" + searchTerm;
			var uUrl = "api/" + searchTerm;	
			vm.defaultView = false;
			vm.hasMResults = false;
			vm.hasUResults = false;
			vm.movies = [{name: "No Results Found", image: "../../assets/images/Death_Note.jpg"}];
			vm.users = [{name: "No Results Found", image: "../../assets/images/user-photo.png"}]
			//$http.get(mUrl).then(function(response) {
			//	if(response.data != undefined) {
			//		vm.movies = response.data;
			//		vm.hasMResults = true;
			//	} 
			//});
			//$http.get(uUrl).then(function(response) {
			//	if(response.data != undefined) {
			//		vm.users = response.data;
			//		vm.hasUResults = true;
			//	} 
			//});
			
			//FOR TESTING
			vm.hasMResults = true;
			//vm.hasUResults = true;
			vm.movies[0].name = searchTerm;
			vm.users[0].name = searchTerm;
		}
	}
})();

function userProfileController($routeParams) {
	var vm = this;
	vm.userId = $routeParams['uid'];
	
}
