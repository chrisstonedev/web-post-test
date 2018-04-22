<?php
include_once("config.php");

try {
	$con = new PDO( DB_DSN, DB_USERNAME, DB_PASSWORD );
	$sql = "SELECT * FROM messages ORDER BY date";
	$result = $con->query($sql);

	$int_num_field = $result->columnCount();
	$json = array();

	//array_push($json,$sql);
	while($row = $result->fetch()){
		$arr_col = array();
		for($i=0; $i<$int_num_field; $i++){
			$arr_col[$result->getColumnMeta($i)['name']] = $row[$i];
		}
		array_push($json,$arr_col);
	}
	$con = null;
	header('Content-type: text/json');
	echo json_encode($json);
} catch ( PDOException $e ) {
	$con = null;
	return $e->getMessage();
}
