package io.gitee.dqcer.mcdull.business.common.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 字节数组流转换类， 将输出转换为输入流
 *
 * @author dqcer
 * @since 2023/03/08
 */
public class ByteArrayInOutConvert extends ByteArrayOutputStream {

    public ByteArrayInOutConvert() {
    }

    public ByteArrayInOutConvert(int size) {
        super(size);
    }

    public ByteArrayInputStream getInputStream() {
        ByteArrayInputStream in = new ByteArrayInputStream(this.buf, 0, this.count);
        this.buf = null;
        return in;
    }
}
