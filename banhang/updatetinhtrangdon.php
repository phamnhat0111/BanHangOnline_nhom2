<?php
include "connect.php";
$id = $_POST['id'];
$trangthai = $_POST['trangthai'];

// check data 
$query = 'UPDATE `donhang` SET `trangthai`= "'.$trangthai.'"  WHERE `id` = '.$id;
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

print_r(json_encode($arr));

?>