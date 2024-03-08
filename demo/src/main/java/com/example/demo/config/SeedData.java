package com.example.demo.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.model.CustomUser;
import com.example.demo.service.UserService;

@DependsOnDatabaseInitialization
@Component
public class SeedData implements CommandLineRunner {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    private  String usersFilePath ;
    private  String postsFilePath ;
    private  String commentsFilePath ;

    private Boolean seedBlogData;

    Logger logger = LoggerFactory.getLogger(getClass());

    public SeedData(UserService userService, Environment env, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

        Optional<Boolean> tmp  = Optional.ofNullable(env.getProperty("replaceBlogData", Boolean.class));
        this.seedBlogData = tmp.orElse(Boolean.FALSE);
        if(seedBlogData){
            logger.info("seedBlogData setto true. Filling DB prepared blog data.");
            this.postsFilePath = env.getRequiredProperty("dataStorePostsPath");
            this.commentsFilePath = env.getRequiredProperty("dataStoreCommentsPath");
            this.usersFilePath  = env.getRequiredProperty("dataStoreUsersPath");
        }



    }

    @Override
    public void run(String... args) throws Exception {

        if ( !this.seedBlogData ){
            return;
        }

        Parser parser = new Parser();

        File usersFile = new File(usersFilePath);
        File postsFile = new File( postsFilePath);
        File commentsFile = new File(commentsFilePath);

        logger.info("parsing user data.");
        parser.parseCSVFile(usersFile, (String line) -> parser.parseUser( line));
        logger.info("parsed user data.");
        logger.info("parsing posts.");
        parser.parseCSVFile(postsFile, (String line) -> parser.parsePost(line));
        logger.info("parsed posts.");
        logger.info("parsing comments.");
        parser.parseCSVFile(commentsFile, (String line) -> parser.parseComment(line));
        logger.info("parsed comments.");

        logger.info("storing data to database.");
        // The posts and comments are added to the DB via cascading
        userService.addAllUsers(new ArrayList<>(parser.userMap.values()));
        userService.flush();
        logger.info("Done.");
    }



    class Parser {        
        
        Map<Long,CustomUser> userMap = new HashMap<>();
        Map<Long,Post> postMap = new HashMap<>();
        Map<Long,Comment> commentMap = new HashMap<>();


        private void parseCSVFile(File file, Consumer<String> lineParserFunction) throws IOException{
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);

                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = br.readLine()) != null) {
                    lineParserFunction.accept(line);
                }
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }

        }
        

        private void validateComments() {
        }

        private void validatePosts(List<Post> posts) {
        }



        private void parseComment(String line) {
            String modLine = prepareString(line);
            line = null;

            Pattern regex = Pattern.compile("\"([^\"]+)\"");
            Matcher m = regex.matcher(modLine);
            m.find();
            Long commentID = Long.parseLong(m.group().replaceAll("\"", ""));
            m.find();
            Long postID = Long.parseLong(m.group().replaceAll("\"", ""));

            m.find();
            String content = m.group().substring(1, m.group().length() -1);    

            m.find();
            String author = m.group();

            m.find();

            LocalDateTime date = LocalDateTime.parse(m.group().replace("\"", "") + "T10:15:30");

            Comment comment = new Comment();
            comment.setAuthor(removeSpecialString(author));
            comment.setContent(removeSpecialString(content));
            comment.setDate(date);
            comment.setPost(postMap.get(postID));
            postMap.get(postID).getComments().add(comment);
            commentMap.put(commentID, comment);

        }
        
        private String specialString = "|<><>*";

                /**
         * replaces left out String values with a String only containing a single space.
         * Also replaces problematic character sequences with unique sequences, which we can later easily undo.
         */
        private String prepareString(String line){
            String result = line.replace("\",,\"", "\",\" \",\"");
            return result.replace("\"\"", specialString);
        }
        /**
         * replaces all occuurrences of the special string by what is was originally.
         */
        private String removeSpecialString(String s){
            return s.replace(specialString, "\"\"");

        }
        
        /**
         * Parses a post.
         * Here there are some difficulties, as contents of posts may contain the
         * delimiter of the csv file (comma), and also quotes. To tackle this
         * we parse everything except the content, and then what is left is the content.
         * 
         * Additionally, content and title of posts may be empty, which is encoded as simply giving no value (instead of the empty string). 
         * Furthermore, the data may contain characters causing difficulties. For this reason to do some preparation on the strings
         */
        private void parsePost(String line) {
            String newLine = prepareString(line);
            line = null;

        
            Pattern regex = Pattern.compile("\"([^\"]+)\"");
            Matcher m = regex.matcher(newLine);
            m.find();
            Long postID = Long.parseLong(m.group().replaceAll("\"", ""));

            m.find();
            String title = m.group().replaceAll("\"", "");

            // not interested in author name
            m.find();
            // author ID
            m.find();
            Long authorID = Long.parseLong(m.group().replaceAll("\"", ""));
            m.find();
            // +2 serves to remove ",
            int startContent = m.end() + 2;

            String reversedLine = new StringBuilder(newLine).reverse().toString();
            regex = Pattern.compile("\"([^\"]+)\"");
            m = regex.matcher(reversedLine);
            // these are not relevant data, like how many posts the author has etc.
            m.find();
            m.find();
            m.find();
            m.find();
            m.find();
            m.find();
            m.find();
            m.find();
            // this should be the date
            m.find();
            LocalDateTime date = LocalDateTime.parse(reverse(m.group().replace("\"", "")) + "T10:15:30");

            m.find();
            // here should be the content.

            // -2 serves to remove ,"
            int contentEnd = newLine.length() - m.end() - 2;
            String content = newLine.substring(startContent, contentEnd);
            
            CustomUser author = userMap.get(authorID);
            Post post = new Post();
            post.setAuthor(author);
            post.setContent(removeSpecialString(content));
            post.setDate(date);
            post.setTitle(removeSpecialString(title));
            post.setComments(new ArrayList<>());
            postMap.put(postID, post);

            author.getPosts().add(post);
        }

        private String reverse(String s) {
            return new StringBuilder(s).reverse().toString();
        }


        private void validateUsers(List<CustomUser> users) {
        }


        public void parseUser(String line) {
            line = line.replaceAll("\"", "");
            String[] parts = line.split(",");
            long id = Long.parseLong(parts[0]);
            String username = createUserName(parts[1]);
            String mail = createMail(username);
            String password = "pw";
            CustomUser user = new CustomUser();

            user.setMail(mail);
            user.setPassword(passwordEncoder.encode(password));
            user.setUsername(username);
            user.setPosts(new ArrayList<>());
            user.setRoles(UserService.USER_ROLE);

            userMap.put(id, user);
        }

        private String createMail(String username) {
            return username + "@mail.com";
        }

        private String createUserName(String name) {
            return name.replace(" ", "_");
        }

    }

}
