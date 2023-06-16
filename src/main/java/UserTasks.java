import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UserTasks {
    static final int USER_ID = 1;
    public static void main(String[] args) throws IOException, InterruptedException {
        List<Task> allTask= HttpHello.getAllUserTasks(USER_ID);
        List<Task> openTask=allTask.stream().filter(t ->t.completed.equals("false")).collect(Collectors.toList());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(openTask));
    }
}
