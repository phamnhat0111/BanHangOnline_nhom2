<?php
include "connect.php";
$email = $_POST['email'];
$pass = $_POST['pass'];
$username = $_POST['username'];
$mobile = $_POST['mobile'];
$uid = $_POST['uid'];


// check data 
$query = 'SELECT * FROM `user` WHERE `email` = "'.$email.'"';
$data = mysqli_query($conn, $query);
$numrow = mysqli_num_rows($data);


if($numrow > 0){
	$arr = [
		'success' => false,
		'message' => "Email đã tồn tại "
		
	];
}
else{
	// 
	$query = 'INSERT INTO `user`(`email`, `pass`, `username`, `mobile`,`uid`) VALUES ("'.$email.'","'.$pass.'","'.$username.'","'.$mobile.'","'.$uid.'")';
	$data = mysqli_query($conn, $query);

	if ($data==true) {
		$arr = [
			'success' => true,
			'message' => "thanh cong"
			
		];
	}else{
		$arr = [
			'success' => false,
			'message' => " khong thanh cong"
		];
	}
}	
print_r(json_encode($arr));

?>