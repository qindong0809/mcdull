package io.gitee.mcdull.tools.web.service.source;

import io.gitee.mcdull.tools.web.domain.LogFileVO;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Where log files come from. Everything above this interface, the backward paging, the keyword scan and
 * the live tail, is unaware of whether the file sits on the local disk or on a remote host.
 *
 * @author dqcer
 */
public interface LogSource {

    /**
     * @return readable log files, newest first
     */
    List<LogFileVO> list();

    /**
     * @return name of the file the live tail follows
     */
    String activeFile();

    /**
     * Opens a random access handle. The caller is responsible for closing it.
     *
     * @param file file name, blank means the active file
     * @return the handle
     */
    LogReader open(String file);

    /**
     * @return charset the log files are written in
     */
    Charset charset();
}
