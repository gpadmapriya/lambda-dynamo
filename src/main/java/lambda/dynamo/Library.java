/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package lambda.dynamo;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import javax.management.Attribute;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Library {
    private DynamoDB dynamoDb;
    private String DYNAMODB_TABLE_NAME = "task";
    private Regions REGION = Regions.US_WEST_2;


    public Task save(Task task){
        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();

        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);
//        Task newTask = new Task(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getAssignee());
//
//        if (task.getId() != null){
//            Task t = ddbMapper.load(Task.class, task.getId());
//            ArrayList<History> oldHistory = t.getHistory();
//            newTask.setHistory(oldHistory);
//            newTask.addHistory();
//        }
        task.addHistory();
        ddbMapper.save(task);
        return task;
    }

    public List<Task> getallTasks() {
        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();

        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);
        List<Task> tasks = ddbMapper.scan(Task.class, new DynamoDBScanExpression());
        return tasks;
    }

    public Task updateState(Task task){
        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);

        Task t = ddbMapper.load(Task.class, task.getId());
        if (t.getStatus().equals("Assigned")) {
            t.setStatus("Accepted");
        } else if (t.getStatus().equals("Accepted")) {
            t.setStatus("Finished");
        } else if (t.getStatus().equals("Available")) {
            t.setStatus("Assigned");
        }
        t.addHistory();
        ddbMapper.save(t);
        return t;
    }

    public Task addTaskAssignee(Task task) {

        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);
        Task t = ddbMapper.load(Task.class, task.getId());

        t.setAssignee(task.getAssignee());
        t.setStatus("Assigned");
        t.addHistory();
        ddbMapper.save(t);
        return t;
    }

    public Task deleteTask(Task task) {
        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);
        Task t = ddbMapper.load(Task.class, task.getId());
        ddbMapper.delete(t);
        return t;
    }

//    public List<Task> getUserTasks(Task task){
//        HashMap<String, AttributeValue> eav = new HashMap<>();
//        //eav.put(":v1", new AttributeValue().withS(task.getAssignee()));
//        eav.put(":v1", new AttributeValue().withN(user));
//        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
//                .withFilterExpression("assignee = :v1")
//                .withExpressionAttributeValues((eav));
//
//        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
//        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);
//        List<Task> tasks = ddbMapper.scan(Task.class, scanExpression);
//        return tasks;
//    }

    public APIGatewayProxyResponseEvent getUserTasks(APIGatewayProxyRequestEvent event, Context context){


//        HashMap<String, AttributeValue> eav = new HashMap<>();
//        eav.put(":v1", new AttributeValue().withS(task.getAssignee()));
//        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
//                .withFilterExpression("assignee = :v1")
//                .withExpressionAttributeValues((eav));
//
//        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
//        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);
//        List<Task> tasks = ddbMapper.scan(Task.class, scanExpression);
//        return tasks;

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);

        response.setBody(event.toString());
        return response;
    }

    public APIGatewayProxyResponseEvent getSingleTask(APIGatewayProxyRequestEvent event){
        Map<String,String> id = event.getPathParameters();
        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);
        Task task = ddbMapper.load(Task.class,id.get("id"));
        System.out.println(task.toString());
        APIGatewayProxyResponseEvent res = new APIGatewayProxyResponseEvent();
        res.setBody(task.toString());
        return res;
    }


}

