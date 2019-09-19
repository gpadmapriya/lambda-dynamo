### TaskMaster - Lambda with Dynamo
- Use lambda to handle database changes in real time

### Feature Tasks
- Create a lambda function in Java that can add a record to the taskmaster table
- Repeat for PUT functionality
- Keep the lambda function warm
- log out delta to cloudwatch logs
- Configure dynamo db to invoke a lambda function on database changes
- Create, update and delete should be done through API calls now
- These endpoints work with JSON data

### Routes
- GET /tasks
- POST/tasks
- PUT /tasks/{id}/state
- PUT /tasks/{id}/assign/{assignee}

[API Gateway](https://30z7g0ekcb.execute-api.us-west-2.amazonaws.com/dev/tasks)