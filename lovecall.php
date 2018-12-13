<?php
$conn = mysqli_connect('localhost', 'root', 'ghdms789', 'mp');
$myId = $_GET['ID'];
$now = $_GET['NOW'];

$query = "SELECT * FROM couple where ACK_ID = '$myId' and answer = 'not yet' and dt > '$now' order by dt";
$res = mysqli_query($conn, $query);
$result = array();
while($row = mysqli_fetch_array($res))
{ array_push($result, array('ASK_ID' => $row[0], 'MESSAGE' => $row[2])); }

echo json_encode(array("result" => $result), JSON_UNESCAPED_UNICODE);
mysqli_close($conn);

?>