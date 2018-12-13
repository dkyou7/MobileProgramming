<?php
$conn = mysqli_connect('localhost', 'root', 'ghdms789', 'mp');

$query = "SELECT * FROM USER";
$res = mysqli_query($conn, $query);
$result = array();
while($row = mysqli_fetch_array($res))
{array_push($result, array('ID' => $row[0], 'PASSWORD' => $row[1], 'NAME' => $row[2], 'SEX' => $row[3])); }

echo json_encode(array("result" => $result), JSON_UNESCAPED_UNICODE);
mysqli_close($conn);

?>