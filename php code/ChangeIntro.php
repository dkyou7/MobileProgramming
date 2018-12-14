<?php
$conn = mysqli_connect('localhost', 'root', 'ghdms789', 'mp');

$id = $_GET['ID'];
$intro = $_GET['INTRO'];

$query = "UPDATE user set INTRO = '$intro' where ID = '$id'";
$res = mysqli_query($conn, $query);
$result = array();
array_push($result, array('ID' => $res));

echo json_encode(array("result" => $result), JSON_UNESCAPED_UNICODE);
mysqli_close($conn);

?>