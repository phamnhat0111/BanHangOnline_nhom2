<?php
include "connect.php";

if($_GET['key'] && $_GET['reset'])
{
  $email=$_GET['key'];
  $pass=$_GET['reset'];
  $query = "select email,pass from user where email ='$email' and pass = '$pass'";
  $data = mysqli_query($conn, $query);

  
  if($data==true )
  {
    ?>
    <form method="post" action="submit_new.php">
    <input type="hidden" name="email" value="<?php echo $email;?>">
    <p>Mời Bạn Nhập Mật Khẩu Mới </p>
    <input type="password" name='password'>
    <input type="submit" name="submit_password">
    </form>
    <?php
  }
}
?>