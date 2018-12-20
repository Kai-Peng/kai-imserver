package im.kai.server.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URL;

/**
 * jar 文件操作类
 */
public class JarUtils {


    /**
     * statics/loclist.txt
     * @param clazz
     * @param path
     * @return
     * @throws IOException
     */
    public static String getResourceTextFile(Class<?> clazz , String path) throws IOException {

        URL url = clazz.getProtectionDomain().getCodeSource().getLocation() ;

        String str = null ;
        if(url.getPath().contains(".jar")) {

            Resource resource = new ClassPathResource(path);
            InputStream inputStream = resource.getInputStream();
            str = opInputStream(inputStream);
            inputStream.close();

        } else {
            File file = ResourceUtils.getFile("classpath:".concat(path)) ;
            FileInputStream input = new  FileInputStream(file);
            str = opInputStream(input);
            input.close();
        }

        return str ;
    }

    private static String opInputStream (InputStream inputStream) throws IOException {

        StringBuilder sb = new StringBuilder();
        try (InputStreamReader reader = new InputStreamReader(inputStream, "utf-8")) {
            char[] buffer = new char[1024 * 2];
            while (reader.ready()) {
                int size = reader.read(buffer);
                if (size > 0) {
                    sb.append(buffer, 0, size);
                } else {
                    break;
                }
            }
        }
        return sb.toString();

    }

}
