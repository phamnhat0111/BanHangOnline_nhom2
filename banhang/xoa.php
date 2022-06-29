<?php
include "connect.php";
$id = $_POST['id'];

$query = 'DELETE FROM `sanphammoi` WHERE `id` = '.$id ;
$data = mysqli_query($conn, $query);
	
if ($data == true) {
	$arr = [
			'success' => true,
			'message' => "Xóa Thành Công"
		];
}else{
		$arr = [
			'success' => false,
			'message' => "Xóa không thành công"
		];
	}

print_r(json_encode($arr));

?>