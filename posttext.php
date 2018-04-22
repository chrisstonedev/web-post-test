<?php
include_once("config.php");

if ( isset($_POST['text']) and $_POST['text'] != "" and isset($_POST['source']) and $_POST['source'] != "") {
	try {
		$con = new PDO( DB_DSN, DB_USERNAME, DB_PASSWORD );
		$con->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );
		
		$sql = "INSERT INTO messages (source,text) VALUES (:source,:text)";
		$stmt = $con->prepare( $sql );
		$stmt->bindValue( "source", $_POST['source'], PDO::PARAM_STR );
		$stmt->bindValue( "text", $_POST['text'], PDO::PARAM_STR );
		$stmt->execute();
		
		$con = null;
		header("Location: index.php");
	} catch ( PDOException $e ) {
		$con = null;
		echo $e->getMessage();
	} catch ( Exception $e) {
		$con = null;
		echo $e->getMessage();
	}
} else {
	echo 'Could not establish connection to database.';
}
