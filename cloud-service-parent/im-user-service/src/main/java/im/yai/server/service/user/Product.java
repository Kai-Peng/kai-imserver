package im.kai.server.service.user;

public class Product {

    public static boolean isDebugMode = true ;

    public final static boolean BOOL_AUTH_HEADER = false  ;


    public static void main(String args[]) {


        //65-90 A-Z

        //97-122 a-z
        int diff = 90 - 65 ;
        int diff2 = 122 - 97 ;
        char ch = (char)(65 + (int)(Math.random() * diff));

        char ch2 = (char)(97 + (int)(Math.random() * diff2));

        //System.out.println("ch ： " + ch +  ch2 + " , ch : " + (int)ch + " , ch2：" + (int)ch2);

        String mobile = "18719398323";
         //mobile.substring(mobile.length() - 4);
        System.out.println(String.valueOf(ch).concat(String.valueOf(ch2)).concat( "" +mobile.substring(mobile.length() - 4)));


    }


}
