package io.gitee.dqcer.mcdull.admin.util;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;

public class JsqlparserUtil {

    public static void main(String[] args) {
        String sql = "update  eclinical_ctms_email_send_history \n" +
                "inner join ( select h.id,e.template_type as email_type from eclinical_ctms_email_send_history h left join eclinical_ctms_email_template e on e.id = h.email_template_id ) v on v.id = eclinical_ctms_email_send_history.id \n" +
                "set eclinical_ctms_email_send_history.email_type = v.email_type;";
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            if (statement instanceof Insert) {
                System.out.println("insert");
            } else if (statement instanceof Update) {
                System.out.println("Update");
            } else if (statement instanceof Delete) {
                System.out.println("Delete");
            } else {
                System.out.println("select");
            }
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
    }
}
