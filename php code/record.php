<?php
$conn = mysqli_connect('localhost', 'root', 'ghdms789', 'mp');
$myId = $_GET['ID'];
$now = $_GET['NOW'];

$query = "SELECT * FROM couple where (ASK_ID = '$myId' or ACK_ID = '$myId') and dt < '$now' order by dt";
$res = mysqli_query($conn, $query);
$result = array();
while($row = mysqli_fetch_array($res))
{ array_push($result, array('ASK_ID' => $row[0], 'ACK_ID' => $row[1], 'MESSAGE' => $row[2], 'ANSWER' => $row[3])); }

echo json_encode(array("result" => $result), JSON_UNESCAPED_UNICODE);
mysqli_close($conn);

?>