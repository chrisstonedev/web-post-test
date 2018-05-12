<?php
session_start();
include_once("config.php");
$message_init = new MessageArray();
$message_array = $message_init->messages;
$err = $message_init->err;
?>
<!DOCTYPE html>
<html>
<head>
	<title>Web Post Test // CJStone</title>
	<link rel="stylesheet" type="text/css" href="style.css" />
</head>
<body>
	<header id="head">
		<p>Web Post Test</p>
	</header>
	<div id="main-wrapper">
		<div id="grid-wrapper">
			<?php if (strlen($err) == 0) { ?>
				<table border="1px solid black">
					<tr>
						<th>Date/Time</th>
						<th>Source</th>
						<th>Text</th>
						<th>Delete</th>
					</tr>
				<?php for ($i = 0; $i < sizeof($message_array); $i++) { ?>
					<tr>
						<td><?php echo date("Y-m-d g:i:s A", strtotime($message_array[$i]["date"]) + (60*60*3)); ?></td>
						<td><?php echo $message_array[$i]["source"]; ?></td>
						<td><?php echo $message_array[$i]["text"]; ?></td>
						<td>
							<form name="postdelete" method="post" action="postdelete.php">
								<input type="hidden" name="id" value="<?php echo $message_array[$i]["id"]; ?>">
								<input type="submit" name="submit" value="X">
							</form>
						</td>
					</tr>
				<?php } ?>
				</table>
				<?php
			} else {
				echo "<h2 class='error'>$err</h2>";
			}
			?>
		</div>
	</div>
	<hr>
	<p>
		<form name="posttext" method="post" action="posttext.php">
			Send Text: <input type="text" name="text"><br>
			<input type="hidden" name="source" value="Web">
			<input type="submit" name="submit" value="Submit">
		</form>
	</p>
</body>
</html>