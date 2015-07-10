function openForm(index, section){
	
	if (! $("#" + section).html().length){
	
		$("#" + section).html(menuFill[index]);
	
	}

}

function finalSubmit(){
	
	var input = false;
	
	if (workOrder.name.value == ""){
		alert("Please choose your name");
		workOrder.name.focus();
		return false;
	}
	if (workOrder.dueDate.value == ""){
		alert("Please enter a Due Date");
		workOrder.dueDate.focus();
		return false;
	}
	
	if ($("#purchase").html().length){
		input = true;
		
		if (workOrder.item.value == ""){
			alert("Please enter an item");
			workOrder.item.focus();
			return false;
		}
		
		if (workOrder.quantity.value == ""){
			alert("Please enter a quantity");
			workOrder.quantity.focus();
			return false;
		}
		
		if (workOrder.vendor.value == ""){
			alert("Please enter a vendor");
			workOrder.vendor.focus();
			return false;
		}
		
		if (workOrder.projectNum.value == ""){
			alert("Please enter a project number");
			workOrder.projectNum.focus();
			return false;
		}
		
	}
	
	if ($("#hire").html().length){
		input = true;
		
	}
	
	if ($("#travel").html().length){
		input = true;
		
	}
	
	if ($("#TEV").html().length){
		input = true;
		
	}
	
	if ($("#other").html().length){
		input = true;
		
	}
	
	if (input == false){
		alert("Please fill out at least one section");
		return false;
	}
	
	return true;
}

$(document).ready(function () {
       var html = "";
       var  profs = [
            "",
            "Suad Alagic",
            "Bob Boothe",
	    	"David Briggs",
            "Charles Welty"
        ]
        for(var i = 0; i < profs.length; i++) {
            html += "<option value='"+ profs[i]  +"'>" + profs[i] + "</option>"
        }
		$("#names").html(html);
			
		//USES DATEPICKER FROM JQUERY UI TO SELECT AND FORMAT DATE
			
		$( "#datepicker" ).datepicker({dateFormat:"mm/dd/yy", minDate:"0"});	
		
				$( "#startDate" ).datepicker({dateFormat:"mm/dd/yy", minDate:"0"});
				
						$( "#endDate" ).datepicker({dateFormat:"mm/dd/yy", minDate:"0"});
		
		
		//SETS THE DATE FOR "TODAYS DATE" FIELD
		
		var date = new Date();
	    var day = date.getDate();
    	var month = date.getMonth() + 1;
	    var year = date.getFullYear();

    	if (month < 10) month = "0" + month;
	    if (day < 10) day = "0" + day;

    	var today = month + "/" + day + "/" + year;       
	    $("#todayDate").val(today);
	
		});
		
menuFill = ["<table align='center'><tr><td>Item Description:</td>" +
"<td><input type='text' id='item' name='item' /></td></tr><tr><td>Quantity:</td><td><input type='text' id='quantity' name='quantity' /></td>" +
"</tr><tr><td>Vendor:</td><td><input type='text' id='vendor' name='vendor' /></td></tr><tr><td>Project Number:</td><td><input type='text' id='projectNum' name='projectNum' />" +
"</td></tr></table><button onClick=\"$('#purchase').empty();\">Remove</button>",

"<table align='center'><tr><td align='right'>Name of hiree:</td>" +
"<td align='left'><input type='text' id='hiree' name='hiree' /></td></tr><tr><td align='right'>Position title:</td>" +
"<td align='left'><input type='text' id='jobtitle' name='jobtitle' /></td></tr></table><br><div align='center'><u>Funding</u>" +
"<br><label><input type='radio' name='funding' value='Student Work Study' id='funding_0'>Student Work-Study</label>" +
"<br><label><input type='radio' name='funding' value='Student Dept / Grant Funded' id='funding_1'>Student Dept/Grant Funded</label>" +
"<br><label><input type='radio' name='funding' value='Temporary Salary' id='funding_2'>Temporary Salary</label><br>" +
"<label><input type='radio' name='funding' value='Temporary Hourly' id='funding_3'>Temporary Hourly</label><br>" +
"<label><input type='radio' name='funding' value='Add Comp Salary' id='funding_4'>Add Comp Salary</label>" +
"<br><label><input type='radio' name='funding' value='Add Comp Hourly' id='funding_5'>Add Comp Hourly</label>" +
"<br><br></div><button onClick=\"$('#hire').empty();\">Remove</button>",

"<table align='center'><tr><td align='right'>Location:</td><td align='left'><input type='text' id='location' name='location' /></td>" +
"</tr><tr><td align='right'>Start Date:</td><td align='left'><input type='text' id='startDate' name='startDate' /></td></tr><tr><td align='right'>End Date:</td>" +
"<td align='left'><input type='text' id='endDate' name='endDate' /></td></tr><tr><td align='right'>Purpose:</td><td align='left'>" +
" <input type='text' id='purpose' name='purpose' /></td></tr><tr><td align='right'>Attendees:</td><td align='left'>" + 
"<input type='text' id='attendees' name='attendees' /></td><!-- NAMES OR NUMBERS? --></tr><tr>" +
"<td align='right'>Meal Estimate:</td><td align='left'><input type='text' id='mealEstimate' name='mealEstimate' /></td></tr><tr><td align='right'>Lodging:</td>" +
"<td align='left'><input type='text' id='lodging' name='lodging' /></td></tr><tr><td align='right'>Local Transport Estimate:</td>" +
"<td align='left'><input type='text' id='transport' name='transport' /></td></tr><tr><td align='right'>Other expenses & estimate:</td>" +
"<td align='left'><input type='text' id='otherExpense' name='otherExpense' /></td></tr><tr><td align='right'>How would you like your meals?</td>" +
"<td><label><input type='radio' name='mealComp' value='Per Diem' id='perDiem'>Per Diem</label><br><label>" +
"<input type='radio' name='mealComp' value='Actual' id='actual'>Actual</label></td></tr></table><br><button id=remove onClick=\"$('#travel').empty();\">Remove</button>",


"Copies of Receipts attached. (originals given to P-card	Coordinator)<br>----insert attachment here----" +
"<br>Enter explanation of expense accounts if more than one used<br><input type='text' id='expenseAccounts' name='expenseAccounts' /><br>" +
"<button id=remove onClick=\"$('#TEV').empty();\">Remove</button>",

"Description:<br><textarea id='otherInfo' name='otherInfo' rows='4' style='width:80%;'></textarea><br>" +
"<button id=remove onClick=\"$('#other').empty();\">Remove</button>"];