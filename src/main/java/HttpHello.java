import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpHello {
    final  static String baseURL= "https://jsonplaceholder.typicode.com";
    final static HttpClient CL = HttpClient.newHttpClient();
    final static Gson GSON = new Gson();
    public static void main(String[] args) throws IOException, InterruptedException {
        User user = newUser("vv");
        System.out.println(user.getId());               //m13-t1.1 new user
        user.setId(1);
        user.setUsername("john");
        user.setName("John Smith");
        var updatedUser= updateUser(user);                         //m13-t1.2 update user
        System.out.println(updatedUser.getId()+" "+updatedUser.getUsername());
        System.out.println(deleteUser(user.getId()));   //m13-t1.3 delete user
        System.out.println(getAllUser());               //m13-t1.4 get all user
        System.out.println(getUserById(1));             //m13-t1.5 get user by id
        System.out.println(getUserByName("Bret"));      //m13-t1.6 get user by name

    }
    public static List<Comment> getAllPostComments(int postId) throws IOException, InterruptedException {
        final var request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL+"/posts/"+postId+"/comments"))
                .GET()
                .build();
        final var response = CL.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode()==200){
            Type listType = new TypeToken<List<Comment>>(){}.getType();
            return GSON.fromJson(response.body(),listType);
        }else {
            throw new IOException("post id:"+postId+" server response:"+response.statusCode());
        }

    }
    public static List<Post> getAllUserPosts(int userId) throws IOException, InterruptedException {
        final var request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL+"/users/"+userId+"/posts"))
                .GET()
                .build();
        final var response = CL.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode()==200){
            Type listType = new TypeToken<List<Post>>(){}.getType();
            return GSON.fromJson(response.body(),listType);
        }else {
            throw new IOException("user id:"+userId+" server response:"+response.statusCode());
        }

    }
    public static User newUser(String username) throws IOException, InterruptedException {
        String jsonStr="{ \"username\": \""+username+"\"}";
        final var request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL+"/users/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonStr))
                .build();
        final var response = CL.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(),User.class);
    }
    public static User updateUser(User newuser) throws IOException, InterruptedException {
        String jsonStr=GSON.toJson(newuser);//"{ \"username\": \""+username+"\"}";
        final var request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL+"/users/"+newuser.getId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonStr))
                .build();
        final var response = CL.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode()==200){
            return GSON.fromJson(response.body(),User.class);
        }else {
            throw new IOException("id:"+newuser.getId()+" server response:"+response.statusCode());
        }

    }
    public static int deleteUser(int id) throws IOException, InterruptedException {
        final var request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL+"/users/"+id))
                .DELETE()
                .build();
        final var response = CL.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode();
    }
    public static User getUserByName(String name) throws IOException, InterruptedException {
        final var request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL+"/users/?username="+name))
                .GET()
                .build();
        final var response = CL.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode()==200){
            if (response.body().length()==2) throw new IOException("username:"+name+" not found");
            Type listType = new TypeToken<List<User>>(){}.getType();
            List<User> user= GSON.fromJson(response.body(),listType);
            return user.get(0);

        }else {
            throw new IOException("username:"+name+" not found. Server response:"+response.statusCode());
        }
    }
    public static User getUserById(int id) throws IOException, InterruptedException {
        final var request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL+"/users/"+id))
                .GET()
                .build();
        final var response = CL.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode()==200){
            return GSON.fromJson(response.body(),User.class);
        }else {
            throw new IOException("id:"+id+" not found");
        }
    }
    public static List<User> getAllUser() throws IOException, InterruptedException {
        final var request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL+"/users"))
                .GET()
                .build();
        final var response = CL.send(request, HttpResponse.BodyHandlers.ofString());
        Type listType = new TypeToken<List<User>>(){}.getType();
        return GSON.fromJson(response.body(),listType);
    }
}
