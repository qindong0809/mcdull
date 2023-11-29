package io.gitee.dqcer.mcdull.mdc;

import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.mdc.client.dto.MailClientDTO;
import io.gitee.dqcer.mcdull.mdc.provider.MetaDataContentApplication;
import io.gitee.dqcer.mcdull.mdc.provider.config.mail.MailTemplate;
import io.gitee.dqcer.mcdull.mdc.provider.web.service.MailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import javax.mail.MessagingException;

@ActiveProfiles("dev")
@SpringBootTest(classes = {MetaDataContentApplication.class})
public class MailTest {

    @Resource
    private MailTemplate mailTemplate;

    @Resource
    private MailService mailService;

    @Test
    void testMailTemplateError() {
        Assertions.assertThrows(MailException.class,  () -> {
            mailTemplate.sendSimpleMail("xxx@sina.com", "test", "hello word mail");
        });
    }

    @Test
    void testHtml() throws MessagingException {
        String s = "<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge,chrome=1\" />\n" +
                "    <title>即享</title>\n" +
                "    <style type=\"text/css\">\n" +
                "        a { border: none; }\n" +
                "        img { border: none; }\n" +
                "        p { margin: 0; line-height: 1.3em; }\n" +
                "        #footer-msg a { color: #F3A836; }\n" +
                "        h1,h2,h3,h4,h5,h6 {font-size:100%;margin:0;}\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body style=\"margin: 0; padding: 0; background-color: #efefef\" bgcolor=\"#efefef\">\n" +
                "<table class=\"e-mail\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" width=\"100%\" style=\"font-family:'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun;\">\n" +
                "    <tr>\n" +
                "        <td align=\"center\" style=\"padding:25px 0; background-color: #efefef;\" bgcolor=\"#efefef\">\n" +
                "            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"margin: 0; color: #444444; font-family:'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px; border: none;background-color: #efefef; \" width=\"750\">\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <!-- #nl_content -->\n" +
                "                        <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"margin: 0; border-collapse: collapse;\" width=\"100%\">\n" +
                "                            <tr>\n" +
                "                                <td style=\"color: #444444; font-family:'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px; border: none; background-color: #efefef;\" align=\"left\">\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #efefef;\"> <!-- 每个table需要声明字体，不然win10自带mail客户端失效 -->\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"153\" style=\"vertical-align: top;\"> <!-- 不写在style中的样式尺寸不加px，不然win10自带mail客户端失效 -->\n" +
                "                                                <img src=\"http://of6ra3uic.bkt.clouddn.com/edm71208_02.png\" style=\"width: 153px;height: 59px;\" alt=\"\">\n" +
                "                                            </td>\n" +
                "                                            <td width=\"597\" style=\"vertical-align: top;padding:40px 10px 0 0;\">\n" +
                "                                                <div style=\"text-align: right;\">\n" +
                "                                                    <a style=\"font-size: 14px;color: #0090da;text-decoration: none;\" href=\"http://www.zhilinktech.com/?mail170208\" target=\"_blank\">进入官网 ></a>\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #efefef;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"750\" style=\"vertical-align: top;\">\n" +
                "                                                <div>\n" +
                "                                                    <span style=\"font-size: 12px;color: #0090da;\">为企业，连接与规划一切资源</span>\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #efefef;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"750\" style=\"vertical-align: top;padding-top:10px;\">\n" +
                "                                                <div>\n" +
                "                                                    <span style=\"font-size: 12px;\">若此电子邮件不能正常显示，请点\n" +
                "                                                        <a style=\"color: #3399ff;\" href=\"http://www.zhilinktech.com/edm7208.html?mail170208\">此处</a>\n" +
                "                                                    </span>\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #efefef;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"750\" height=\"200px\" style=\"vertical-align: top;\">\n" +
                "                                                <div style=\"width: 750px;height:200px;\"> <!-- 这里必须有div包裹，不然td实际高度大于200px，即便设置高度也不生效 -->\n" +
                "                                                    <img src=\"http://of6ra3uic.bkt.clouddn.com/edm71208_05.png\" style=\"width:750px;height:200px;\">\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"245\">\n" +
                "                                                <hr style=\"margin: 0;display: block; height: 1px; line-height: 0; width: 100%; border: none; background-color: #999999;\">\n" +
                "                                            </td>\n" +
                "                                            <td width=\"180\" style=\"padding: 30px 0;vertical-align: top;\">\n" +
                "                                                <p style=\"text-align: center;color: #0090da;\">JUSTSHARE CLOUD</p>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"245\">\n" +
                "                                                <hr style=\"margin: 0;display: block; height: 1px; line-height: 0; width: 100%; border: none; background-color: #999999;\">\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"670\" style=\"padding: 10px 0;vertical-align: top;\">\n" +
                "                                                <p style=\"margin-top: 0;font-size: 14px;color:#333;\">尊敬的用户：</p>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"670\" style=\"padding: 10px 0;vertical-align: top;\">\n" +
                "                                                <p style=\"margin-bottom:10px;text-indent:2em;font-size: 14px;color: #333;\">供应商绩效管理的成功是采购管理成功的基础，可达到保持和提升企业核心竞争力的目标； 然而尽管大多数企业已经认识到供应商绩效管理的重要性，但或受系统所限缺乏数据支持，或受人力所限无法快速进行，导致考核标准无法量化，考核周期冗长，考核结果欠缺客观性；</p>\n" +
                "                                                <p style=\"font-size: 14px;color: #333;\">对此，即享近期推出了新功能：<span style=\"font-size: 14px;font-weight: bold;color:#0090da\">即享供应商绩效管理，轻松保障采购供应活动出色完成！</span></p>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"670\" style=\"padding: 20px 0;\">\n" +
                "                                                <hr style=\"margin: 0;display: block; height: 1px; line-height: 0; width: 100%; border: none; background-color: #efefef;\">\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"315\" style=\"padding: 10px 0\">\n" +
                "                                                <table cellpadding=\"20\" cellspacing=\"0\" style=\"font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun;\">\n" +
                "                                                    <tr>\n" +
                "                                                        <td width=\"315\" align=\"center\" style=\"background-color: #efefef;vertical-align: top;-moz-border-radius:25px;-webkit-border-radius:25px;border-radius:25px;\">\n" +
                "                                                            <div style=\"width: 275px;height: 200px;border-radius: 0 0 25px 25px; -moz-border-radius: 0 0 25px 25px; -webkit-border-radius: 0 0 25px 25px;\">\n" +
                "                                                                <p style=\"margin-bottom: 20px;font-size:16px;font-weight: 600;color: #0090da;text-align:left;\">采11购交货排程</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #212121;text-align:left;\">支持企业在供应链多级交货计划协同：</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #666666;text-align:left;\">-  长周期 2〜3个月生产预测计划通知：</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #666666;text-align:left;\">-  中周期采购订单协同</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #666666;text-align:left;\">-  短周期每日交货排程协同及看板</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #212121;text-align:left;\">排程在线快速答交，自动更新排程计划</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #212121;text-align:left;\">看板指导准确物流，快速查看达成与异常</p>\n" +
                "                                                            </div>\n" +
                "                                                        </td>\n" +
                "                                                    </tr>\n" +
                "                                                </table>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"315\" style=\"padding: 10px 0\">\n" +
                "                                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun;\">\n" +
                "                                                    <tr>\n" +
                "                                                        <td width=\"315\" align=\"center\" style=\"padding: 20px;background-color: #efefef;vertical-align: top;-moz-border-radius:25px;-webkit-border-radius:25px;border-radius:25px;\">\n" +
                "                                                            <div style=\"width: 275px;height: 200px;border-radius: 0 0 25px 25px; -moz-border-radius: 0 0 25px 25px; -webkit-border-radius: 0 0 25px 25px;\">\n" +
                "                                                                <p style=\"margin-bottom: 20px;font-size:16px;font-weight: 600;color: #0090da;text-align:left;\">采购交货排程</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #212121;text-align:left;\">支持企业在供应链多级交货计划协同：</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #666666;text-align:left;\">-  长周期 2〜3个月生产预测计划通知：</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #666666;text-align:left;\">-  中周期采购订单协同</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #666666;text-align:left;\">-  短周期每日交货排程协同及看板</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #212121;text-align:left;\">排程在线快速答交，自动更新排程计划</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #212121;text-align:left;\">看板指导准确物流，快速查看达成与异常</p>\n" +
                "                                                            </div>\n" +
                "                                                        </td>\n" +
                "                                                    </tr>\n" +
                "                                                </table>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444;font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"315\" style=\"padding: 10px 0\">\n" +
                "                                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun;\">\n" +
                "                                                    <tr>\n" +
                "                                                        <td width=\"315\" align=\"center\" style=\"padding: 20px;background-color: #efefef;vertical-align: top;-moz-border-radius:25px;-webkit-border-radius:25px;border-radius:25px;\">\n" +
                "                                                            <div style=\"width: 275px;height: 200px;border-radius: 0 0 25px 25px; -moz-border-radius: 0 0 25px 25px; -webkit-border-radius: 0 0 25px 25px;\">\n" +
                "                                                                <p style=\"margin-bottom: 20px;font-size:16px;font-weight: 600;color: #0090da;text-align:left;\">采购交货排程</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #212121;text-align:left;\">支持企业在供应链多级交货计划协同：</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #666666;text-align:left;\">-  长周期 2〜3个月生产预测计划通知：</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #666666;text-align:left;\">-  中周期采购订单协同</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #666666;text-align:left;\">-  短周期每日交货排程协同及看板</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #212121;text-align:left;\">排程在线快速答交，自动更新排程计划</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #212121;text-align:left;\">看板指导准确物流，快速查看达成与异常</p>\n" +
                "                                                            </div>\n" +
                "                                                        </td>\n" +
                "                                                    </tr>\n" +
                "                                                </table>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"315\" style=\"padding: 10px 0\">\n" +
                "                                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun;\">\n" +
                "                                                    <tr>\n" +
                "                                                        <td width=\"315\" align=\"center\" style=\"padding: 20px;background-color: #efefef;vertical-align: top;-moz-border-radius:25px;-webkit-border-radius:25px;border-radius:25px;\">\n" +
                "                                                            <div style=\"width: 275px;height: 200px;border-radius: 0 0 25px 25px; -moz-border-radius: 0 0 25px 25px; -webkit-border-radius: 0 0 25px 25px;\">\n" +
                "                                                                <p style=\"margin-bottom: 20px;font-size:16px;font-weight: 600;color: #0090da;text-align:left;\">采购交货排程</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #212121;text-align:left;\">支持企业在供应链多级交货计划协同：</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #666666;text-align:left;\">-  长周期 2〜3个月生产预测计划通知：</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #666666;text-align:left;\">-  中周期采购订单协同</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #666666;text-align:left;\">-  短周期每日交货排程协同及看板</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #212121;text-align:left;\">排程在线快速答交，自动更新排程计划</p>\n" +
                "                                                                <p style=\"font-size:14px;margin: 8px 0;color: #212121;text-align:left;\">看板指导准确物流，快速查看达成与异常</p>\n" +
                "                                                            </div>\n" +
                "                                                        </td>\n" +
                "                                                    </tr>\n" +
                "                                                </table>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"670\" style=\"padding: 20px 0;\">\n" +
                "                                                <hr style=\"margin: 0;display: block; height: 1px; line-height: 0; width: 100%; border: none; background-color: #efefef;\">\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"421\" style=\"vertical-align: top;\">\n" +
                "                                                <p style=\"margin-bottom: 10px;font-size: 16px;font-weight: bold;color:#0090da;\"><span style=\"line-height: 20px;height: 20px;margin-right: 8px;font-size: 42px;color: #0090da;\">.</span>自动生成精确的供应商绩效数据</p>\n" +
                "                                                <p style=\"font-size: 14px;color: #333;\">即享供应商绩效管理后台可自动通过订单数据生成精确的供应商绩效数据，包含准时答交率、答交无差异率、准时交货率、交货良品批次率、交货良品率。从而避免手工维护统计粗略，无法准确量化，主观影响大的缺陷。</p>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"24\"></td>\n" +
                "                                            <td width=\"225\" style=\"vertical-align: top;\">\n" +
                "                                                <div style=\"width:225px; height:129px;\">\n" +
                "                                                    <img src=\"http://of6ra3uic.bkt.clouddn.com/edm71208_25.png\" style=\"width:225px; height:129px;\">\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"670\" style=\"padding: 20px 0;\">\n" +
                "                                                <hr style=\"margin: 0;display: block; height: 1px; line-height: 0; width: 100%; border: none; background-color: #efefef;\">\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"421\" style=\"vertical-align: top;\">\n" +
                "                                                <p style=\"margin-bottom: 10px;font-size: 16px;font-weight: bold;color:#0090da;\"><span style=\"line-height: 20px;height: 20px;margin-right: 8px;font-size: 42px;color: #0090da;\">.</span>自动生成精确的供应商绩效数据</p>\n" +
                "                                                <p style=\"font-size: 14px;color: #333;\">即享供应商绩效管理后台可自动通过订单数据生成精确的供应商绩效数据，包含准时答交率、答交无差异率、准时交货率、交货良品批次率、交货良品率。从而避免手工维护统计粗略，无法准确量化，主观影响大的缺陷。</p>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"24\"></td>\n" +
                "                                            <td width=\"225\" style=\"vertical-align: top;\">\n" +
                "                                                <div style=\"width:225px; height:129px;\">\n" +
                "                                                    <img src=\"http://of6ra3uic.bkt.clouddn.com/edm71208_25.png\" style=\"width:225px; height:129px;\">\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"670\" style=\"padding: 20px 0;\">\n" +
                "                                                <hr style=\"margin: 0;display: block; height: 1px; line-height: 0; width: 100%; border: none; background-color: #efefef;\">\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"421\" style=\"vertical-align: top;\">\n" +
                "                                                <p style=\"margin-bottom: 10px;font-size: 16px;font-weight: bold;color:#0090da;\"><span style=\"line-height: 20px;height: 20px;margin-right: 8px;font-size: 42px;color: #0090da;\">.</span>自动生成精确的供应商绩效数据</p>\n" +
                "                                                <p style=\"font-size: 14px;color: #333;\">即享供应商绩效管理后台可自动通过订单数据生成精确的供应商绩效数据，包含准时答交率、答交无差异率、准时交货率、交货良品批次率、交货良品率。从而避免手工维护统计粗略，无法准确量化，主观影响大的缺陷。</p>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"24\"></td>\n" +
                "                                            <td width=\"225\" style=\"vertical-align: top;\">\n" +
                "                                                <div style=\"width:225px; height:129px;\">\n" +
                "                                                    <img src=\"http://of6ra3uic.bkt.clouddn.com/edm71208_25.png\" style=\"width:225px; height:129px;\">\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"670\" style=\"padding: 20px 0;\">\n" +
                "                                                <hr style=\"margin: 0;display: block; height: 1px; line-height: 0; width: 100%; border: none; background-color: #efefef;\">\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"421\" style=\"vertical-align: top;\">\n" +
                "                                                <p style=\"margin-bottom: 10px;font-size: 16px;font-weight: bold;color:#0090da;\"><span style=\"line-height: 20px;height: 20px;margin-right: 8px;font-size: 42px;color: #0090da;\">.</span>自动生成精确的供应商绩效数据</p>\n" +
                "                                                <p style=\"font-size: 14px;color: #333;\">即享供应商绩效管理后台可自动通过订单数据生成精确的供应商绩效数据，包含准时答交率、答交无差异率、准时交货率、交货良品批次率、交货良品率。从而避免手工维护统计粗略，无法准确量化，主观影响大的缺陷。</p>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"24\"></td>\n" +
                "                                            <td width=\"225\" style=\"vertical-align: top;\">\n" +
                "                                                <div style=\"width:225px; height:129px;\">\n" +
                "                                                    <img src=\"http://of6ra3uic.bkt.clouddn.com/edm71208_25.png\" style=\"width:225px; height:129px;\">\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"670\" style=\"padding: 20px 0;\">\n" +
                "                                                <hr style=\"margin: 0;display: block; height: 1px; line-height: 0; width: 100%; border: none; background-color: #efefef;\">\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"208\"></td>\n" +
                "                                            <td align=\"center\" valign=\"middle\" width=\"152\" height=\"42\" style=\"vertical-align: top;-moz-border-radius:25px;-webkit-border-radius:25px;border-radius:25px;background-color: #0090da;\"> <!-- valign属性可以设置居中 -->\n" +
                "                                                <table cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                                                    <tr>\n" +
                "                                                        <td style=\"padding:11px;\">\n" +
                "                                                            <a href=\"https://www.justsharecloud.com/register/user?mail170208\" target=\"_blank\" style=\"text-decoration:none;border: none;\"><span style=\"color: #ffffff; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;height:20px;line-height:20px;display:inline-block;\">免费开通</span></a>\n" +
                "                                                        </td>\n" +
                "                                                    </tr>\n" +
                "                                                </table>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"37\"></td>\n" +
                "                                            <td style=\"padding: 0;\">\n" +
                "                                                <table cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                                                    <tr>\n" +
                "                                                        <td align=\"center\" valign=\"middle\" width=\"152\" height=\"42\" style=\"vertical-align: top;-moz-border-radius:25px;-webkit-border-radius:25px;border-radius:25px;background-color: #0090da;\">\n" +
                "                                                            <div style=\"width:152px;height:42px;border-radius: 0 0 25px 25px; -moz-border-radius: 0 0 25px 25px; -webkit-border-radius: 0 0 25px 25px;\">\n" +
                "                                                                <table cellpadding=\"11\" cellspacing=\"0\">\n" +
                "                                                                    <tr>\n" +
                "                                                                        <td style=\"padding:11px 0;\">\n" +
                "                                                                            <a href=\"http://www.zhilinktech.com/product.html?mail170208\" target=\"_blank\" style=\"text-decoration:none;border: none;\"><span style=\"color: #ffffff; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;height: 20px;line-height:20px;display:inline-block;\">了解更多</span></a>\n" +
                "                                                                        </td>\n" +
                "                                                                    </tr>\n" +
                "                                                                </table>\n" +
                "                                                            </div>\n" +
                "                                                        </td>\n" +
                "                                                    </tr>\n" +
                "                                                </table>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"208\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"59\"></td>\n" +
                "                                            <td width=\"670\" style=\"padding: 30px 0 10px 0;vertical-align: top;\">\n" +
                "                                                <p style=\"margin-top: 0;font-size:16px;font-weight: 600;color: #0090da;\">已发布功能</p>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"59\"></td>\n" +
                "                                            <td width=\"106\" style=\"vertical-align: top;\">\n" +
                "                                                <div style=\"width: 106px;height: 170px;\">\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-weight: 600;color: #184674;\">订单协同</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">采购订单自动发布</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">订单及在线答交</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">答交差异同步ERP</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">采购变更</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">订单可附件图纸</p>\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"25\"></td>\n" +
                "                                            <td width=\"193\" style=\"vertical-align: top;\">\n" +
                "                                                <div style=\"width: 193px;height: 170px;\">\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-weight: 600;color: #184674;\">交货协同</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">按订单交货，移动收货，条码收货</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">在线收货、退换货</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">多级计划协同：</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">- 企业通知：3个月生产计划发放</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">- 采购订单：周期物料订单</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">- 交货排程：日明细交货数量</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">- Excel导入与API集成接口</p>\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"29\"></td>\n" +
                "                                            <td width=\"110\" style=\"vertical-align: top;\">\n" +
                "                                                <div style=\"width: 110px;height: 170px;\">\n" +
                "                                                    <p style=\"margin:0 0 5px 0;font-weight: 600;color: #184674;\">供应商管理</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">跨网采购寻源</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">供应商询报价管理</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">供应商绩效管理</p>\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"48\"></td>\n" +
                "                                            <td width=\"130\" style=\"vertical-align: top;\">\n" +
                "                                                <div style=\"width: 130px;height: 170px;\">\n" +
                "                                                    <p style=\"margin:0 0 5px 0;font-weight: 600;color: #184674;\">信息协同</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">业务单据通知 PC/APP</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">企业通知全面替代邮件</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">企业内部通讯录</p>\n" +
                "                                                    <p style=\"margin: 0 0 5px 0;font-size: 12px;color: #333333;\">供应商好友列表</p>\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"50\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"670\" style=\"padding: 20px 0;\">\n" +
                "                                                <hr style=\"margin: 0;display: block; height: 1px; line-height: 0; width: 100%; border: none; background-color: #efefef;\">\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                            <td width=\"670\" style=\"padding: 20px;\">\n" +
                "                                                <p style=\"margin-top: 0;text-align: center;color: #212121;\">已经有超过<span style=\"font-size: 24px;color: #0099ff;\">5000+</span>家企业在使用以上功能，在即享云供应链提高供应链管理效率和快速获益。</p>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"32\"></td>\n" +
                "                                            <td width=\"678\" style=\"padding: 0;\">\n" +
                "                                                <div style=\"width:678px;height:102px;\">\n" +
                "                                                    <img src=\"http://of6ra3uic.bkt.clouddn.com/edm71208_84.png\" style=\"width:678px;height:102px;\">\n" +
                "                                                </div>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"40\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;background-color: #ffffff;\">\n" +
                "                                        <tr>\n" +
                "                                            <td width=\"208\"></td>\n" +
                "                                            <td style=\"padding: 30px 0 50px 0;\">\n" +
                "                                                <table cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                                                    <tr>\n" +
                "                                                        <td align=\"center\" valign=\"middle\" width=\"152\" height=\"42\" style=\"vertical-align: top;-moz-border-radius:25px;-webkit-border-radius:25px;border-radius:25px;background-color: #0090da;\"> <!-- valign属性可以设置居中 -->\n" +
                "                                                            <div style=\"width:152px;height:42px;border-radius: 0 0 25px 25px; -moz-border-radius: 0 0 25px 25px; -webkit-border-radius: 0 0 25px 25px;\">\n" +
                "                                                                <table cellpadding=\"11\" cellspacing=\"0\">\n" +
                "                                                                    <tr>\n" +
                "                                                                        <td style=\"padding:11px 0;\">\n" +
                "                                                                            <a href=\"https://www.justsharecloud.com/register/user?mail170208\" target=\"_blank\" style=\"text-decoration:none;border: none;\"><span style=\"color: #ffffff; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;height:20px;line-height:20px;display:inline-block;\">免费开通</span></a>\n" +
                "                                                                        </td>\n" +
                "                                                                    </tr>\n" +
                "                                                                </table>\n" +
                "                                                            </div>\n" +
                "                                                        </td>\n" +
                "                                                    </tr>\n" +
                "                                                </table>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"37\"></td>\n" +
                "                                            <td style=\"padding: 30px 0 50px 0;\">\n" +
                "                                                <table cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                                                    <tr>\n" +
                "                                                        <td align=\"center\" valign=\"middle\" width=\"152\" height=\"42\" style=\"vertical-align: top;-moz-border-radius:25px;-webkit-border-radius:25px;border-radius:25px;background-color: #0090da;\">\n" +
                "                                                            <div style=\"width:152px;height:42px;border-radius: 0 0 25px 25px; -moz-border-radius: 0 0 25px 25px; -webkit-border-radius: 0 0 25px 25px;\">\n" +
                "                                                                <table cellpadding=\"11\" cellspacing=\"0\">\n" +
                "                                                                    <tr>\n" +
                "                                                                        <td style=\"padding:11px 0;\">\n" +
                "                                                                            <a href=\"http://www.zhilinktech.com/product.html?mail170208\" target=\"_blank\" style=\"text-decoration:none;border: none;\"><span style=\"color: #ffffff; font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px;height: 20px;line-height:20px;display:inline-block;\">了解更多</span></a>\n" +
                "                                                                        </td>\n" +
                "                                                                    </tr>\n" +
                "                                                                </table>\n" +
                "                                                            </div>\n" +
                "                                                        </td>\n" +
                "                                                    </tr>\n" +
                "                                                </table>\n" +
                "                                            </td>\n" +
                "                                            <td width=\"208\"></td>\n" +
                "                                        </tr>\n" +
                "                                    </table>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                        </table>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "<table class=\"e-mail\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" width=\"100%\" style=\"font-family:'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun;\">\n" +
                "    <tr>\n" +
                "        <td align=\"center\" style=\"background-color: #0090da;\" bgcolor=\"#0090da\">\n" +
                "            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"margin: 0; color: #444444; font-family:'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px; border: none;background-color: #0090da; \" width=\"750\">\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <!-- #nl_content -->\n" +
                "                        <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"margin: 0; border-collapse: collapse;\" width=\"100%\">\n" +
                "                            <tr>\n" +
                "                                <td style=\"color: #444444; font-family:'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun; font-size: 14px; border: none; background-color: #0090da;\" align=\"left\">\n" +
                "                                    <div style=\"background-color: #0090da;\" bgcolor=\"#0090da\">\n" +
                "                                        <div style=\"background-color: #0090da;\">\n" +
                "                                            <table style=\"margin: 0 auto;background-color: #0090da;font-family: 'Microsoft YaHei',Arial,Helvetica,sans-serif,SimSun;\">\n" +
                "                                                <tr style=\"background-color: #0090da\">\n" +
                "                                                    <td>\n" +
                "                                                        <img src=\"http://of6ra3uic.bkt.clouddn.com/EDM20161217_21.png\" width=\"146\" height=\"211\" alt=\"\">\n" +
                "                                                    </td>\n" +
                "                                                    <td colspan=\"3\" width=\"604\" height=\"211\">\n" +
                "                                                        <div style=\"margin: 0 30px;color: #fff;\">\n" +
                "                                                            <p style=\"padding-bottom: 12px;border-bottom: 1px dashed #fff;font-size: 12px;\">扫描二维码关注智互联官微，可下载 Android 和 iOS的移动供应链APP体验版本， 还可获取更多产品资讯及在线客服</p>\n" +
                "                                                            <p style=\"margin: 2px 0;\">联系我们将安排顾问免费辅导及ERP集成服务，</p>\n" +
                "                                                            <p style=\"margin: 2px 0;\">请即刻拨打服务热线：<span style=\"font-weight: 600; font-size: 16px;\">400 602 3399</span></p>\n" +
                "                                                            <p style=\"margin: 2px 0;\">深圳市南山区蛇口南海意库2栋402（518026）</p>\n" +
                "                                                            <p style=\"margin: 2px 0;\"><span>即享网页端：<a href=\"https://www.justsharecloud.com/?mail170208\" target=\"_blank\" style=\"color: #fff;text-decoration: none;\">www.justsharecloud.com</a></span></p>\n" +
                "                                                        </div>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </table>\n" +
                "                                        </div>\n" +
                "                                    </div>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                        </table>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "<div style='display:none'> <img src='http://ossnotify.justsharecloud.com/oss/notify/api/edm'/></div>\n" +
                "</body>\n" +
                "</html>";
//        s = s.replaceAll("\\u0020", "&nbsp;");
//        s = s.replaceAll("\\u3000", "&nbsp;");
//        s = s.replaceAll("\\u00A0", "&nbsp;");
        s = "<!DOCTYPE HTML>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\">\n" +
                "    <title>Weekend Reads: Creative Computers</title>\n" +
                "    <link rel=\"image_src\" href=\"http://jet.technologyreview.com/nlt/xz44/lm7s7.png\">\n" +
                "    <style type=\"text/css\">\n" +
                "        a { border: none; }\n" +
                "        img { border: none; }\n" +
                "        p { margin: 0; line-height: 1.3em; }\n" +
                "        #footer-msg a { color: #F3A836; }\n" +
                "        h1,h2,h3,h4,h5,h6 {font-size:100%;margin:0;}\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body style=\"margin: 0; padding: 0; background-color: #eeeeee\" bgcolor=\"#eeeeee\">\n" +
                "\n" +
                "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" width=\"100%\">\n" +
                "    <tr>\n" +
                "        <td align=\"center\" style=\"padding: 37px 0; background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\n" +
                "            <div style=\"padding: 0 0 10px\">\n" +
                "                <a href=\"http://jet.technologyreview.com/nl/xz44/lm7s7.html?a=oFE8Pb&amp;b=b1e74896&amp;c=xz44&amp;d=e27f4260&amp;e=dcde04f9&amp;email=xiaogliu%40outlook.com\" style=\"line-height: 1;vertical-align: bottom;color: #000000; font-family: arial; font-size: 11px; ;border: none;\">View online version</a>\n" +
                "            </div>\n" +
                "            <!-- #nl_container -->\n" +
                "            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"margin: 0; border: 1px solid #dddddd; color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \" width=\"600\"><tr>\n" +
                "                <td>\n" +
                "                    <!-- #nl_header -->\n" +
                "                    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
                "                        <tr>\n" +
                "                            <td></td>\n" +
                "                        </tr>\n" +
                "                    </table>\n" +
                "                    <!-- #nl_content -->\n" +
                "                    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"margin: 0; border-collapse: collapse;\" width=\"100%\">\n" +
                "                        <tr>\n" +
                "                            <td style=\"color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff;  padding: 5px 0;\" align=\"left\">\n" +
                "                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \"><tr>\n" +
                "                                    <td width=\"10px\"></td>\n" +
                "                                    <td width=\"580\" style=\"vertical-align: top; padding: 5px 0; \">\n" +
                "                                        <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" style=\"color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                            <tr>\n" +
                "                                                <td style=\"vertical-align: top; padding: 5px; text-align: center;\" width=\"185\">\n" +
                "                                                    <a href=\"http://www.technologyreview.com\" style=\"border: none;\">\n" +
                "                                                        <img alt=\"\" width=\"173\" height=\"84\" style=\"border: none;\" src=\"http://jet.technologyreview.com/img/xz44/1yx8k/s11p9.jpg\">\n" +
                "                                                    </a>\n" +
                "                                                </td>\n" +
                "                                                <td width=\"10\"></td>\n" +
                "                                                <td style=\"vertical-align: top; padding: 5px;\">\n" +
                "                                                    <div style=\"text-align: right;\"><span style=\"line-height: 15.6px; text-align: right; font-family: arial, helvetica, sans-serif; font-size: 14px;\">December 3, 2016</span></div>\n" +
                "                                                    <div style=\"text-align: right;\"><span style=\"line-height: 15.6px; text-align: right; font-family: arial, helvetica, sans-serif; font-size: 20px;\">Weekend Reads:<br><span style=\"font-size:18px;\">Creative Computers</span></span></div>\n" +
                "                                                </td>\n" +
                "                                            </tr>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                </table>\n" +
                "                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                    <tr>\n" +
                "                                        <td width=\"10px\"></td>\n" +
                "                                        <td width=\"580\" style=\"vertical-align: top; padding: 5px 0; \">\n" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;width:565px;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \" width=\"565\">\n" +
                "                                                <tr>\n" +
                "                                                    <td style=\"padding:5px 0 5px 5px; line-height:normal;\">\n" +
                "                                                        <hr style=\"margin: 0;display: block; height: 1px; line-height: 0; width: 100%; border: none; background-color: #444444; \">\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </table>\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                </table>\n" +
                "                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                    <tr>\n" +
                "                                        <td width=\"10px\"></td>\n" +
                "                                        <td width=\"580\" style=\"vertical-align: top; padding: 5px 0; \">\n" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;width:565px;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \" width=\"565\">\n" +
                "                                                <tr>\n" +
                "                                                    <td style=\"padding:5px 0 5px 5px;line-height:normal;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                                        <p style=\"margin: 0 0 10px;line-height: 1.3em;\">\n" +
                "                                                            <span style=\"font-size:14px;\">\n" +
                "                                                                <span style=\"font-family:arial,helvetica,sans-serif;\">This week\n" +
                "                                                                    <a target=\"_blank\" href=\"https://www.technologyreview.com/s/603003/ai-songsmith-cranks-out-surprisingly-catchy-tunes/\" style=\"color: #0033cc; ;border: none;\">\n" +
                "                                                                        <font color=\"#0033cc\">we reported on Mag</font>\n" +
                "                                                                        <font color=\"#0033cc\">enta</font>\n" +
                "                                                                    </a>, an effort at Google to get computers to write music. The project has made remarkable progress after gaining an understanding of music theory. But there still is a long way to go before computers can be said to be truly creative. And that matters because it goes to the heart of what artificial intelligence is—or what it could be. As AI and robotics editor Will Knight wrote last year, some computer scientists have argued that “feats of creativity, whether in painting, writing, music, or some other field, are much harder to fake and are fundamental to intelligence.”\n" +
                "                                                                </span>\n" +
                "                                                            </span>\n" +
                "                                                        </p>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </table>\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                </table>\n" +
                "                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                    <tr>\n" +
                "                                        <td width=\"10px\"></td>\n" +
                "                                        <td width=\"580\" style=\"vertical-align: top; padding: 5px 0; \">\n" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;width:565px;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \" width=\"565\">\n" +
                "                                                <tr>\n" +
                "                                                    <td style=\"padding:5px 0 5px 5px; line-height:normal;\">\n" +
                "                                                        <hr style=\"margin: 0;display: block; height: 1px; line-height: 0; width: 100%; border: none; background-color: #444444; \">\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </table>\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                </table>\n" +
                "                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                    <tr>\n" +
                "                                        <td width=\"10px\"></td>\n" +
                "                                        <td width=\"580\" style=\"vertical-align: top; padding: 5px 0; \">\n" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" style=\"color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                                <tr>\n" +
                "                                                    <td style=\"vertical-align: top; padding: 5px; text-align: center;\" width=\"284\">\n" +
                "                                                        <img alt=\"\" width=\"273\" height=\"155\" style=\"border: none;\" src=\"http://jet.technologyreview.com/img/xz44/lm7s7/soslx.jpg\">\n" +
                "                                                    </td>\n" +
                "                                                    <td width=\"10\"></td>\n" +
                "                                                    <td style=\"vertical-align: top; padding: 5px;\">\n" +
                "                                                        <p style=\"margin: 0 0 10px;line-height: 1.3em;\"><span style=\"font-size:14px;\"><span style=\"font-family:arial,helvetica,sans-serif;\"><a href=\"https://www.technologyreview.com/s/601642/ok-computer-write-me-a-song/\" target=\"_blank\" style=\"color: #0033cc; ;border: none;\">OK Computer, Write Me a Song</a><br style=\"font-family: arial, helvetica, sans-serif; font-size: 14px;\">When it first introduced the Magenta project, Google said it could help musicians, architects, and other artists rather than replace them.</span></span></p>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </table>\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                </table>\n" +
                "                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                    <tr>\n" +
                "                                        <td width=\"10px\"></td>\n" +
                "                                        <td width=\"580\" style=\"vertical-align: top; padding: 5px 0; \">\n" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;width:565px;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \" width=\"565\">\n" +
                "                                                <tr>\n" +
                "                                                    <td style=\"padding:5px 0 5px 5px; line-height:normal;\">\n" +
                "                                                        <hr style=\"margin: 0;display: block; height: 1px; line-height: 0; width: 100%; border: none; background-color: #444444; \">\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </table>\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                </table>\n" +
                "                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \"><tr>\n" +
                "                                    <td width=\"10px\"></td>\n" +
                "                                    <td width=\"580\" style=\"vertical-align: top; padding: 5px 0; \">\n" +
                "                                        <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" style=\"color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                            <tr>\n" +
                "                                                <td style=\"vertical-align: top; padding: 5px; text-align: center;\" width=\"284\">\n" +
                "                                                    <img alt=\"\" width=\"273\" height=\"155\" style=\"border: none;\" src=\"http://jet.technologyreview.com/img/xz44/lm7s7/sos1k.jpg\">\n" +
                "                                                </td>\n" +
                "                                                <td width=\"10\"></td>\n" +
                "                                                <td style=\"vertical-align: top; padding: 5px;\">\n" +
                "                                                    <p style=\"margin: 0 0 10px;line-height: 1.3em;\"><span style=\"font-size:14px;\"><span style=\"font-family:arial,helvetica,sans-serif;\"><a href=\"https://www.technologyreview.com/s/600762/robot-art-raises-questions-about-human-creativity/\" target=\"_blank\" style=\"color: #0033cc; ;border: none;\">Robot Art Raises Questions about Human Creativity</a><br>Can machines ever do things that can truly be described as creative or imaginative?</span></span><br></p>\n" +
                "                                                </td>\n" +
                "                                            </tr>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                </table>\n" +
                "                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                    <tr>\n" +
                "                                        <td width=\"10px\"></td>\n" +
                "                                        <td width=\"580\" style=\"vertical-align: top; padding: 5px 0; \">\n" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;width:565px;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \" width=\"565\">\n" +
                "                                                <tr>\n" +
                "                                                    <td style=\"padding:5px 0 5px 5px; line-height:normal;\">\n" +
                "                                                        <hr style=\"margin: 0;display: block; height: 1px; line-height: 0; width: 100%; border: none; background-color: #444444; \">\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </table>\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                </table>\n" +
                "                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                    <tr>\n" +
                "                                        <td width=\"10px\"></td>\n" +
                "                                        <td width=\"580\" style=\"vertical-align: top; padding: 5px 0; \">\n" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" style=\"color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                                <tr>\n" +
                "                                                    <td style=\"vertical-align: top; padding: 5px; text-align: center;\" width=\"185\">\n" +
                "                                                        <img alt=\"\" width=\"173\" height=\"226\" style=\"border: none;\" src=\"http://jet.technologyreview.com/img/xz44/lm7s7/sos1n.jpg\">\n" +
                "                                                    </td>\n" +
                "                                                    <td width=\"10\"></td>\n" +
                "                                                    <td style=\"vertical-align: top; padding: 5px;\">\n" +
                "                                                        <p style=\"margin: 0 0 10px;line-height: 1.3em;\"><span style=\"font-size:14px;\"><span style=\"font-family:arial,helvetica,sans-serif;\"><a href=\"https://www.technologyreview.com/s/535391/rewriting-the-rules-of-turings-imitation-game/\" target=\"_blank\" style=\"color: #0033cc; ;border: none;\">Rewriting the Rules of Turing’s Imitation Game</a><br>Why the Lovelace test—a measure of creativity—could supplant the Turing test as a benchmark for AI. </span></span><br></p>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </table>\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                </table>\n" +
                "                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                    <tr>\n" +
                "                                        <td width=\"10px\"></td>\n" +
                "                                        <td width=\"580\" style=\"vertical-align: top; padding: 5px 0; \">\n" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;width:565px;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \" width=\"565\">\n" +
                "                                                <tr>\n" +
                "                                                    <td style=\"padding:5px 0 5px 5px; line-height:normal;\">\n" +
                "                                                        <hr style=\"margin: 0;display: block; height: 1px; line-height: 0; width: 100%; border: none; background-color: #444444; \">\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </table>\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                </table>\n" +
                "                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                    <tr>\n" +
                "                                        <td width=\"10px\"></td>\n" +
                "                                        <td width=\"580\" style=\"vertical-align: top; padding: 5px 0; \">\n" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" style=\"color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                                <tr>\n" +
                "                                                    <td style=\"vertical-align: top; padding: 5px; text-align: center;\" width=\"284\">\n" +
                "                                                        <img alt=\"\" width=\"273\" height=\"155\" style=\"border: none;\" src=\"http://jet.technologyreview.com/img/xz44/lm7s7/sos1r.jpg\">\n" +
                "                                                    </td>\n" +
                "                                                    <td width=\"10\"></td>\n" +
                "                                                    <td style=\"vertical-align: top; padding: 5px;\">\n" +
                "                                                        <p style=\"margin: 0 0 10px;line-height: 1.3em;\"><span style=\"font-size:14px;\"><span style=\"font-family:arial,helvetica,sans-serif;\"><a href=\"https://www.technologyreview.com/s/541471/the-hit-charade/\" target=\"_blank\" style=\"color: #0033cc; ;border: none;\">The Hit Charade</a><br>There is still no algorithm that can account for human taste.</span></span><br></p>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </table>\n" +
                "                                        </td>\n" +
                "                                    </tr></table>\n" +
                "                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                    <tr>\n" +
                "                                        <td width=\"10px\"></td>\n" +
                "                                        <td width=\"580\" style=\"vertical-align: top; padding: 5px 0; \">\n" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;width:565px;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \" width=\"565\">\n" +
                "                                                <tr>\n" +
                "                                                    <td style=\"padding:5px 0 5px 5px; line-height:normal;\">\n" +
                "                                                        <hr style=\"margin: 0;display: block; height: 1px; line-height: 0; width: 100%; border: none; background-color: #444444; \">\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </table>\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                </table>\n" +
                "                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                    <tr>\n" +
                "                                        <td width=\"10px\"></td>\n" +
                "                                        <td width=\"580\" style=\"vertical-align: top; padding: 5px 0; \">\n" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" style=\"color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                                <tr>\n" +
                "                                                    <td style=\"vertical-align: top; padding: 5px; text-align: center;\" width=\"185\">\n" +
                "                                                        <img alt=\"\" width=\"173\" height=\"226\" style=\"border: none;\" src=\"http://jet.technologyreview.com/img/xz44/lm7s7/sos17.jpg\">\n" +
                "                                                    </td>\n" +
                "                                                    <td width=\"10\"></td>\n" +
                "                                                    <td style=\"vertical-align: top; padding: 5px;\">\n" +
                "                                                        <p style=\"margin: 0 0 10px;line-height: 1.3em;\"><span style=\"font-size:14px;\"><span style=\"font-family:arial,helvetica,sans-serif;\"><a href=\"https://www.technologyreview.com/s/408171/artificial-intelligence-is-lost-in-the-woods/\" target=\"_blank\" style=\"color: #0033cc; ;border: none;\">Artificial Intelligence Is Lost in the Woods</a><br>If a machine could ever be truly creative, perhaps it could be said to have a conscious mind. But achieving that in software alone is impossible, computer scientist David Gelernter argued in 2007.</span></span><br></p>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </table>\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                </table>\n" +
                "                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                    <tr>\n" +
                "                                        <td width=\"10px\"></td>\n" +
                "                                        <td width=\"580\" style=\"vertical-align: top; padding: 5px 0; \">\n" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;width:565px;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \" width=\"565\">\n" +
                "                                                <tr>\n" +
                "                                                    <td style=\"padding:5px 0 5px 5px; line-height:normal;\">\n" +
                "                                                        <hr style=\"margin: 0;display: block; height: 1px; line-height: 0; width: 100%; border: none; background-color: #444444; \">\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </table>\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                </table>\n" +
                "                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                    <tr>\n" +
                "                                        <td width=\"10px\"></td>\n" +
                "                                        <td width=\"580\" style=\"vertical-align: top; padding: 5px 0; \">\n" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;width:565px;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \" width=\"565\">\n" +
                "                                                <tr>\n" +
                "                                                    <td style=\"padding:5px 0 5px 5px;line-height:normal;\">\n" +
                "                                                        <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
                "                                                            <tr>\n" +
                "                                                                <td>\n" +
                "                                                                    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"left\">\n" +
                "                                                                        <tr>\n" +
                "                                                                            <td>\n" +
                "                                                                                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"\">\n" +
                "                                                                                    <tbody>\n" +
                "                                                                                    <tr>\n" +
                "                                                                                        <td align=\"center\" valign=\"middle\" bgcolor=\"\" background=\"http://www.mailjet.com/images/theme/v1/bg/bg_nl_bt_top_gradient.png\" style=\"background:url(http://www.mailjet.com/images/theme/v1/bg/bg_nl_bt_top_gradient.png) repeat-x scroll 100% 0 transparent;background-repeat: repeat-x; -moz-border-radius:25px;-webkit-border-radius:25px;border-radius:25px;border: 1px solid;background-color: #246f93; border-color: #ffffff; \">\n" +
                "                                                                                            <div style=\"padding:0;padding-left: 5px;padding-right: 5px;background: transparent url(http://www.mailjet.com/images/theme/v1/bg/bg_nl_bt_bottom_gradient.png) bottom left repeat-x; background-repeat: repeat-x; border-radius: 0 0 25px 25px; -moz-border-radius: 0 0 25px 25px; -webkit-border-radius: 0 0 25px 25px;\">\n" +
                "                                                                                                <table cellpadding=\"12\" cellspacing=\"1\">\n" +
                "                                                                                                    <tr>\n" +
                "                                                                                                        <td>\n" +
                "                                                                                                            <a href=\"http://www.technologyreview.com/\" style=\"text-decoration:none;border: none;\"><span style=\"color: #ffffff; font-family: arial; font-size: 14px; ;white-space:nowrap;display:block;\">SEE MORE STORIES &gt;&gt;</span></a>\n" +
                "                                                                                                        </td>\n" +
                "                                                                                                    </tr>\n" +
                "                                                                                                </table>\n" +
                "                                                                                            </div>\n" +
                "                                                                                        </td>\n" +
                "                                                                                    </tr>\n" +
                "                                                                                    </tbody>\n" +
                "                                                                                </table>\n" +
                "                                                                            </td>\n" +
                "                                                                        </tr>\n" +
                "                                                                    </table>\n" +
                "                                                                </td>\n" +
                "                                                            </tr>\n" +
                "                                                        </table>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </table>\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                </table>\n" +
                "                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                    <tr>\n" +
                "                                        <td width=\"10px\">\n" +
                "\n" +
                "                                        </td>\n" +
                "                                        <td width=\"580\" style=\"vertical-align: top; padding: 5px 0; \">\n" +
                "                                            <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;width:565px;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \" width=\"565\">\n" +
                "                                                <tr>\n" +
                "                                                    <td style=\"padding:5px 0 5px 5px; line-height:normal;\">\n" +
                "                                                        <hr style=\"margin: 0;display: block; height: 1px; line-height: 0; width: 100%; border: none; background-color: #444444; \">\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                            </table>\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                </table>\n" +
                "                                <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \"><tr>\n" +
                "                                    <td width=\"10px\"></td>\n" +
                "                                    <td width=\"580\" style=\"vertical-align: top; padding: 5px 0; \">\n" +
                "                                        <table cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;width:565px;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \" width=\"565\">\n" +
                "                                            <tr>\n" +
                "                                                <td style=\"padding:5px 0 5px 5px;line-height:normal;color: #444444; font-family: arial; font-size: 12px; border-color: #dddddd; background-color: #ffffff; \">\n" +
                "                                                    <p style=\"margin: 0 0 10px;line-height: 1.3em;\"><span style=\"font-size:14px;\"><span style=\"color: rgb(17, 17, 17); line-height: 16.1px; font-family: arial, helvetica, sans-serif; background-color: rgb(250, 251, 251);\">If you would like to update the frequency or type of newsletters received, please <a href=\"http://newsletters.technologyreview.com/lookup/\" style=\"color: #0033cc; ;border: none;\">edit your preferences</a> or <a href=\"http://newsletters.technologyreview.com/lookup/\" style=\"color: #0033cc; ;border: none;\">unsubscribe from all MIT Technology Review communications</a>.<br><br>MIT Technology Review</span><br><span style=\"color: rgb(17, 17, 17); line-height: 16.1px; font-family: arial, helvetica, sans-serif; background-color: rgb(250, 251, 251);\">One Main Street</span><br><span style=\"color: rgb(17, 17, 17); line-height: 16.1px; font-family: arial, helvetica, sans-serif; background-color: rgb(250, 251, 251);\">Cambridge, MA 02142</span></span></p>\n" +
                "                                                </td>\n" +
                "                                            </tr>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                </table>\n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                    </table>\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "            </table>\n" +
                "            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"10\" style=\"height: 10px;border-collapse: collapse;font-size: 1px;\">\n" +
                "                <tr>\n" +
                "                    <td height=\"10\" style=\"height:10px; border-spacing: 0;font-size: 1px;\"> </td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "            <table cellpadding=\"10\" cellspacing=\"0\" border=\"0\" style=\"margin: 0; border-collapse: collapse;\" width=\"600\"><tr>\n" +
                "                <td style=\"color: #444444; background-color: #eeeeee; border-color: #eeeeee; align: left; font-family: arial; font-size: 8px; \">\n" +
                "                    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 100%; margin: 0; border-collapse: collapse;\" width=\"100%\"><tr>\n" +
                "                        <td align=\"left\" valign=\"top\" style=\"vertical-align: top; text-align: left; color: #444444; background-color: #eeeeee; border-color: #eeeeee; align: left; font-family: arial; font-size: 8px; \">\n" +
                "                            <div id=\"nl_footer\" style=\"display: block; text-align: left; margin: 0;\">This email has been sent to <a href=\"mailto:xiaogliu@outlook.com\" style=\"color:#000000;border: none;\">xiaogliu@outlook.com</a>, <a href=\"http://jet.technologyreview.com/unsub?hl=en&amp;a=oFE8Pb&amp;b=b1e74896&amp;c=xz44&amp;d=e27f4260&amp;e=dcde04f9&amp;email=xiaogliu%40outlook.com\" style=\"color:#000000;border: none;\">click here to unsubscribe</a>.</div>\n" +
                "                        </td>\n" +
                "                        <td align=\"right\" valign=\"top\" style=\"vertical-align: top; text-align: right\"></td>\n" +
                "                    </tr>\n" +
                "                    </table>\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "            </table>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>";
        s = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><!--[if IE]><html xmlns=\"http://www.w3.org/1999/xhtml\" class=\"ie\"><![endif]--><!--[if !IE]><!--><html style=\"margin: 0;padding: 0;\" xmlns=\"http://www.w3.org/1999/xhtml\"><!--<![endif]--><head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                "    <title></title>\n" +
                "    <!--[if !mso]><!--><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><!--<![endif]-->\n" +
                "    <meta name=\"viewport\" content=\"width=device-width\"><style type=\"text/css\">\n" +
                "@media only screen and (min-width: 620px){.wrapper{min-width:600px !important}.wrapper h1{}.wrapper h1{font-size:26px !important;line-height:34px !important}.wrapper h2{}.wrapper h2{font-size:20px !important;line-height:28px !important}.wrapper h3{}.column{}.wrapper .size-8{font-size:8px !important;line-height:14px !important}.wrapper .size-9{font-size:9px !important;line-height:16px !important}.wrapper .size-10{font-size:10px !important;line-height:18px !important}.wrapper .size-11{font-size:11px !important;line-height:19px !important}.wrapper .size-12{font-size:12px !important;line-height:19px !important}.wrapper .size-13{font-size:13px !important;line-height:21px !important}.wrapper .size-14{font-size:14px !important;line-height:21px !important}.wrapper .size-15{font-size:15px !important;line-height:23px !important}.wrapper .size-16{font-size:16px !important;line-height:24px \n" +
                "!important}.wrapper .size-17{font-size:17px !important;line-height:26px !important}.wrapper .size-18{font-size:18px !important;line-height:26px !important}.wrapper .size-20{font-size:20px !important;line-height:28px !important}.wrapper .size-22{font-size:22px !important;line-height:31px !important}.wrapper .size-24{font-size:24px !important;line-height:32px !important}.wrapper .size-26{font-size:26px !important;line-height:34px !important}.wrapper .size-28{font-size:28px !important;line-height:36px !important}.wrapper .size-30{font-size:30px !important;line-height:38px !important}.wrapper .size-32{font-size:32px !important;line-height:40px !important}.wrapper .size-34{font-size:34px !important;line-height:43px !important}.wrapper .size-36{font-size:36px !important;line-height:43px !important}.wrapper .size-40{font-size:40px !important;line-height:47px !important}.wrapper \n" +
                ".size-44{font-size:44px !important;line-height:50px !important}.wrapper .size-48{font-size:48px !important;line-height:54px !important}.wrapper .size-56{font-size:56px !important;line-height:60px !important}.wrapper .size-64{font-size:64px !important;line-height:63px !important}}\n" +
                "</style>\n" +
                "    <style type=\"text/css\">\n" +
                "body {\n" +
                "  margin: 0;\n" +
                "  padding: 0;\n" +
                "}\n" +
                "table {\n" +
                "  border-collapse: collapse;\n" +
                "  table-layout: fixed;\n" +
                "}\n" +
                "* {\n" +
                "  line-height: inherit;\n" +
                "}\n" +
                "[x-apple-data-detectors],\n" +
                "[href^=\"tel\"],\n" +
                "[href^=\"sms\"] {\n" +
                "  color: inherit !important;\n" +
                "  text-decoration: none !important;\n" +
                "}\n" +
                ".wrapper .footer__share-button a:hover,\n" +
                ".wrapper .footer__share-button a:focus {\n" +
                "  color: #ffffff !important;\n" +
                "}\n" +
                ".btn a:hover,\n" +
                ".btn a:focus,\n" +
                ".footer__share-button a:hover,\n" +
                ".footer__share-button a:focus,\n" +
                ".email-footer__links a:hover,\n" +
                ".email-footer__links a:focus {\n" +
                "  opacity: 0.8;\n" +
                "}\n" +
                ".preheader,\n" +
                ".header,\n" +
                ".layout,\n" +
                ".column {\n" +
                "  transition: width 0.25s ease-in-out, max-width 0.25s ease-in-out;\n" +
                "}\n" +
                ".layout,\n" +
                "div.header {\n" +
                "  max-width: 400px !important;\n" +
                "  -fallback-width: 95% !important;\n" +
                "  width: calc(100% - 20px) !important;\n" +
                "}\n" +
                "div.preheader {\n" +
                "  max-width: 360px !important;\n" +
                "  -fallback-width: 90% !important;\n" +
                "  width: calc(100% - 60px) !important;\n" +
                "}\n" +
                ".snippet,\n" +
                ".webversion {\n" +
                "  Float: none !important;\n" +
                "}\n" +
                ".column {\n" +
                "  max-width: 400px !important;\n" +
                "  width: 100% !important;\n" +
                "}\n" +
                ".fixed-width.has-border {\n" +
                "  max-width: 402px !important;\n" +
                "}\n" +
                ".fixed-width.has-border .layout__inner {\n" +
                "  box-sizing: border-box;\n" +
                "}\n" +
                ".snippet,\n" +
                ".webversion {\n" +
                "  width: 50% !important;\n" +
                "}\n" +
                ".ie .btn {\n" +
                "  width: 100%;\n" +
                "}\n" +
                "[owa] .column div,\n" +
                "[owa] .column button {\n" +
                "  display: block !important;\n" +
                "}\n" +
                ".ie .column,\n" +
                "[owa] .column,\n" +
                ".ie .gutter,\n" +
                "[owa] .gutter {\n" +
                "  display: table-cell;\n" +
                "  float: none !important;\n" +
                "  vertical-align: top;\n" +
                "}\n" +
                ".ie div.preheader,\n" +
                "[owa] div.preheader,\n" +
                ".ie .email-footer,\n" +
                "[owa] .email-footer {\n" +
                "  max-width: 560px !important;\n" +
                "  width: 560px !important;\n" +
                "}\n" +
                ".ie .snippet,\n" +
                "[owa] .snippet,\n" +
                ".ie .webversion,\n" +
                "[owa] .webversion {\n" +
                "  width: 280px !important;\n" +
                "}\n" +
                ".ie div.header,\n" +
                "[owa] div.header,\n" +
                ".ie .layout,\n" +
                "[owa] .layout,\n" +
                ".ie .one-col .column,\n" +
                "[owa] .one-col .column {\n" +
                "  max-width: 600px !important;\n" +
                "  width: 600px !important;\n" +
                "}\n" +
                ".ie .fixed-width.has-border,\n" +
                "[owa] .fixed-width.has-border,\n" +
                ".ie .has-gutter.has-border,\n" +
                "[owa] .has-gutter.has-border {\n" +
                "  max-width: 602px !important;\n" +
                "  width: 602px !important;\n" +
                "}\n" +
                ".ie .two-col .column,\n" +
                "[owa] .two-col .column {\n" +
                "  max-width: 300px !important;\n" +
                "  width: 300px !important;\n" +
                "}\n" +
                ".ie .three-col .column,\n" +
                "[owa] .three-col .column,\n" +
                ".ie .narrow,\n" +
                "[owa] .narrow {\n" +
                "  max-width: 200px !important;\n" +
                "  width: 200px !important;\n" +
                "}\n" +
                ".ie .wide,\n" +
                "[owa] .wide {\n" +
                "  width: 400px !important;\n" +
                "}\n" +
                ".ie .two-col.has-gutter .column,\n" +
                "[owa] .two-col.x_has-gutter .column {\n" +
                "  max-width: 290px !important;\n" +
                "  width: 290px !important;\n" +
                "}\n" +
                ".ie .three-col.has-gutter .column,\n" +
                "[owa] .three-col.x_has-gutter .column,\n" +
                ".ie .has-gutter .narrow,\n" +
                "[owa] .has-gutter .narrow {\n" +
                "  max-width: 188px !important;\n" +
                "  width: 188px !important;\n" +
                "}\n" +
                ".ie .has-gutter .wide,\n" +
                "[owa] .has-gutter .wide {\n" +
                "  max-width: 394px !important;\n" +
                "  width: 394px !important;\n" +
                "}\n" +
                ".ie .two-col.has-gutter.has-border .column,\n" +
                "[owa] .two-col.x_has-gutter.x_has-border .column {\n" +
                "  max-width: 292px !important;\n" +
                "  width: 292px !important;\n" +
                "}\n" +
                ".ie .three-col.has-gutter.has-border .column,\n" +
                "[owa] .three-col.x_has-gutter.x_has-border .column,\n" +
                ".ie .has-gutter.has-border .narrow,\n" +
                "[owa] .has-gutter.x_has-border .narrow {\n" +
                "  max-width: 190px !important;\n" +
                "  width: 190px !important;\n" +
                "}\n" +
                ".ie .has-gutter.has-border .wide,\n" +
                "[owa] .has-gutter.x_has-border .wide {\n" +
                "  max-width: 396px !important;\n" +
                "  width: 396px !important;\n" +
                "}\n" +
                ".ie .fixed-width .layout__inner {\n" +
                "  border-left: 0 none white !important;\n" +
                "  border-right: 0 none white !important;\n" +
                "}\n" +
                ".ie .layout__edges {\n" +
                "  display: none;\n" +
                "}\n" +
                ".mso .layout__edges {\n" +
                "  font-size: 0;\n" +
                "}\n" +
                ".layout-fixed-width,\n" +
                ".mso .layout-full-width {\n" +
                "  background-color: #ffffff;\n" +
                "}\n" +
                "@media only screen and (min-width: 620px) {\n" +
                "  .column,\n" +
                "  .gutter {\n" +
                "    display: table-cell;\n" +
                "    Float: none !important;\n" +
                "    vertical-align: top;\n" +
                "  }\n" +
                "  div.preheader,\n" +
                "  .email-footer {\n" +
                "    max-width: 560px !important;\n" +
                "    width: 560px !important;\n" +
                "  }\n" +
                "  .snippet,\n" +
                "  .webversion {\n" +
                "    width: 280px !important;\n" +
                "  }\n" +
                "  div.header,\n" +
                "  .layout,\n" +
                "  .one-col .column {\n" +
                "    max-width: 600px !important;\n" +
                "    width: 600px !important;\n" +
                "  }\n" +
                "  .fixed-width.has-border,\n" +
                "  .fixed-width.ecxhas-border,\n" +
                "  .has-gutter.has-border,\n" +
                "  .has-gutter.ecxhas-border {\n" +
                "    max-width: 602px !important;\n" +
                "    width: 602px !important;\n" +
                "  }\n" +
                "  .two-col .column {\n" +
                "    max-width: 300px !important;\n" +
                "    width: 300px !important;\n" +
                "  }\n" +
                "  .three-col .column,\n" +
                "  .column.narrow {\n" +
                "    max-width: 200px !important;\n" +
                "    width: 200px !important;\n" +
                "  }\n" +
                "  .column.wide {\n" +
                "    width: 400px !important;\n" +
                "  }\n" +
                "  .two-col.has-gutter .column,\n" +
                "  .two-col.ecxhas-gutter .column {\n" +
                "    max-width: 290px !important;\n" +
                "    width: 290px !important;\n" +
                "  }\n" +
                "  .three-col.has-gutter .column,\n" +
                "  .three-col.ecxhas-gutter .column,\n" +
                "  .has-gutter .narrow {\n" +
                "    max-width: 188px !important;\n" +
                "    width: 188px !important;\n" +
                "  }\n" +
                "  .has-gutter .wide {\n" +
                "    max-width: 394px !important;\n" +
                "    width: 394px !important;\n" +
                "  }\n" +
                "  .two-col.has-gutter.has-border .column,\n" +
                "  .two-col.ecxhas-gutter.ecxhas-border .column {\n" +
                "    max-width: 292px !important;\n" +
                "    width: 292px !important;\n" +
                "  }\n" +
                "  .three-col.has-gutter.has-border .column,\n" +
                "  .three-col.ecxhas-gutter.ecxhas-border .column,\n" +
                "  .has-gutter.has-border .narrow,\n" +
                "  .has-gutter.ecxhas-border .narrow {\n" +
                "    max-width: 190px !important;\n" +
                "    width: 190px !important;\n" +
                "  }\n" +
                "  .has-gutter.has-border .wide,\n" +
                "  .has-gutter.ecxhas-border .wide {\n" +
                "    max-width: 396px !important;\n" +
                "    width: 396px !important;\n" +
                "  }\n" +
                "}\n" +
                "@media only screen and (-webkit-min-device-pixel-ratio: 2), only screen and (min--moz-device-pixel-ratio: 2), only screen and (-o-min-device-pixel-ratio: 2/1), only screen and (min-device-pixel-ratio: 2), only screen and (min-resolution: 192dpi), only screen and (min-resolution: 2dppx) {\n" +
                "  .fblike {\n" +
                "    background-image: url(http://i3.createsend1.com/static/eb/master/13-the-blueprint-3/images/fblike@2x.png) !important;\n" +
                "  }\n" +
                "  .tweet {\n" +
                "    background-image: url(http://i4.createsend1.com/static/eb/master/13-the-blueprint-3/images/tweet@2x.png) !important;\n" +
                "  }\n" +
                "  .linkedinshare {\n" +
                "    background-image: url(http://i5.createsend1.com/static/eb/master/13-the-blueprint-3/images/lishare@2x.png) !important;\n" +
                "  }\n" +
                "  .forwardtoafriend {\n" +
                "    background-image: url(http://i6.createsend1.com/static/eb/master/13-the-blueprint-3/images/forward@2x.png) !important;\n" +
                "  }\n" +
                "}\n" +
                "@media (max-width: 321px) {\n" +
                "  .fixed-width.has-border .layout__inner {\n" +
                "    border-width: 1px 0 !important;\n" +
                "  }\n" +
                "  .layout,\n" +
                "  .column {\n" +
                "    min-width: 320px !important;\n" +
                "    width: 320px !important;\n" +
                "  }\n" +
                "  .border {\n" +
                "    display: none;\n" +
                "  }\n" +
                "}\n" +
                ".mso div {\n" +
                "  border: 0 none white !important;\n" +
                "}\n" +
                ".mso .w560 .divider {\n" +
                "  Margin-left: 260px !important;\n" +
                "  Margin-right: 260px !important;\n" +
                "}\n" +
                ".mso .w360 .divider {\n" +
                "  Margin-left: 160px !important;\n" +
                "  Margin-right: 160px !important;\n" +
                "}\n" +
                ".mso .w260 .divider {\n" +
                "  Margin-left: 110px !important;\n" +
                "  Margin-right: 110px !important;\n" +
                "}\n" +
                ".mso .w160 .divider {\n" +
                "  Margin-left: 60px !important;\n" +
                "  Margin-right: 60px !important;\n" +
                "}\n" +
                ".mso .w354 .divider {\n" +
                "  Margin-left: 157px !important;\n" +
                "  Margin-right: 157px !important;\n" +
                "}\n" +
                ".mso .w250 .divider {\n" +
                "  Margin-left: 105px !important;\n" +
                "  Margin-right: 105px !important;\n" +
                "}\n" +
                ".mso .w148 .divider {\n" +
                "  Margin-left: 54px !important;\n" +
                "  Margin-right: 54px !important;\n" +
                "}\n" +
                ".mso .footer__share-button p {\n" +
                "  margin: 0;\n" +
                "}\n" +
                ".mso .size-8,\n" +
                ".ie .size-8 {\n" +
                "  font-size: 8px !important;\n" +
                "  line-height: 14px !important;\n" +
                "}\n" +
                ".mso .size-9,\n" +
                ".ie .size-9 {\n" +
                "  font-size: 9px !important;\n" +
                "  line-height: 16px !important;\n" +
                "}\n" +
                ".mso .size-10,\n" +
                ".ie .size-10 {\n" +
                "  font-size: 10px !important;\n" +
                "  line-height: 18px !important;\n" +
                "}\n" +
                ".mso .size-11,\n" +
                ".ie .size-11 {\n" +
                "  font-size: 11px !important;\n" +
                "  line-height: 19px !important;\n" +
                "}\n" +
                ".mso .size-12,\n" +
                ".ie .size-12 {\n" +
                "  font-size: 12px !important;\n" +
                "  line-height: 19px !important;\n" +
                "}\n" +
                ".mso .size-13,\n" +
                ".ie .size-13 {\n" +
                "  font-size: 13px !important;\n" +
                "  line-height: 21px !important;\n" +
                "}\n" +
                ".mso .size-14,\n" +
                ".ie .size-14 {\n" +
                "  font-size: 14px !important;\n" +
                "  line-height: 21px !important;\n" +
                "}\n" +
                ".mso .size-15,\n" +
                ".ie .size-15 {\n" +
                "  font-size: 15px !important;\n" +
                "  line-height: 23px !important;\n" +
                "}\n" +
                ".mso .size-16,\n" +
                ".ie .size-16 {\n" +
                "  font-size: 16px !important;\n" +
                "  line-height: 24px !important;\n" +
                "}\n" +
                ".mso .size-17,\n" +
                ".ie .size-17 {\n" +
                "  font-size: 17px !important;\n" +
                "  line-height: 26px !important;\n" +
                "}\n" +
                ".mso .size-18,\n" +
                ".ie .size-18 {\n" +
                "  font-size: 18px !important;\n" +
                "  line-height: 26px !important;\n" +
                "}\n" +
                ".mso .size-20,\n" +
                ".ie .size-20 {\n" +
                "  font-size: 20px !important;\n" +
                "  line-height: 28px !important;\n" +
                "}\n" +
                ".mso .size-22,\n" +
                ".ie .size-22 {\n" +
                "  font-size: 22px !important;\n" +
                "  line-height: 31px !important;\n" +
                "}\n" +
                ".mso .size-24,\n" +
                ".ie .size-24 {\n" +
                "  font-size: 24px !important;\n" +
                "  line-height: 32px !important;\n" +
                "}\n" +
                ".mso .size-26,\n" +
                ".ie .size-26 {\n" +
                "  font-size: 26px !important;\n" +
                "  line-height: 34px !important;\n" +
                "}\n" +
                ".mso .size-28,\n" +
                ".ie .size-28 {\n" +
                "  font-size: 28px !important;\n" +
                "  line-height: 36px !important;\n" +
                "}\n" +
                ".mso .size-30,\n" +
                ".ie .size-30 {\n" +
                "  font-size: 30px !important;\n" +
                "  line-height: 38px !important;\n" +
                "}\n" +
                ".mso .size-32,\n" +
                ".ie .size-32 {\n" +
                "  font-size: 32px !important;\n" +
                "  line-height: 40px !important;\n" +
                "}\n" +
                ".mso .size-34,\n" +
                ".ie .size-34 {\n" +
                "  font-size: 34px !important;\n" +
                "  line-height: 43px !important;\n" +
                "}\n" +
                ".mso .size-36,\n" +
                ".ie .size-36 {\n" +
                "  font-size: 36px !important;\n" +
                "  line-height: 43px !important;\n" +
                "}\n" +
                ".mso .size-40,\n" +
                ".ie .size-40 {\n" +
                "  font-size: 40px !important;\n" +
                "  line-height: 47px !important;\n" +
                "}\n" +
                ".mso .size-44,\n" +
                ".ie .size-44 {\n" +
                "  font-size: 44px !important;\n" +
                "  line-height: 50px !important;\n" +
                "}\n" +
                ".mso .size-48,\n" +
                ".ie .size-48 {\n" +
                "  font-size: 48px !important;\n" +
                "  line-height: 54px !important;\n" +
                "}\n" +
                ".mso .size-56,\n" +
                ".ie .size-56 {\n" +
                "  font-size: 56px !important;\n" +
                "  line-height: 60px !important;\n" +
                "}\n" +
                ".mso .size-64,\n" +
                ".ie .size-64 {\n" +
                "  font-size: 64px !important;\n" +
                "  line-height: 63px !important;\n" +
                "}\n" +
                ".footer__share-button p {\n" +
                "  margin: 0;\n" +
                "}\n" +
                "</style>\n" +
                "    \n" +
                "  <!--[if !mso]><!--><style type=\"text/css\">\n" +
                "@import url(https://fonts.googleapis.com/css?family=Roboto:400,700,400italic,700italic);\n" +
                "</style><link href=\"https://fonts.googleapis.com/css?family=Roboto:400,700,400italic,700italic\" rel=\"stylesheet\" type=\"text/css\"><!--<![endif]--><style type=\"text/css\">\n" +
                "body{background-color:#f2f4f6}.logo a:hover,.logo a:focus{color:#859bb1 !important}.mso .layout-has-border{border-top:1px solid #b6c1cc;border-bottom:1px solid #b6c1cc}.mso .layout-has-bottom-border{border-bottom:1px solid #b6c1cc}.mso .border,.ie .border{background-color:#b6c1cc}.mso h1,.ie h1{}.mso h1,.ie h1{font-size:26px !important;line-height:34px !important}.mso h2,.ie h2{}.mso h2,.ie h2{font-size:20px !important;line-height:28px !important}.mso h3,.ie h3{}.mso .layout__inner,.ie .layout__inner{}.mso .footer__share-button p{}.mso .footer__share-button p{font-family:Roboto,sans-serif}\n" +
                "</style><meta name=\"robots\" content=\"noindex,nofollow\"></meta>\n" +
                "<meta property=\"og:title\" content=\"May News: The New Marketing Resources Hub | How Flight Centre Increased CTRs by 57%\"></meta>\n" +
                "</head>\n" +
                "<!--[if mso]>\n" +
                "  <body class=\"mso\">\n" +
                "<![endif]-->\n" +
                "<!--[if !mso]><!-->\n" +
                "  <body class=\"no-padding\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;\">\n" +
                "<!--<![endif]-->\n" +
                "    <table class=\"wrapper\" style=\"border-collapse: collapse;table-layout: fixed;min-width: 320px;width: 100%;background-color: #f2f4f6;\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"><tbody><tr><td>\n" +
                "      <div role=\"banner\">\n" +
                "        <div class=\"preheader\" style=\"Margin: 0 auto;max-width: 560px;min-width: 280px; width: 280px;width: calc(28000% - 167440px);\">\n" +
                "          <div style=\"border-collapse: collapse;display: table;width: 100%;\">\n" +
                "          <!--[if (mso)|(IE)]><table align=\"center\" class=\"preheader\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"><tr><td style=\"width: 280px\" valign=\"top\"><![endif]-->\n" +
                "            <div class=\"snippet\" style=\"display: table-cell;Float: left;font-size: 12px;line-height: 19px;max-width: 280px;min-width: 140px; width: 140px;width: calc(14000% - 78120px);padding: 10px 0 5px 0;color: #8e959c;font-family: Roboto,sans-serif;\">\n" +
                "              <p style=\"Margin-top: 0;Margin-bottom: 0;\">The latest news you need to know.</p>\n" +
                "            </div>\n" +
                "          <!--[if (mso)|(IE)]></td><td style=\"width: 280px\" valign=\"top\"><![endif]-->\n" +
                "            <div class=\"webversion\" style=\"display: table-cell;Float: left;font-size: 12px;line-height: 19px;max-width: 280px;min-width: 139px; width: 139px;width: calc(14100% - 78680px);padding: 10px 0 5px 0;text-align: right;color: #8e959c;font-family: Roboto,sans-serif;\">\n" +
                "              <p style=\"Margin-top: 0;Margin-bottom: 0;\">No Images? <a style=\"text-decoration: underline;transition: opacity 0.1s ease-in;color: #8e959c;\" href=\"https://campaignmonitor.createsend1.com/t/y-e-hituxy-tlhdxtjb-a/\">Click here</a></p>\n" +
                "            </div>\n" +
                "          <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "          </div>\n" +
                "        </div>\n" +
                "        \n" +
                "      </div>\n" +
                "      <div role=\"section\">\n" +
                "      <div style=\"background-color: #509cf6;background-position: 0px 0px;background-image: url(http://i1.createsend1.com/ei/y/09/911/61F/085008/csfinal/May31-03-011.jpg);background-repeat: repeat;\">\n" +
                "        <div class=\"layout one-col\" style=\"Margin: 0 auto;max-width: 600px;min-width: 320px; width: 320px;width: calc(28000% - 167400px);overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;\">\n" +
                "          <div class=\"layout__inner\" style=\"border-collapse: collapse;display: table;width: 100%;\">\n" +
                "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"><tr class=\"layout-full-width\" style=\"background-color: #509cf6;background-position: 0px 0px;background-image: url(http://i1.createsend1.com/ei/y/09/911/61F/085008/csfinal/May31-03-011.jpg);background-repeat: repeat;\"><td class=\"layout__edges\">&nbsp;</td><td style=\"width: 600px\" class=\"w560\"><![endif]-->\n" +
                "            <div class=\"column\" style=\"max-width: 600px;min-width: 320px; width: 320px;width: calc(28000% - 167400px);text-align: left;color: #717a8a;font-size: 14px;line-height: 21px;font-family: Roboto,sans-serif;\">\n" +
                "            \n" +
                "        <div style=\"font-size: 12px;font-style: normal;font-weight: normal;\" align=\"center\">\n" +
                "          <a style=\"text-decoration: underline;transition: opacity 0.1s ease-in;color: #509cf6;\" href=\"https://campaignmonitor.createsend1.com/t/y-l-hituxy-tlhdxtjb-r/\"><img style=\"border: 0;display: block;height: auto;width: 100%;max-width: 296px;\" alt=\"\" width=\"296\" src=\"http://i1.createsend1.com/ei/y/09/911/61F/085040/csfinal/campaignmonitor_logo_white_3_big1.png\"></a>\n" +
                "        </div>\n" +
                "      \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;Margin-top: 20px;\">\n" +
                "      <h3 class=\"size-12\" style=\"Margin-top: 0;Margin-bottom: 12px;font-style: normal;font-weight: normal;color: #717a8a;font-size: 12px;line-height: 19px;text-align: center;\" lang=\"x-size-12\"><span style=\"color:#ffffff\"><strong>NEWSLETTER</strong></span></h3>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <h1 class=\"size-44\" style=\"Margin-top: 0;Margin-bottom: 20px;font-style: normal;font-weight: normal;color: #717a8a;font-size: 34px;line-height: 43px;text-align: center;\" lang=\"x-size-44\"><a style=\"text-decoration: none;transition: opacity 0.1s ease-in;color: #509cf6;\" href=\"https://campaignmonitor.createsend1.com/t/y-l-hituxy-tlhdxtjb-y/\"><span style=\"color:#ffffff\"><strong>Introducing the&nbsp;</strong></span></a><strong><a style=\"text-decoration: none;transition: opacity 0.1s ease-in;color: #509cf6;\" href=\"https://campaignmonitor.createsend1.com/t/y-l-hituxy-tlhdxtjb-j/\"><span style=\"color:#ffffff\">New Resources Hub</span></a></strong></h1>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <p style=\"Margin-top: 0;Margin-bottom: 20px;text-align: center;\"><span style=\"color:#ffffff\">Everything you need for digital and email marketing that's on point.</span></p>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <div class=\"btn btn--flat btn--large\" style=\"Margin-bottom: 20px;text-align: center;\">\n" +
                "        <![if !mso]><a style=\"border-radius: 4px;display: inline-block;font-size: 14px;font-weight: bold;line-height: 24px;padding: 12px 24px;text-align: center;text-decoration: none !important;transition: opacity 0.1s ease-in;color: #509cf6 !important;background-color: #f6f6f8;font-family: sans-serif;\" href=\"https://campaignmonitor.createsend1.com/t/y-l-hituxy-tlhdxtjb-t/\">Check it out</a><![endif]>\n" +
                "      <!--[if mso]><p style=\"line-height:0;margin:0;\">&nbsp;</p><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" href=\"https://campaignmonitor.createsend1.com/t/y-l-hituxy-tlhdxtjb-t/\" style=\"width:128px\" arcsize=\"9%\" fillcolor=\"#F6F6F8\" stroke=\"f\"><v:textbox style=\"mso-fit-shape-to-text:t\" inset=\"0px,11px,0px,11px\"><center style=\"font-size:14px;line-height:24px;color:#509CF6;font-family:sans-serif;font-weight:bold;mso-line-height-rule:exactly;mso-text-raise:4px\">Check it out</center></v:textbox></v:roundrect><![endif]--></div>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <div style=\"line-height:10px;font-size:1px\">&nbsp;</div>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "        <div style=\"font-size: 12px;font-style: normal;font-weight: normal;\" align=\"center\">\n" +
                "          <a style=\"text-decoration: underline;transition: opacity 0.1s ease-in;color: #509cf6;\" href=\"https://campaignmonitor.createsend1.com/t/y-l-hituxy-tlhdxtjb-i/\"><img style=\"border: 0;display: block;height: auto;width: 100%;max-width: 600px;\" alt=\"Campaign Monitor Resources Hub\" width=\"560\" src=\"http://i2.createsend1.com/ei/y/09/911/61F/085040/csfinal/www.GIFCreator.me_OeWa7Q2.gif\"></a>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "            \n" +
                "            </div>\n" +
                "          <!--[if (mso)|(IE)]></td><td class=\"layout__edges\">&nbsp;</td></tr></table><![endif]-->\n" +
                "          </div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "  \n" +
                "      <div style=\"background-color: #8dc8d6;background-position: 0px 0px;background-image: url(http://i2.createsend1.com/ei/y/09/911/61F/085008/csfinal/CloudsBackground2.jpg);background-repeat: repeat;\">\n" +
                "        <div class=\"layout two-col\" style=\"Margin: 0 auto;max-width: 600px;min-width: 320px; width: 320px;width: calc(28000% - 167400px);overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;\">\n" +
                "          <div class=\"layout__inner\" style=\"border-collapse: collapse;display: table;width: 100%;\">\n" +
                "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"><tr class=\"layout-full-width\" style=\"background-color: #8dc8d6;background-position: 0px 0px;background-image: url(http://i2.createsend1.com/ei/y/09/911/61F/085008/csfinal/CloudsBackground2.jpg);background-repeat: repeat;\"><td class=\"layout__edges\">&nbsp;</td><td style=\"width: 300px\" valign=\"top\" class=\"w260\"><![endif]-->\n" +
                "            <div class=\"column\" style=\"Float: left;max-width: 320px;min-width: 300px; width: 320px;width: calc(12300px - 2000%);text-align: left;color: #717a8a;font-size: 14px;line-height: 21px;font-family: Roboto,sans-serif;\">\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <div style=\"line-height:41px;font-size:1px\">&nbsp;</div>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <p class=\"size-13\" style=\"Margin-top: 0;Margin-bottom: 20px;font-size: 13px;line-height: 21px;\" lang=\"x-size-13\"><span style=\"color:#ffffff\"><strong>BLOG</strong></span></p>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <h2 class=\"size-26\" style=\"Margin-top: 0;Margin-bottom: 16px;font-style: normal;font-weight: normal;color: #509cf6;font-size: 22px;line-height: 31px;\" lang=\"x-size-26\"><a style=\"text-decoration: none;transition: opacity 0.1s ease-in;color: #509cf6;\" href=\"https://campaignmonitor.createsend1.com/t/y-l-hituxy-tlhdxtjb-d/\"><strong>How Flight Centre Increased Click-Throughs&nbsp;by 57%</strong></a></h2>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <p class=\"size-16\" style=\"Margin-top: 0;Margin-bottom: 20px;font-size: 16px;line-height: 24px;\" lang=\"x-size-16\"><span style=\"color:#ffffff\">Discover how Flight Centre&nbsp;uses Campaign Monitor to fuel Flight Centre Canada&#8217;s email marketing engine.</span></p>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <div class=\"btn btn--flat btn--medium\" style=\"Margin-bottom: 20px;text-align: left;\">\n" +
                "        <![if !mso]><a style=\"border-radius: 4px;display: inline-block;font-size: 12px;font-weight: bold;line-height: 22px;padding: 10px 20px;text-align: center;text-decoration: none !important;transition: opacity 0.1s ease-in;color: #509cf6 !important;background-color: #ffffff;font-family: sans-serif;\" href=\"https://campaignmonitor.createsend1.com/t/y-l-hituxy-tlhdxtjb-h/\">Read on</a><![endif]>\n" +
                "      <!--[if mso]><p style=\"line-height:0;margin:0;\">&nbsp;</p><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" href=\"https://campaignmonitor.createsend1.com/t/y-l-hituxy-tlhdxtjb-h/\" style=\"width:87px\" arcsize=\"10%\" fillcolor=\"#FFFFFF\" stroke=\"f\"><v:textbox style=\"mso-fit-shape-to-text:t\" inset=\"0px,9px,0px,9px\"><center style=\"font-size:12px;line-height:22px;color:#509CF6;font-family:sans-serif;font-weight:bold;mso-line-height-rule:exactly;mso-text-raise:4px\">Read on</center></v:textbox></v:roundrect><![endif]--></div>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <div style=\"line-height:20px;font-size:1px\">&nbsp;</div>\n" +
                "    </div>\n" +
                "            \n" +
                "            </div>\n" +
                "          <!--[if (mso)|(IE)]></td><td style=\"width: 300px\" valign=\"top\" class=\"w260\"><![endif]-->\n" +
                "            <div class=\"column\" style=\"Float: left;max-width: 320px;min-width: 300px; width: 320px;width: calc(12300px - 2000%);text-align: left;color: #717a8a;font-size: 14px;line-height: 21px;font-family: Roboto,sans-serif;\">\n" +
                "            \n" +
                "        <div style=\"font-size: 12px;font-style: normal;font-weight: normal;\" align=\"center\">\n" +
                "          <a style=\"text-decoration: underline;transition: opacity 0.1s ease-in;color: #509cf6;\" href=\"https://campaignmonitor.createsend1.com/t/y-l-hituxy-tlhdxtjb-k/\"><img class=\"gnd-corner-image gnd-corner-image-center gnd-corner-image-top gnd-corner-image-bottom\" style=\"border: 0;display: block;height: auto;width: 100%;max-width: 450px;\" alt=\"Flight Centre\" width=\"300\" src=\"http://i3.createsend1.com/ei/y/09/911/61F/085040/csfinal/Airplane23.png\"></a>\n" +
                "        </div>\n" +
                "      \n" +
                "            </div>\n" +
                "          <!--[if (mso)|(IE)]></td><td class=\"layout__edges\">&nbsp;</td></tr></table><![endif]-->\n" +
                "          </div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "  \n" +
                "      <div style=\"background-color: #f2f4f6;\">\n" +
                "        <div class=\"layout one-col\" style=\"Margin: 0 auto;max-width: 600px;min-width: 320px; width: 320px;width: calc(28000% - 167400px);overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;\">\n" +
                "          <div class=\"layout__inner\" style=\"border-collapse: collapse;display: table;width: 100%;\">\n" +
                "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"><tr class=\"layout-full-width\" style=\"background-color: #f2f4f6;\"><td class=\"layout__edges\">&nbsp;</td><td style=\"width: 600px\" class=\"w560\"><![endif]-->\n" +
                "            <div class=\"column\" style=\"max-width: 600px;min-width: 320px; width: 320px;width: calc(28000% - 167400px);text-align: left;color: #717a8a;font-size: 14px;line-height: 21px;font-family: Roboto,sans-serif;\">\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <div style=\"line-height:20px;font-size:1px\">&nbsp;</div>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <div style=\"line-height:70px;font-size:1px\">&nbsp;</div>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <h2 class=\"size-26\" style=\"Margin-top: 0;Margin-bottom: 16px;font-style: normal;font-weight: normal;color: #509cf6;font-size: 22px;line-height: 31px;\" lang=\"x-size-26\"><span style=\"color:#002b45\"><strong>Read the latest from the blog</strong></span></h2>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <div style=\"line-height:15px;font-size:1px\">&nbsp;</div>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <h3 style=\"Margin-top: 0;Margin-bottom: 12px;font-style: normal;font-weight: normal;color: #717a8a;font-size: 16px;line-height: 24px;font-family: sans-serif;\"><span class=\"font-sans-serif\"><span style=\"color:#509cf6\"><strong>38 Awesome Chrome Extensions for Email Marketers</strong></span></span></h3>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <p style=\"Margin-top: 0;Margin-bottom: 20px;line-height: 24px;\"><font size=\"3\">Leverage the best Chrome&nbsp;extensions for your email&nbsp;marketing.&nbsp;</font><a style=\"text-decoration: underline;transition: opacity 0.1s ease-in;color: #509cf6;font-size: 16px;\" href=\"https://campaignmonitor.createsend1.com/t/y-l-hituxy-tlhdxtjb-u/\">Read on</a></p>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <div style=\"line-height:10px;font-size:1px\">&nbsp;</div>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <h3 style=\"Margin-top: 0;Margin-bottom: 12px;font-style: normal;font-weight: normal;color: #717a8a;font-size: 16px;line-height: 24px;font-family: sans-serif;\"><span class=\"font-sans-serif\"><span style=\"color:#509cf6\"><strong><a style=\"text-decoration: none;transition: opacity 0.1s ease-in;color: #509cf6;\" href=\"https://campaignmonitor.createsend1.com/t/y-l-hituxy-tlhdxtjb-o/\">L</a>essons from the Customer Journey</strong></span></span></h3>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <p class=\"size-16\" style=\"Margin-top: 0;Margin-bottom: 20px;font-size: 16px;line-height: 24px;\" lang=\"x-size-16\">Focus on&nbsp;the touchpoints&nbsp;along the customer journey for success.<span style=\"color:rgb(142, 149, 156)\">&nbsp;</span><a style=\"text-decoration: underline;transition: opacity 0.1s ease-in;color: #509cf6;\" href=\"https://campaignmonitor.createsend1.com/t/y-l-hituxy-tlhdxtjb-b/\"><span style=\"color:#509cf6\">Read on</span></a></p>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <div style=\"line-height:10px;font-size:1px\">&nbsp;</div>\n" +
                "    </div>\n" +
                "            \n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <div style=\"line-height:50px;font-size:1px\">&nbsp;</div>\n" +
                "    </div>\n" +
                "            \n" +
                "            </div>\n" +
                "          <!--[if (mso)|(IE)]></td><td class=\"layout__edges\">&nbsp;</td></tr></table><![endif]-->\n" +
                "          </div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "  \n" +
                "      <div class=\"layout one-col fixed-width\" style=\"Margin: 0 auto;max-width: 600px;min-width: 320px; width: 320px;width: calc(28000% - 167400px);overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;\">\n" +
                "        <div class=\"layout__inner\" style=\"border-collapse: collapse;display: table;width: 100%;background-color: #509cf6;\">\n" +
                "        <!--[if (mso)|(IE)]><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"><tr class=\"layout-fixed-width\" style=\"background-color: #509cf6;\"><td style=\"width: 600px\" class=\"w560\"><![endif]-->\n" +
                "          <div class=\"column\" style=\"text-align: left;color: #717a8a;font-size: 14px;line-height: 21px;font-family: Roboto,sans-serif;max-width: 600px;min-width: 320px; width: 320px;width: calc(28000% - 167400px);\">\n" +
                "        \n" +
                "            <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <div style=\"line-height:11px;font-size:1px\">&nbsp;</div>\n" +
                "    </div>\n" +
                "        \n" +
                "          </div>\n" +
                "        <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "        </div>\n" +
                "      </div>\n" +
                "  \n" +
                "      <div class=\"layout one-col fixed-width\" style=\"Margin: 0 auto;max-width: 600px;min-width: 320px; width: 320px;width: calc(28000% - 167400px);overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;\">\n" +
                "        <div class=\"layout__inner\" style=\"border-collapse: collapse;display: table;width: 100%;background-color: #ffffff;\">\n" +
                "        <!--[if (mso)|(IE)]><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"><tr class=\"layout-fixed-width\" style=\"background-color: #ffffff;\"><td style=\"width: 600px\" class=\"w560\"><![endif]-->\n" +
                "          <div class=\"column\" style=\"text-align: left;color: #717a8a;font-size: 14px;line-height: 21px;font-family: Roboto,sans-serif;max-width: 600px;min-width: 320px; width: 320px;width: calc(28000% - 167400px);\">\n" +
                "        \n" +
                "            <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <div style=\"line-height:50px;font-size:1px\">&nbsp;</div>\n" +
                "    </div>\n" +
                "        \n" +
                "            <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <h2 class=\"size-26\" style=\"Margin-top: 0;Margin-bottom: 16px;font-style: normal;font-weight: normal;color: #509cf6;font-size: 22px;line-height: 31px;text-align: center;\" lang=\"x-size-26\"><span style=\"color:#002b45\"><strong>Send&nbsp;email your customers can't ignore</strong></span></h2>\n" +
                "    </div>\n" +
                "        \n" +
                "            <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <p class=\"size-16\" style=\"Margin-top: 0;Margin-bottom: 20px;font-size: 16px;line-height: 24px;text-align: center;\" lang=\"x-size-16\">Easy-to-use, professional-grade email marketing and automation for today&#8217;s fast-growing businesses.</p>\n" +
                "    </div>\n" +
                "        \n" +
                "            <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <div class=\"btn btn--flat btn--medium\" style=\"Margin-bottom: 20px;text-align: center;\">\n" +
                "        <![if !mso]><a style=\"border-radius: 4px;display: inline-block;font-size: 12px;font-weight: bold;line-height: 22px;padding: 10px 20px;text-align: center;text-decoration: none !important;transition: opacity 0.1s ease-in;color: #ffffff !important;background-color: #509cf6;font-family: Roboto, Tahoma, sans-serif;\" href=\"https://campaignmonitor.createsend1.com/t/y-l-hituxy-tlhdxtjb-n/\">Create an email</a><![endif]>\n" +
                "      <!--[if mso]><p style=\"line-height:0;margin:0;\">&nbsp;</p><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" href=\"https://campaignmonitor.createsend1.com/t/y-l-hituxy-tlhdxtjb-n/\" style=\"width:124px\" arcsize=\"10%\" fillcolor=\"#509CF6\" stroke=\"f\"><v:textbox style=\"mso-fit-shape-to-text:t\" inset=\"0px,9px,0px,9px\"><center style=\"font-size:12px;line-height:22px;color:#FFFFFF;font-family:Roboto,Tahoma,sans-serif;font-weight:bold;mso-line-height-rule:exactly;mso-text-raise:4px\">Create an email</center></v:textbox></v:roundrect><![endif]--></div>\n" +
                "    </div>\n" +
                "        \n" +
                "            <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n" +
                "      <div style=\"line-height:44px;font-size:1px\">&nbsp;</div>\n" +
                "    </div>\n" +
                "        \n" +
                "          </div>\n" +
                "        <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "        </div>\n" +
                "      </div>\n" +
                "  \n" +
                "      <div style=\"line-height:45px;font-size:45px;\">&nbsp;</div>\n" +
                "  \n" +
                "      <div style=\"background-color: #f2f4f6;\">\n" +
                "        <div class=\"layout three-col\" style=\"Margin: 0 auto;max-width: 600px;min-width: 320px; width: 320px;width: calc(28000% - 167400px);overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;\">\n" +
                "          <div class=\"layout__inner\" style=\"border-collapse: collapse;display: table;width: 100%;\">\n" +
                "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"><tr class=\"layout-full-width\" style=\"background-color: #f2f4f6;\"><td class=\"layout__edges\">&nbsp;</td><td style=\"width: 200px\" valign=\"top\" class=\"w160\"><![endif]-->\n" +
                "            <div class=\"column\" style=\"Float: left;max-width: 320px;min-width: 200px; width: 320px;width: calc(72200px - 12000%);text-align: left;color: #717a8a;font-size: 14px;line-height: 21px;font-family: Roboto,sans-serif;\">\n" +
                "            \n" +
                "        <div style=\"font-size: 12px;font-style: normal;font-weight: normal;\" align=\"center\">\n" +
                "          <a style=\"text-decoration: underline;transition: opacity 0.1s ease-in;color: #509cf6;\" href=\"https://campaignmonitor.createsend1.com/t/y-l-hituxy-tlhdxtjb-p/\"><img class=\"gnd-corner-image gnd-corner-image-center gnd-corner-image-top gnd-corner-image-bottom\" style=\"border: 0;display: block;height: auto;width: 100%;max-width: 300px;\" alt=\"twitter\" width=\"200\" src=\"http://i4.createsend1.com/ei/y/09/911/61F/085040/csfinal/social-tw.png\"></a>\n" +
                "        </div>\n" +
                "      \n" +
                "            </div>\n" +
                "          <!--[if (mso)|(IE)]></td><td style=\"width: 200px\" valign=\"top\" class=\"w160\"><![endif]-->\n" +
                "            <div class=\"column\" style=\"Float: left;max-width: 320px;min-width: 200px; width: 320px;width: calc(72200px - 12000%);text-align: left;color: #717a8a;font-size: 14px;line-height: 21px;font-family: Roboto,sans-serif;\">\n" +
                "            \n" +
                "        <div style=\"font-size: 12px;font-style: normal;font-weight: normal;\" align=\"center\">\n" +
                "          <a style=\"text-decoration: underline;transition: opacity 0.1s ease-in;color: #509cf6;\" href=\"https://campaignmonitor.createsend1.com/t/y-l-hituxy-tlhdxtjb-x/\"><img class=\"gnd-corner-image gnd-corner-image-center gnd-corner-image-top gnd-corner-image-bottom\" style=\"border: 0;display: block;height: auto;width: 100%;max-width: 300px;\" alt=\"facebook\" width=\"200\" src=\"http://i5.createsend1.com/ei/y/09/911/61F/085040/csfinal/social-fb.png\"></a>\n" +
                "        </div>\n" +
                "      \n" +
                "            </div>\n" +
                "          <!--[if (mso)|(IE)]></td><td style=\"width: 200px\" valign=\"top\" class=\"w160\"><![endif]-->\n" +
                "            <div class=\"column\" style=\"Float: left;max-width: 320px;min-width: 200px; width: 320px;width: calc(72200px - 12000%);text-align: left;color: #717a8a;font-size: 14px;line-height: 21px;font-family: Roboto,sans-serif;\">\n" +
                "            \n" +
                "        <div style=\"font-size: 12px;font-style: normal;font-weight: normal;\" align=\"center\">\n" +
                "          <a style=\"text-decoration: underline;transition: opacity 0.1s ease-in;color: #509cf6;\" href=\"https://campaignmonitor.createsend1.com/t/y-l-hituxy-tlhdxtjb-m/\"><img class=\"gnd-corner-image gnd-corner-image-center gnd-corner-image-top gnd-corner-image-bottom\" style=\"border: 0;display: block;height: auto;width: 100%;max-width: 300px;\" alt=\"linkedin\" width=\"200\" src=\"http://i6.createsend1.com/ei/y/09/911/61F/085040/csfinal/social-li.png\"></a>\n" +
                "        </div>\n" +
                "      \n" +
                "            </div>\n" +
                "          <!--[if (mso)|(IE)]></td><td class=\"layout__edges\">&nbsp;</td></tr></table><![endif]-->\n" +
                "          </div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "  \n" +
                "      <div style=\"line-height:45px;font-size:45px;\">&nbsp;</div>\n" +
                "  \n" +
                "      \n" +
                "      <div role=\"contentinfo\">\n" +
                "        <div class=\"layout email-footer\" style=\"Margin: 0 auto;max-width: 600px;min-width: 320px; width: 320px;width: calc(28000% - 167400px);overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;\">\n" +
                "          <div class=\"layout__inner\" style=\"border-collapse: collapse;display: table;width: 100%;\">\n" +
                "          <!--[if (mso)|(IE)]><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"><tr class=\"layout-email-footer\"><td style=\"width: 400px;\" valign=\"top\" class=\"w360\"><![endif]-->\n" +
                "            <div class=\"column wide\" style=\"text-align: left;font-size: 12px;line-height: 19px;color: #8e959c;font-family: Roboto,sans-serif;Float: left;max-width: 400px;min-width: 320px; width: 320px;width: calc(8000% - 47600px);\">\n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;Margin-top: 10px;Margin-bottom: 10px;\">\n" +
                "                \n" +
                "                <div style=\"font-size: 12px;line-height: 19px;\">\n" +
                "                  <div>We're Campaign Monitor<br>\n" +
                "631 Howard St, San Francisco, CA 94105</div>\n" +
                "                </div>\n" +
                "                <div style=\"font-size: 12px;line-height: 19px;Margin-top: 18px;\">\n" +
                "                  <div>You&#8217;re receiving this because you subscribe to the Campaign Monitor newsletter.</div>\n" +
                "                </div>\n" +
                "              </div>\n" +
                "            </div>\n" +
                "          <!--[if (mso)|(IE)]></td><td style=\"width: 200px;\" valign=\"top\" class=\"w160\"><![endif]-->\n" +
                "            <div class=\"column narrow\" style=\"text-align: left;font-size: 12px;line-height: 19px;color: #8e959c;font-family: Roboto,sans-serif;Float: left;max-width: 320px;min-width: 200px; width: 320px;width: calc(72200px - 12000%);\">\n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;Margin-top: 10px;Margin-bottom: 10px;\">\n" +
                "                \n" +
                "              </div>\n" +
                "            </div>\n" +
                "          <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "          </div>\n" +
                "        </div>\n" +
                "        <div class=\"layout one-col email-footer\" style=\"Margin: 0 auto;max-width: 600px;min-width: 320px; width: 320px;width: calc(28000% - 167400px);overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;\">\n" +
                "          <div class=\"layout__inner\" style=\"border-collapse: collapse;display: table;width: 100%;\">\n" +
                "          <!--[if (mso)|(IE)]><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"><tr class=\"layout-email-footer\"><td style=\"width: 600px;\" class=\"w560\"><![endif]-->\n" +
                "            <div class=\"column\" style=\"text-align: left;font-size: 12px;line-height: 19px;color: #8e959c;font-family: Roboto,sans-serif;max-width: 600px;min-width: 320px; width: 320px;width: calc(28000% - 167400px);\">\n" +
                "              <div style=\"Margin-left: 20px;Margin-right: 20px;Margin-top: 10px;Margin-bottom: 10px;\">\n" +
                "                <div style=\"font-size: 12px;line-height: 19px;\">\n" +
                "                  <a style=\"text-decoration: underline;transition: opacity 0.1s ease-in;color: #8e959c;\" href=\"https://campaignmonitor.createsend1.com/t/y-u-hituxy-tlhdxtjb-f/\">Unsubscribe</a>\n" +
                "                </div>\n" +
                "              </div>\n" +
                "            </div>\n" +
                "          <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "          </div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "      <div style=\"line-height:40px;font-size:40px;\">&nbsp;</div>\n" +
                "    </div></td></tr></tbody></table>\n" +
                "  <img style=\"overflow: hidden;position: fixed;visibility: hidden !important;display: block !important;height: 1px !important;width: 1px !important;border: 0 !important;margin: 0 !important;padding: 0 !important;\" src=\"http://campaignmonitor.createsend1.com/t/y-o-hituxy-tlhdxtjb/o.gif\" width=\"1\" height=\"1\" border=\"0\" alt=\"\">\n" +
                "</body></html>";
//        mailTemplate.sendHtmlMail("dqcer@sina.com", "subject", s);
    }

    @Test
    void testSendMailService() {
        MailClientDTO dto = new MailClientDTO()
                .setTo("dqcer@sina.com").setSubject("subject").setContent("hello word:     content");
        Result<Boolean> result = mailService.send(dto);
        Assertions.assertTrue(result.getData());
    }
}
