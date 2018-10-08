<!DOCTYPE html>
<!--
  Saleem Bhatti, https://saleem.host.cs.st-andrews.ac.uk/
  18 Sep 2018
-->
<html>
<head>
<meta http-equiv="refresh" content="2"> <!-- reload every 2 seconds -->
<title>CS2003 Net1 Practical - Daily Messages Board</title>
</head>
<body>

<h1>CS2003 Net1 Practical - Daily Messages Board</h1>

<p>
<?php
$now = date("Y-m-d H:i:s");
$today = date("Y-m-d");
$display = 20; /* controls how many messages are displayed */
echo "Now it is - " . $now . ".<br />\n";
echo "Only showing the most recent " . $display . " messages for today - " . $today . ".\n";
?>
</p>

<?php
// today's messages
$messages = array();
$todayDir = "./" . $today;
if (is_dir($todayDir))
{
  $dir = new DirectoryIterator("./" . $todayDir);
  foreach($dir as $f)
  {
    if (!$f->isDot() && !$f->isDir())
    { array_push($messages, $f->getFilename()); }
  }
  rsort($messages, SORT_STRING);
}
// all the days for which messages are available
$availableDays = array();
$dir = new DirectoryIterator("./");
foreach($dir as $f)
{
  if (!$f->isDot() && $f->isDir())
    { array_push($availableDays, $f->getFilename()); }
}
rsort($availableDays, SORT_STRING);
?>

<table border="1" cellspacing="1" cellpadding="1" width="600">
<?php
$lines = 0;
foreach ($messages as $f)
{
  // filename is the timestamp of creation of the message
  echo "<tr><td><em style='color: blue';>" . $f . "</em><br />";
  include($today . "/" . $f);
  echo "</td><tr>\n";
  $lines += 1;
  if ($lines == $display)
    { break; }
}
if ($lines == 0)
  { echo "<em style='color: red';>No messages for today, yet.</em><br />"; }
?>
</table>

<p>
<?php
echo "Total number of Messages today is " . sizeof($messages) . ".";
?>
</p>

<br />
<hr />

<p>
Days for which messages have been recorded are listed below.
</p>

<table border="0" cellspacing="1" cellpadding="1" width="200">
<?php
foreach ($availableDays as $d)
{
  // dirname is the datestamp of creation of the message dir
  echo "<tr><td><em style='color: green';>" . $d . "</em><br />";
  echo "</td><tr>\n";
}
?>
</table>

<br />
<hr />

<small>
Created by <a href="https://saleem.host.cs.st-andrews.ac.uk/">Saleem Bhatti</a>, 18 Sep 2018.
</small>

</body>
</html>
