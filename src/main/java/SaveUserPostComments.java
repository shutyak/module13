import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class SaveUserPostComments {
    static final int USER_ID = 1;
    public static void main(String[] args) throws IOException, InterruptedException {
        {
            List<Comment> allComment = getUserLastPostCommet(USER_ID);
            String filename = "user-"+ USER_ID +"-post-"+allComment.get(0).postId+ "-comments.json";
            PrintWriter writer= new PrintWriter(new FileWriter(filename));
            String jsonComments= HttpHello.GSON.toJson(allComment);
            writer.println(jsonComments);
            System.out.println(jsonComments);
        }
    }
    public static List<Comment> getUserLastPostCommet(int userId) throws IOException, InterruptedException {
        List<Post> allPosts=HttpHello.getAllUserPosts(userId);
        if (allPosts.size()>0) {
            int maxId = allPosts.get(allPosts.size() - 1).id;
            return HttpHello.getAllPostComments(maxId);
        }else {
            throw new IOException("userId: "+userId+" hasn`t any post");
        }
    }
}
