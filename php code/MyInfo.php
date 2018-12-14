<?php
$conn = mysqli_connect('localhost', 'root', 'ghdms789', 'mp');

$id = $_GET['ID'];

$query = "SELECT * FROM USER where ID = '$id'";
$res = mysqli_query($conn, $query);
$result = array();
while($row = mysqli_fetch_array($res))
{array_push($result, array('ID' => $row[0], 'NAME' => $row[2], 'SEX' => $row[3], 'INTRO' => $row[4])); }

echo json_encode(array("result" => $result), JSON_UNESCAPED_UNICODE);
mysqli_close($conn);

?>