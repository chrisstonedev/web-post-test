<?php
include_once("config.php");

try {
    $con = new PDO( DB_DSN, DB_USERNAME, DB_PASSWORD );
    $con->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );
    
    $sql = "DELETE FROM messages WHERE id=:id";
    $stmt = $con->prepare( $sql );
    $stmt->bindValue( "id", $_POST['id'], PDO::PARAM_STR );
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
