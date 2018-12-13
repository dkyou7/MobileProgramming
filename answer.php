<?php
$conn = mysqli_connect('localhost', 'root', 'ghdms789', 'mp');

$askid = $_GET['ASK_ID'];
$ackid = $_GET['ACK_ID'];
$msg = $_GET['MESSAGE'];
$answer = $_GET['ANSWER'];
$dt = $_GET['DT'];

$query = "UPDATE couple set ANSWER = '$answer' where ASK_ID = '$askid' and ACK_ID = '$ackid' and MESSAGE = '$msg' and ANSWER = 'not yet' and dt > '$dt'";
$res = mysqli_query($conn, $query);
$result = array();
array_push($result, array('ID' => $res));

echo json_encode(array("result" => $result), JSON_UNESCAPED_UNICODE);
mysqli_close($conn);

?>