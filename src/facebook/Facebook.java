/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facebook;

/**
 *
 * @author haiphan
 */
import com.restfb.*;
import com.restfb.types.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
public class Facebook {

    /**
     * @param args the command line arguments
     */
    ArrayList nodeName = new ArrayList();
    ArrayList nodeID =new ArrayList();
    
    private void getFriends(String token) {
        FacebookClient facebookClient = new DefaultFacebookClient(token);
        Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class);
        for (User uservalue : myFriends.getData()) {
            nodeID.add(uservalue.getId());
            nodeName.add(uservalue.getName());
        }
        System.out.println("Count of all my friends: " + myFriends.getTotalCount());
        System.out.println("Count of my public friends: " + myFriends.getData().size());
        System.out.println(nodeID);
        System.out.println(nodeName);
    }
    
    private void getFeeds(String token) throws IOException {
        FacebookClient facebookClient = new DefaultFacebookClient(token);        
        Connection<Post> myFeed = facebookClient.fetchConnection("me/feed", Post.class);

        System.out.println("First item in my feed: " + myFeed.getData().get(0));

        // Connections support paging and are iterable
        PrintWriter writer = new PrintWriter("myfeeds.txt", "UTF-8");
        for (List<Post> myFeedConnectionPage : myFeed) {
            for (Post post : myFeedConnectionPage) {
                System.out.println("Post: " + post);
                writer.println(post.getStory());
            }
        }
        writer.close();
    }
    
    private void publishFeeds(String token) {
        // Publishing a simple message.
        // FacebookType represents any Facebook Graph Object that has an ID property.
        FacebookClient facebookClient = new DefaultFacebookClient(token);

        FacebookType publishMessageResponse = facebookClient.publish("me/feed", 
                FacebookType.class, Parameter.with("message", "hello IS333"));

        System.out.println("Published message ID: " + publishMessageResponse.getId());
    }

    private void writeData(String filename) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        //write information begin
        for (int i = 0; i < 10; i++) {
            bw.write(i + "\n");
        }
        //write information end
        bw.close();
    }

    private void readData(String filename) throws FileNotFoundException, IOException {
        ArrayList information = new ArrayList();
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        String line;
        //read information begin
        while ((line = br.readLine()) != null) {
            information.add(line);
            System.out.println(information.get(information.size() - 1));
        }
        //read information end
    }

    private void loadInformation(String filename) throws FileNotFoundException, IOException {
        ArrayList nodeName = new ArrayList();
        int[][] relationship = new int[1][1];

        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        String line;
        Boolean getNodeInformation = false;
        Boolean getRelationshipInformation = false;

        //begin read information
        while ((line = br.readLine()) != null) {
            if (line.startsWith("*")) {// change the control parameter
                if (line.startsWith("*Vertices")) {
                    getNodeInformation = true;
                    getRelationshipInformation = false;
                }
                if (line.startsWith("*Edges")) {
                    getNodeInformation = false;
                    getRelationshipInformation = true;
                    relationship =
                            new int[nodeName.size()][nodeName.size()];
                }
            } else {//update the related parameter
                if (getNodeInformation) {
                    String name =
                            line.substring(line.indexOf(" ") + 1);
                    nodeName.add(name);
                }
                if (getRelationshipInformation) {
                    String[] index = line.split(" ");
                    relationship[Integer.parseInt(index[0]) - 1]
                            [Integer.parseInt(index[1]) - 1] = 1;
                    relationship[Integer.parseInt(index[1]) - 1]
                            [Integer.parseInt(index[0]) - 1] = 1;
                }
            }
        }
        //end read information

        for (int i = 0; i < nodeName.size(); i++) {
            System.out.print("," + nodeName.get(i));
        }
        System.out.print("\n");

        for (int i = 0; i < nodeName.size(); i++) {
            System.out.print(nodeName.get(i).toString());
            for (int j = 0; j < nodeName.size(); j++) {
                System.out.print(","+relationship[i][j]);
            }
            System.out.print("\n");
        }
    }
   
   
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        //System.out.println("connecting fb...");
        Facebook tmp=new Facebook();
        tmp.getFeeds("EAAEfZCprGTVQBAP2ZCxZCGEi6ZABhNzlQzvrl29FrORIgAPST2GzDiQTKqqKNsWgYj593rzIpQpPF4XIfvHIc81i6DuT91RjiZC67IZCv5Fe9jxxSh8wxJaiZAh6cpZCDL6v9WAoYHzkhIuqmIggCR2TPADxkXzNz3U1vFO8YL79LAyFMKsoHsTDOY2mKjwzFboZD");
        
        //tmp.loadInformation("Friendship_small.net");
        
    }
    
}
