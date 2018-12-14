<?php
$conn = mysqli_connect('localhost', 'root', 'ghdms789', 'mp');

$id = $_GET['ID'];
$pw = $_GET['PW'];

$query = "SELECT * FROM USER where ID = '$id' and PASSWORD = '$pw'";
$res = mysqli_query($conn, $query);
$result = array();
$chk = "f";

while($row = mysqli_fetch_array($res))
{
$chk = "t";
array_push($result, array('ID' => $row[0], 'PASSWORD' => $row[1], 'NAME' => $row[2], 'SEX' => $row[3])); 
}

if($chk == "f") { array_push($result, array('ID' => "", 'PASSWORD' => "", 'NAME' => "", 'SEX' => "")); }

echo json_encode(array("result" => $result), JSON_UNESCAPED_UNICODE);
mysqli_close($conn);

?>