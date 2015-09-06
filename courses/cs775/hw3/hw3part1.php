<!DOCTYPE html>
<html>
	<head>
		<title>HW #3 - Part #1</title>
		<style type="text/css">
			body {font-family:"Courier New";}
			table {border:2px solid black; border-collapse:collapse;}
			th, td {border:2px solid black;text-align:left; vertical-align:top;}
		</style>
	</head>
	<body>
		<h1> HW #3 - Part #1 </h1>
		<p> Note:This labs assumes that the "company.sql" file has been properly imported into MySQL. </p>
		<hr />
		<h2> Section I </h2>
		<h3> Establish database connection and open "company" database. </h3>
		<?php
			//Create a connection to the MySQL server and store DB resource handle
		        $link = mysql_connect('localhost', 'smx227', 'RuacAm0');	
			
			//Check to see if connection established and exit on error
			if ($link == FALSE) {
    				die('Could not connect: ' . mysql_error());
			}
			
			//Select the database that you want to use
			mysql_select_db('smx227');
			
		?>
		<hr />
		<h2> Section II </h2>
		<h3> Formulate basic query and display output in HTML tables. </h3>
		<h4> Query: List all of the projects for department number "5". <br />
			Output columns: Project Name, Project Number, Project Location. <br />
			Output display: Utilize a TABLE to display your output, similar to the following.
		</h4>
		<table border="1">
			<tr>
				<th>Project Name</th>
				<th>Project Number</th>
				<th>Project Location</th>
			</tr>
			<tr>
				<td>Example Project</td>
				<td>123</td>
				<td>Providence</td>
			</tr>
		</table>
		<?php
			//Step #1 - Formulate your query
			$query = "SELECT * FROM PROJECT WHERE Dnum='5'";
			echo "<p> $query </p>";
			//Step #2 - Run query and store result
			$result = mysql_query($query);
			
			//Step #3 - Working with the results
			echo "<table border='1'>";
			echo 	"<tr>";
			echo		"<th>Project Name</th>";
			echo 		"<th>Project Number</th>";
			echo 		"<th>Project Location</th>";
			echo 	"</tr>";
			while($row=mysql_fetch_assoc($result)){
				echo	"<tr>";
				echo 		"<td>" . $row['Pname'] . "</td>";
				echo 		"<td>" . $row['Pnumber'] . "</td>";
				echo 		"<td>" . $row['Plocation'] . "</td>";
				echo 	"</tr>";
			}
			echo "</table>";
			
		?>
		<hr />
		
		   
		<h2> Section III </h2>
		<h3> Formulate sub-query that runs for each result row and add output to the HTML table. </h3>
		<h4> Query: List all of the projects for department number "5" and the employees associated to the project. <br />
			Output columns: Project Name, Project Number, Project Location, Employees(SSN). <br />
			Output display: Utilize a TABLE to display your output, similar to the following.
		</h4>
		<table border="1">
			<tr>
				<th>Project Name</th>
				<th>Project Number</th>
				<th>Project Location</th>
				<th>Employees (SSN)</th>
			</tr>
			<tr>
				<td>Example Project</td>
				<td>123</td>
				<td>Providence</td>
				<td>
					123456789 <br />
					234567890 <br />
				</td>
			</tr>
		</table>
		<?php
			//Step #1 - Formulate your query  (Same as Section II)
			$query = "SELECT * FROM PROJECT WHERE Dnum='5'";	
			echo "<p> $query </p>";
			//Step #2 - Run query and store result  (Same as Section II)
			$result = mysql_query($query);
			
			//Step #3 - Working with the results  (Some will be the same as Section II)

			// When you get to the last column of a row, run a new subquery.
			// Remember to use different names for your subquery and its result
			// Try using the prefix "sub" for the variables: $subquery, $subresult, $subrow
			echo "<table border='1'>";
                        echo    "<tr>";
                        echo            "<th>Project Name</th>";
                        echo            "<th>Project Number</th>";
                        echo            "<th>Project Location</th>";
			echo 		"<th>Employees (SSN)</th>";
                        echo    "</tr>";
                        while($row=mysql_fetch_assoc($result)){
                                echo    "<tr>";
                                echo            "<td>" . $row['Pname'] . "</td>";
                                echo            "<td>" . $row['Pnumber'] . "</td>";
                                echo            "<td>" . $row['Plocation'] . "</td>";
				echo 		"<td>";
				$subquery = "SELECT Ssn FROM EMPLOYEE JOIN WORKS_ON ON Ssn=Essn JOIN PROJECT ON Pno=Pnumber WHERE Pno='" . 
					$row['Pnumber'] . "' AND Plocation='" . $row['Plocation'] . "'";
				$subresult = mysql_query($subquery);
				while($subrow=mysql_fetch_assoc($subresult)){
					echo $subrow['Ssn'] . "<br />";
				}
				echo "</td>";
                                echo    "</tr>";
                        }
                        echo "</table>";

			
		?>
		<hr />
		<h2> End </h2>
		<h3> Close connection to database. </h3>
		<?php
			mysql_close($link);		
		?>
		
	</body>
</html>
