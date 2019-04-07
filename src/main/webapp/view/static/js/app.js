angular
		.module('sdpappManagerApp', [])
		.controller(
				'loginController',
				function($scope, $http) {
					$scope.file = '';
					$scope.addTask = function addTask() {
						$http
								.get(
										'/loginController/isUsedId?email=prototypeoneb@gmail.com')
								.then(function(response) {
									alert(JSON.stringify({
										response : response
									}));
								});
					};

					$scope.sendPasswordByMail = function sendPasswordByMail() {
						$http
								.get(
										'/loginController/sendPasswordByMail?email=prototypeoneb@gmail.com')
								.then(function(response) {
									alert(JSON.stringify({
										response : response
									}));
								});
					};

					$scope.listUserFiles = function listUserFiles() {
						$http
								.get(
										'/systemController/listUserFiles/prototypeoneb@gmail.com/test123/token=4sDn7')
								.then(function(response) {
									alert(JSON.stringify({
										response : response
									}));
								});
					};

					$scope.listSharedFiles = function listSharedFiles() {
						$http
								.get(
										'/systemController/listSharedFiles/prototypeoneb@gmail.com/test123/4sDn7')
								.then(function(response) {
									alert(JSON.stringify({
										response : response
									}));
								});
					};

					$scope.logout = function logout() {
						$http.post('/loginController/logout', {
							mail : 'prototypeoneb@gmail.com',
							password : 'test123'
						}).then(function(response) {
							alert(JSON.stringify({
								response : response
							}));
						});

					};

					$scope.login = function login() {
						$http.post('/loginController/login', {
							mail : 'prototypeoneb@gmail.com',
							password : 'test123'
						}).then(function(response) {
							alert(JSON.stringify({
								response : response
							}));
						});

					};

					$scope.uploadFile = function uploadFile() {
						 var file = $scope.myFile;
						alert(file);
						var formData = new FormData();
						formData.append('file', file);
						alert(formData);
						$http
								.post(
										'/systemController/addFileToUser?email=prototypeoneb@gmail.com&password=test123&token=shkZg',
										formData,
										{
											transformRequest : angular.identity,
											headers : {
												'Content-Type' : undefined
											}
										}).then(function(response) {
									alert(JSON.stringify({
										response : response
									}));
								});
					};

				});
