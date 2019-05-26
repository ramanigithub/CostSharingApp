# CostSharingApp

How to Test 

Swagger: http://localhost:8080/swagger-ui.html

1. Create Members:
-------------------
Method: POST
http://localhost:8080/api/v1/members

{
    "memberId": 1,
    "memberName": "Ramani"
}

{
    "memberId": 2,
    "memberName": "Hari"
}

{
    "memberId": 3,
    "memberName": "Anjali"
}

{
    "memberId": 4,
    "memberName": "Rajamani"
}


2. Create Groups:
------------------
Method: POST
http://localhost:8080/api/v1/groups

{
	"groupId":1,
	"groupName":"TEAM-A"
}

{
	"groupId":2,
	"groupName":"TEAM-B"
}

3. Add members to Group:
---------------------------
Method: PUT
http://localhost:8080/api/v1/groups/1/members

[1,2,3,4]

http://localhost:8080/api/v1/groups/2/members

[1,2]


4. payment activities of  Groups:
---------------------------------
In the current scenario any EXTERNAL transaction is considered to spent for whole Group and any INTERNAL transaction is between 1:1

Method: POST
http://localhost:8080/api/v1/payactivities

Ramani paid for LUNCH: 1000:

{
  "activityCode": "EXTERNAL",
  "activityDescription": "Paid for Lunch",
  "amount": 1000,
  "group": {
    "groupId": 1
  },
  "member": {   
    "memberId": 1
  },
  "paymentDate": "2019-05-26T10:29:23.674Z",
  "transWithMembers": [
	   {
		 "memberId": 1
	   },
	   {
		 "memberId": 2
	   },
	   {
		 "memberId": 3
	   },
	   {
		 "memberId": 4
	   }
   ]
}

Ramani paid for Gift: 1000:

{
  "activityCode": "EXTERNAL",
  "activityDescription": "Paid for Gift",
  "amount": 1000,
  "group": {
    "groupId": 1
  },
  "member": {   
    "memberId": 1
  },
  "paymentDate": "2019-05-26T10:29:23.674Z",
  "transWithMembers": [
	   {
		 "memberId": 1
	   },
	   {
		 "memberId": 2
	   },
	   {
		 "memberId": 3
	   },
	   {
		 "memberId": 4
	   }
   ]
}

Anjali paid for Gift: 1500:

{
  "activityCode": "EXTERNAL",
  "activityDescription": "Paid for Gift",
  "amount": 1500,
  "group": {
    "groupId": 1
  },
  "member": {   
    "memberId": 3
  },
  "paymentDate": "2019-05-26T10:29:23.674Z",
  "transWithMembers": [
	   {
		 "memberId": 1
	   },
	   {
		 "memberId": 2
	   },
	   {
		 "memberId": 3
	   },
	   {
		 "memberId": 4
	   }
   ]
}

Hari Paid Ramani 200 towards his contribution

{
  "activityCode": "INTERNAL",
  "activityDescription": "Paid partially contribution",
  "amount": 200,
  "group": {
    "groupId": 1
  },
  "member": {   
    "memberId": 2
  },
  "paymentDate": "2019-05-26T10:29:23.674Z",
  "transWithMembers": [
	   {
		 "memberId": 1
	   }
   ]
}


Rajamani Paid Anjali 300 towards his contribution

{
  "activityCode": "INTERNAL",
  "activityDescription": "Paid partially contribution",
  "amount": 300,
  "group": {
    "groupId": 1
  },
  "member": {   
    "memberId": 4
  },
  "paymentDate": "2019-05-26T10:29:23.674Z",
  "transWithMembers": [
	   {
		 "memberId": 3
	   }
   ]
}

5. Fetch payment summary  for Group 1
--------------------------------------
For Member 1
http://localhost:8080//api/v1/payactivities/summary/groups/1?memberId=1

For Member 2
http://localhost:8080//api/v1/payactivities/summary/groups/1?memberId=2

For Member 3
http://localhost:8080//api/v1/payactivities/summary/groups/1?memberId=3

For Member 4
http://localhost:8080//api/v1/payactivities/summary/groups/1?memberId=4
