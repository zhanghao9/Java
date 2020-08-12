import org.springframework.util.FileCopyUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 用来解决HttpRequest不可重复读的问题
 * 
 */
public class CachingRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] bodyCopier;

    public CachingRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        bodyCopier = FileCopyUtils.copyToByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStreamCopy(bodyCopier);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    /**
     * 返回缓存的字节
     * @return
     */
    public byte[] getBodyCopier() {
        return this.bodyCopier;
    }

    public String getBody() throws UnsupportedEncodingException {
        return new String(this.bodyCopier,"UTF-8");
    }
}
