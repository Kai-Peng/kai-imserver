package im.kai.server.service.push.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.*;
import org.apache.commons.lang.StringUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/19 11:13
 */
public class FcmUtil {


    /**
     * 初始化sdk
     * @throws IOException
     */
    public static void initializeAppByJson(String name,String jsonFilePath,String appName) throws IOException {
        FileInputStream serviceAccount = new FileInputStream(jsonFilePath);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(String.format("https://%s.firebaseio.com/",name))
                .build();
            FirebaseApp.initializeApp(options,StringUtils.isEmpty(appName)?FirebaseApp.DEFAULT_APP_NAME:appName);


    }

    /**
     * 发送token 消息
     * @throws FirebaseMessagingException
     */
    public static void  sendTokenPayload() throws FirebaseMessagingException {
        // 此注册令牌来自客户端FCM SDKS。
        String registrationToken = "YOUR_REGISTRATION_TOKEN";

        // 参见定义消息有效载荷的文档。
        Message message = Message.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .setToken(registrationToken)
                .build();

        // 向所提供的设备发送消息
        // 注册令牌
        String response = FirebaseMessaging.getInstance().send(message,true);
        // 响应是一个消息ID字符串。
        System.out.println("Successfully sent message: " + response);

        /*
           projects/{project_id}/messages/{message_id}
           错误：FCM API
         */
    }

    /**
     * 发送单个topic消息
     * @throws FirebaseMessagingException
     */
    public static  void sendTopicPayload() throws FirebaseMessagingException {
        // 主题名称可以可选地用“/主题/”前缀。
        String topic = "highScores";

        // 参见定义消息有效载荷的文档。
        Message message = Message.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .setTopic(topic)
                .build();

        // 向订阅的主题的设备发送消息。
        String response = FirebaseMessaging.getInstance().send(message);
        //响应是一个消息ID字符串。
        System.out.println("Successfully sent message: " + response);
         /*
           projects/{project_id}/messages/{message_id}
           错误：FCM API
         */
    }

    /**
     * 发送topic组消息
     */
    public static  void sendTopicsPayload() throws FirebaseMessagingException {
        // 定义将被发送到订阅设备的条件。
        // 无论是谷歌股票还是科技产业话题
        //TODO 主题条件仅支持在每个表达式中采用最多两个运算符。使用两个以上运算符会导致错误。
        String condition = "'stock-GOOG' in topics || 'industry-tech' in topics";

        // 参见定义消息有效载荷的文档.
        Message message = Message.builder()
                .setNotification(new Notification(
                        "$GOOG up 1.43% on the day",
                        "$GOOG gained 11.80 points to close at 835.67, up 1.43% on the day."))
                .setCondition(condition)
                .build();

        // 向订阅主题的设备发送消息
        // 由所提供的条件指定。
        String response = FirebaseMessaging.getInstance().send(message);
        // 响应是一个消息ID字符串
        System.out.println("Successfully sent message: " + response);
    }

    /**
     * 测试模式发送
     */
    public static void sendTestPayload(String token) throws FirebaseMessagingException {
        // 在“干运行模式”中发送消息。
        boolean dryRun = true;
        // 参见定义消息有效载荷的文档.
        Message message = Message.builder()
                .setNotification(new Notification(
                        "day",
                        "points to close at 835.67, up 1.43% on the day."))
                .setToken(token)
                .build();
        FirebaseApp.getApps();
        String response = FirebaseMessaging.getInstance(FirebaseApp.getInstance("test")).send(message, dryRun);
        // 响应是一个消息ID字符串。
        System.out.println("Dry run successful: " + response);
    }

    /**
     * 将对应于注册令牌的设备订阅到 topic
     * 在单个请求中，最多可以为 1000 台设备订阅主题
     * 如果提供的数组所含的注册令牌超过 1000 个，
     * 则系统将无法处理此请求，并且会显示 messaging/invalid-argument 错误。
     */
    public static  void registerTopic(String topic) throws ExecutionException, InterruptedException {
        // These registration tokens come from the client FCM SDKs.
        List<String> registrationTokens = Arrays.asList(
                "YOUR_REGISTRATION_TOKEN_1",
                // ...
                "YOUR_REGISTRATION_TOKEN_n"
        );

        // 将对应于注册令牌的设备订阅到
         // topic.
        TopicManagementResponse response = FirebaseMessaging
                .getInstance(FirebaseApp.getInstance("test"))
                .subscribeToTopicAsync(registrationTokens, topic)
                .get();
        // 参见TopICMaungError参考文档
        // 对于反应内容。
        System.out.println(response.getSuccessCount() + " tokens were subscribed successfully");
    }


    /**
     * 取消topic订阅
     * 在单个请求中，最多可以为 1000 台设备退订主题
     * 如果提供的数组所含的注册令牌超过 1000 个，
     * 则系统将无法处理此请求，并且会显示 messaging/invalid-argument 错误。
     * @param topic
     */
    public static void  unRegisterTopic(String topic) throws ExecutionException, InterruptedException {
        // 这些注册令牌来自客户端FCM SDKS。
        List<String> registrationTokens = Arrays.asList(
                "YOUR_REGISTRATION_TOKEN_1",
                // ...
                "YOUR_REGISTRATION_TOKEN_n"
        );

        // 取消订阅对应于注册令牌的设备
        // the topic.
        TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopicAsync(
                registrationTokens, topic).get();
        System.out.println(response.getSuccessCount() + " tokens were unsubscribed successfully");
    }


    public static void main(String ...arges) throws IOException {

        FileInputStream fileInputStream =
                new FileInputStream("C:\\Users\\pengp\\work_space\\im\\dev\\1.0\\yai-server\\cloud-service-parent\\im-push-service\\src\\main\\resources\\yai-im-fbaaf.json");
        Map<String,Object> auth = new HashMap<>();
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(fileInputStream))
                .setProjectId("yai-im-2018")
                .build();
       FirebaseApp firebaseApp = FirebaseApp.initializeApp(options,"test");
       List<FirebaseApp> apps = FirebaseApp.getApps();
        FirebaseAuth defaultAuth = FirebaseAuth.getInstance(firebaseApp);
//        FirebaseDatabase defaultDatabase = FirebaseDatabase.getInstance(firebaseApp);
//        FirebaseDatabase database = FirebaseDatabase.getInstance(FirebaseApp.getInstance("test"));
//        DatabaseReference myRef = database.getReference("message");
//        myRef.setValue("hi", new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                databaseReference.setValueAsync("hello");
//            }
//        });
//        FirebaseDatabase defaultDatabase =myRef.getDatabase();
//        defaultDatabase.goOnline();
//        System.out.println(defaultDatabase.getApp().getName());
//        System.out.println(myRef.getKey());
//
//        try {
//            String token = defaultAuth.createCustomToken("111111");
//            defaultDatabase.getApp();
//            System.out.println(token);
//        } catch (FirebaseAuthException e) {
//            e.printStackTrace();
//        }
//        try {
//            System.out.println(defaultAuth.getUser("111111"));
//        } catch (FirebaseAuthException e) {
//            e.printStackTrace();
//        }
        for (FirebaseApp app:apps) {
            System.out.println(app.getName());
        }

//        String response = null;
//        try {
//            response = FirebaseMessaging
//                        .getInstance(FirebaseApp.getInstance("test"))
//                        .send(Message.builder()
//                                .setToken("token")
//                                .setNotification(new Notification("title","body"))
//                                .build(),true);
//        } catch (FirebaseMessagingException e) {
//            e.printStackTrace();
//        }
//        System.out.println(response);



        // The topic name can be optionally prefixed with "/topics/".
        String topic = "highScores";

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .setNotification(new Notification("tile","body"))
                .setTopic(topic)
                .build();

        // Send a message to the devices subscribed to the provided topic.
        String response2 = null;
        try {
            response2 = FirebaseMessaging.getInstance(FirebaseApp.getInstance("test"))
                    .send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + response2);
    }


}
