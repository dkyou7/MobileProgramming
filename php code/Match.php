<?php
$conn = mysqli_connect('localhost', 'root', 'ghdms789', 'mp');

$askid = $_GET['ASK_ID'];
$ackid = $_GET['ACK_ID'];
$msg = $_GET['MESSAGE'];
$dt = $_GET['DT'];

$query = "INSERT INTO COUPLE VALUES ('$askid', '$ackid', '$msg', 'not yet', '$dt')";
$res = mysqli_query($conn, $query);
$result = array();
array_push($result, array('ID' => $res));

echo json_encode(array("result" => $result), JSON_UNESCAPED_UNICODE);
mysqli_close($conn);

?>