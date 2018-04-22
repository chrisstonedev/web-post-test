<?php
class MessageArray {
	public $messages;
	public $err;
	function __construct() {
		try {
			$con = new PDO( DB_DSN, DB_USERNAME, DB_PASSWORD );
			$con->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );
			$query = "SELECT * FROM messages ORDER BY date";
			$stmt = $con->prepare( $query );
			$stmt->execute();
			$result = $stmt->fetchAll();
			$this->messages = $result;
		} catch ( PDOException $e ) {
			//echo $e->getMessage();
			$this->err = "Server down. Please try again later.";
		}
	}
}