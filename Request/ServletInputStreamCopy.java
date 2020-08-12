import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

/**
 * 
 */
public class ServletInputStreamCopy extends ServletInputStream {

    private ByteArrayInputStream bais;

    public ServletInputStreamCopy(byte[] bytes) {
        this.bais = new ByteArrayInputStream(bytes);
    }

    @Override
    public boolean isFinished() {
        return bais.available() == 0;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public int read() throws IOException {
        return this.bais.read();
    }
}
