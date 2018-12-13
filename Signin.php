<?php
$conn = mysqli_connect('localhost', 'root', 'ghdms789', 'mp');

$id = $_GET['ID'];
$pw = $_GET['PW'];
$name = $_GET['NAME'];
$sex = $_GET['SEX'];
$intro = $_GET['INTRO'];

$query = "INSERT INTO USER VALUES ('$id', '$pw', '$name', '$sex', '$intro')";
$res = mysqli_query($conn, $query);
$result = array();
array_push($result, array('ID' => $res));

echo json_encode(array("result" => $result), JSON_UNESCAPED_UNICODE);
mysqli_close($conn);

?>