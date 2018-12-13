<?php
$conn = mysqli_connect('localhost', 'root', 'ghdms789', 'mp');

$query = "SELECT DISTINCT NAME FROM $_GET[FIELD] order by NAME";
$res = mysqli_query($conn, $query);
$result = array();
while($row = mysqli_fetch_array($res))
{ array_push($result, array('NAME' => $row[0])); }

echo json_encode(array("result" => $result), JSON_UNESCAPED_UNICODE);
mysqli_close($conn);

?>