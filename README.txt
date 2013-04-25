CloseVote|root|ttgwzmt5fz|hostname
	No return values

GET-ALL-RES|root|ttgwzmt5fz|hostname|username
	status|owner|nameOfRes|name|name|....
	or if status is closed
	status|owner|nameOfRes|numberOfvote|nameOfRes|numberOfVotes|....
	
	Example:
	open|chester|kibu|dongfang|KFC|....
	closed|chester|kibu|2|dongfang|1|KFC|3|..
	
InitDB|root|ttgwzmt5fz|hostname|username|name1|name2|name3|....
	No return values

voteRes|root|ttgwzmt5fz|hostname|restaurantName1|name2|.....
	No return values

Register|root|ttgwzmt5fz|username|password
	No return values

Login|root|ttgwzmt5fz|username|password
	1 if matched
	2 otherwise

getHosts|root|ttgwzmt5fz
	tablename|owner|status
