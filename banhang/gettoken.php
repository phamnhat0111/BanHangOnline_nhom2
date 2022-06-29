<?php
include "connect.php";
$status = $_POST['status'];

if($status==1){
	$query = 'SELECT * FROM `user` WHERE `status` = '.$status;
$data = mysqli_query($conn,$query);
$result = array();
while ($row = mysqli_fetch_assoc($data)){
	$result[]=($row);
} 

if(!empty($result)){
	$arr =[
		'success' => true,
		'message' => "thành công",
		'result' => $result
	];
}else{
	$arr =[
		'success' => false,
		'message' => "không thành công",
		'result' => $result
	];
  }
}else if($status==0){
	$iduser = $_POST['iduser'];
	$query = 'SELECT * FROM `user` WHERE `id` = '.$iduser.' AND `status` = '.$status ;
$data = mysqli_query($conn,$query);
$result = array();
while ($row = mysqli_fetch_assoc($data)){
	$result[]=($row);
} 

if(!empty($result)){
	$arr =[
		'success' => true,
		'message' => "thành công",
		'result' => $result
	];
}else{
	$arr =[
		'success' => false,
		'message' => "không thành công",
		'result' => $result
	];
}
}



print_r(json_encode($arr));


?>