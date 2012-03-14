REPORTER is a personal reporting tool.

It wotrks with 4 different types of activities: self development, working time, extra time, team time.

Version 1.0 
	What has been done:
	1. Create a reportDB with table of users, which contains with login and password
	2. If login="newcomer" and password="newcomer" we ragistration new users
	3. If login and password entered correctly, we see empty page of new Activity WorkWithReport
	4. If login and password entered not correctly, we see a litle toast massage.

Version 2.0
	What has been done:
	1. Create a new table "allreports" in DB wich contain from fields:
		_id, login, date, typeofactivities, nameofproject, info, time 
	and Upgrade reportDB from version 1.0 to version 2.0
	2. When we logon, we see the the Activity WorkWithReport
		This activity has button "Create new report (show calendar).
		If we click this button we see a calendar and can choose date.
	3.After we choose a date we call activtiy AddNewReport.
		We input data of report and then save him into DB.
		Then we return to activity WorkWithReport