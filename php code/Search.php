<?php
$conn = mysqli_connect('localhost', 'root', 'ghdms789', 'mp');

$sex = $_GET['SEX'];

$query = "SELECT * FROM USER where SEX <> '$sex'";
$res = mysqli_query($conn, $query);
$result = array();
while($row = mysqli_fetch_array($res))
{array_push($result, array('ID' => $row[0], 'INTRO' => $row[4])); }

echo json_encode(array("result" => $result), JSON_UNESCAPED_UNICODE);
mysqli_close($conn);

?>