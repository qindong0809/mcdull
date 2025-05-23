package io.gitee.dqcer.mcdull.framework.base.constants;

/**
 * i18n常量
 *
 * @author dqcer
 * @since 2022/07/26
 */
@SuppressWarnings("unused")
public class I18nConstants {


    /**
     * 禁止实例化
     */
    private I18nConstants(){
        throw new AssertionError();
    }


    public static final String DATA_NEED_REFRESH = "data.need.refresh";
    public static final String DATA_NOT_EXIST = "data.not.exist";
    public static final String DATA_EXISTS = "data.exists";
    public static final String DATA_ASSOCIATED_NEED_DISASSOCIATE = "data.associated.need.disassociate";
    public static final String NAME_DUPLICATED = "name.duplicated";

    public static final String PERMISSION_DENIED = "system.permission.denied";

    public static final String SYSTEM_ERROR = "system.error";

    public static final String SYSTEM_BUSY = "system.busy";
    public static final String SYSTEM_REQUEST_TOO_FREQUENT = "system.request.too.frequent";

    public static final String DB_OPERATION_FAILED = "db.operation.failed";
    public static final String MISSING_PARAMETER = "missing.parameter";


}
