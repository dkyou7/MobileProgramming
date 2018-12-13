<?php
$conn = mysqli_connect('localhost', 'root', 'ghdms789', 'mp');

$id = $_GET['ID'];

$query = "SELECT * FROM USER where ID = '$id'";
$res = mysqli_query($conn, $query);
$result = array();
$chk = "f";

while($row = mysqli_fetch_array($res))
{
$chk = "t";
array_push($result, array('ID' => $row[0]));
}

if($chk == "f") { array_push($result, array('ID' => "")); }

echo json_encode(array("result" => $result), JSON_UNESCAPED_UNICODE);
mysqli_close($conn);

?>