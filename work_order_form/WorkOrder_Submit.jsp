<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="jquery-ui.css" />
<link rel="stylesheet" href="WorkOrder.css" />
<style type="text/css">
body{
	color:#FFFFFF;
}
</style>
<title>Work Order Submittal</title>
</head>

<body>
<%@ page import="java.io.*,java.util.*,javax.mail.*"%>
<%@ page import="javax.mail.internet.*,javax.activation.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%
Map<String, String[]> parameters = request.getParameterMap();
	String result = "";
	String professor = parameters.get("name")[0];
%>
Work Order Submitted for Professor <%=professor%>: <br />
  <%
  /*
   // BUILDING THE EMAIL
  
   String results;
   // Recipient's email ID needs to be mentioned.
   String to = "joseph.cronyn@gmail.com";

   // Sender's email ID needs to be mentioned
   String from = "joseph.cronyn@maine.edu";

   // Assuming you are sending email from localhost
   String host = "localhost";

   // Get system properties object
   Properties properties = System.getProperties();

   // Setup mail server
   properties.setProperty("mail.smtp.host", host);

   // Get the default Session object.
   Session mailSession = Session.getDefaultInstance(properties);
   
   */
   
   String today = parameters.get("todayDate")[0];
   String dueDate = parameters.get("dueDate")[0];
   
   result += "<br />Today's Date: " + today + "<br />Due Date: " + dueDate + "<br /><br />";
	
	if (parameters.containsKey("item")){
		String item = parameters.get("item")[0];
	   	String vendor = parameters.get("vendor")[0];
	   	String quantity = parameters.get("quantity")[0];
	   	String projectNumber = parameters.get("projectNum")[0];
		
		result += "<u>Purchase Request</u><br />Item: " + item + "<br />Vendor: " + vendor + "<br />Quantity: " + quantity + " <br />Project Number: " + projectNumber + "<br />";
	}
	if(parameters.containsKey("hiree")){
		String hiree = parameters.get("hiree")[0];
	   	String title = parameters.get("jobtitle")[0];
	   	String funding = parameters.get("funding")[0];

		result += "<u>Hire Request</u><br />Name of Hiree: " + hiree + "<br />Position Title: " + title + "<br />Funding Source: " + funding + "<br />";
	}
	if(parameters.containsKey("location")){
		String location = parameters.get("location")[0];
	   	String start = parameters.get("startDate")[0];
	   	String end = parameters.get("endDate")[0];
		String purpose = parameters.get("purpose")[0];
	   	String attendees = parameters.get("attendees")[0];
	   	String mealEstimate = parameters.get("mealEstimate")[0];
		String lodging = parameters.get("lodging")[0];
	   	String transport = parameters.get("transport")[0];
	   	String otherExpense = parameters.get("otherExpense")[0];
		String mealComp = parameters.get("mealComp")[0];

		result += "<u>Prepare Travel Request</u><br />Location: " + location + "<br />Start Date: " + start + "<br />End Date: " + end + "<br />Purpose: " + purpose + "<br />Attendees: " + attendees + "<br />Meal Estimate: " + mealEstimate + "<br />Lodging: " + lodging + "<br />Transport: " + transport + "<br />Other Expenses: " + otherExpense + "<br />Meal Compensation: " + mealComp + "<br />";
	}
	if(parameters.containsKey("expenseAccounts")){
		String expense = parameters.get("expenseAccounts")[0];

		result += "<u>Prepare TEV</u><br />Expense Accounts: " + expense + "<br />";
	}
	if(parameters.containsKey("otherInfo")){
		String otherInfo = parameters.get("otherInfo")[0];
	   	
		result += "<u>Other</u><br />Other Info: " + otherInfo + "<br />";
	}
	
	out.println (result);
	
	/*
	//SEND EMAIL WITH CONTENT
	
	try{
      // Create a default MimeMessage object.
      MimeMessage message = new MimeMessage(mailSession);
      // Set From: header field of the header.
      message.setFrom(new InternetAddress(from));
      // Set To: header field of the header.
      message.addRecipient(Message.RecipientType.TO,
                               new InternetAddress(to));
      // Set Subject: header field
      message.setSubject("Work Order Request from " + professor);
      // Now set the actual message
      message.setText("Work Order: " + result);
      // Send message
      Transport.send(message);
      results = "Sent message successfully....";
   }catch (MessagingException mex) {
      mex.printStackTrace();
      results = "Error: unable to send message....";
   }
   */
	%>
</body>
</html>