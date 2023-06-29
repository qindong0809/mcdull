package io.gitee.dqcer.mcdull.business.common.jd;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.math.RoundingMode;
import java.util.UUID;

public class Demo {

    public static int error = 0;

    public static void main(String[] args) throws InterruptedException {

        int number = 0;
        while (true) {
            Thread.sleep(Convert.toInt(RandomUtil.randomDouble(2D, 3D, 2, RoundingMode.UP) * 1000));
            number = extracted(number);
            System.out.println("success: " + number + ", error: " + error);
        }
    }

    private static int extracted(int number) {

        String url = "https://api.m.jd.com/?appid=pc-item-soa&functionId=pc_detailpage_wareBusiness&client=pc&clientVersion=1.0.0&t="+System.currentTimeMillis()+"&body=%7B%22skuId%22%3A10071442696062%2C%22cat%22%3A%22737%2C794%2C870%22%2C%22area%22%3A%2222_1930_50948_71603%22%2C%22shopId%22%3A%2212548140%22%2C%22venderId%22%3A13199193%2C%22paramJson%22%3A%22%7B%5C%22platform2%5C%22%3A%5C%221%5C%22%2C%5C%22colType%5C%22%3A0%2C%5C%22specialAttrStr%5C%22%3A%5C%22p0pppppppppppppppppppppp2p%5C%22%2C%5C%22skuMarkStr%5C%22%3A%5C%2210%5C%22%7D%22%2C%22num%22%3A1%2C%22bbTraffic%22%3A%22%22%7D&h5st=20230616142225674%3B3323787546171770%3Bfb5df%3Btk03wafde1bf818nKkLI0XhYUsdyj15TSk6nvoNjFPtfGTrdS0tDsB6tJJnZP3G7ButegLI3-XYSHHdKn3xy20cPbhc0%3Bc4361be28fed799ac6315e601b40bc44d425335f6e40114041cb0b5f6884e743%3B3.1%3B1686896545674%3B24c9ee85e67cf80746dd82817ecbeafc7a829b35c7f446a4c7d476cc9faa1d8834a93323ad7bce9bef1bba682b93d2e355d6e8e5dd59f86cf6366b81c51e383a69ca80f45cf27038cb4a8abfaaa4c6a75276a524878f3078836cd6b5686f3cfa&x-api-eid-token=jdd033JLKSA37EUZ7TU5XDJPLXLDLS5WPAJWI5CIVLTCZOLFVDWNAYSFMZRS7Z54ZA5K2LPPL5TSY7ECY5SCQUAISKWHSYQAAAAMIYLPJF4YAAAAADKRQH33XTGQCH4X&loginType=3&uuid=122270672.1458852648.1669512833.1680225740.1686894989.11";
//        url = "https://api.m.jd.com/?appid=pc-item-soa&functionId=pc_detailpage_wareBusiness&client=pc&clientVersion=1.0.0&t=1686925917379&body={\"skuId\":10070113578968,\"cat\":\"737,794,870\",\"area\":\"22_1930_50948_71603\",\"shopId\":\"12548140\",\"venderId\":13199193,\"paramJson\":\"{\\\"platform2\\\":\\\"1\\\",\\\"colType\\\":0,\\\"specialAttrStr\\\":\\\"p0ppppppppppppppppppppppp\\\",\\\"skuMarkStr\\\":\\\"01\\\"}\",\"num\":1,\"bbTraffic\":\"\"}&h5st=20230616223157437;7740991119928637;fb5df;tk03wb2fc1cc418n1zsg1GTmug0WA1NvoZr0JVhKeLIPxAbUywqYb380ED8AseBijXhunUcKs-KtTKjNu8JuqnW3bcxP;e698210d83289cb76a0d649713211997719f9456253a2b9787833e0271b159ed;3.1;1686925917437;24c9ee85e67cf80746dd82817ecbeafc7a829b35c7f446a4c7d476cc9faa1d88ac5c6f8bd926f3a1ad74112d65db142235ee329568bfb20afef8cfc37e867ae04682aafbcf5a7b4285feb5c96c7c5669bf05a2b573946fc565e2b1d31d02b52b&x-api-eid-token=jdd03MWFQCLVDNMRBNOFKSKLDQQRGUABQUBYWAIRROMLI5622DWD7TTEOGKKRH3GC6BJDU5A3HDNYNSTCIQX75EX5IPI7EEAAAAMIYSPCTYAAAAAACNNWKRE3AK3Z2YX&loginType=3&uuid=122270672.168689817956724347058.1686898180.1686901752.1686925402.3";
//        String s = HttpUtil.get(url);
//        System.out.println(s);
        String uuid = "4aa024f31ba96fa59cfe0bb80ca50654";
        uuid = UUID.randomUUID().toString();
        HttpResponse execute = HttpRequest.get(url)
                .header(Header.ORIGIN, "https://item.jd.com")
                .header(Header.REFERER, "https://item.jd.com/")

                .header(Header.COOKIE, "token=" + uuid + ",2,937165; __tk=JiI1JckikzIzIvjEIzSEIibhkUn1kDfijstxJUjxIiezJshokUeEjn,2,937165; jsavif=1; shshshfpa=09179e30-b862-c09b-9459-029dbc702b52-1686898179; shshshfpx=09179e30-b862-c09b-9459-029dbc702b52-1686898179; shshshsID=9d0a6e76b828befb86616f3624b67c12_2_1686898192390; __jda=122270672." + System.currentTimeMillis() + "24347058.1686898180.1686898180.1686898180.1; __jdb=122270672.2." + System.currentTimeMillis() + "24347058|1.1686898180; __jdc=122270672; __jdv=122270672|direct|-|none|-|" + System.currentTimeMillis() + "; 3AB9D23F7A4B3CSS=jdd03MWFQCLVDNMRBNOFKSKLDQQRGUABQUBYWAIRROMLI5622DWD7TTEOGKKRH3GC6BJDU5A3HDNYNSTCIQX75EX5IPI7EEAAAAMIYL3YENIAAAAAD3PUDOJ2F5DPAQX; 3AB9D23F7A4B3C9B=MWFQCLVDNMRBNOFKSKLDQQRGUABQUBYWAIRROMLI5622DWD7TTEOGKKRH3GC6BJDU5A3HDNYNSTCIQX75EX5IPI7EE; _gia_d=1; __jdu=" + System.currentTimeMillis() + "24347058; shshshfpb=vOqACuued8cVHh9gpXFLNIQ; areaId=22; ipLoc-djd=22-1930-50948-71603")
                .header(Header.ACCEPT, "application/json, text/javascript, */*; q=0.01")
                .header(Header.HOST, "api.m.jd.com")
                .header("TE", "trailers")
                .header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:106.0) Gecko/20100101 Firefox/106.0")
                .header("x-referer-page", "https://item.jd.com/10071442696062.html")
                .header("x-rp-client", "h5_1.0.0")
                .execute();
        int status = execute.getStatus();
        if (status != 200) {
            error ++ ;
            return number;
        }
        String body = execute.body();

        boolean containsWu = body.contains("<strong>无货</strong>，此商品暂时售完");
        boolean contains = body.contains("<strong>有货</strong>");
        if (contains || containsWu) {
            number ++;
//            sendEmil();
        }
//        System.out.println(body);
        JSONConfig config = new JSONConfig();
        JSONObject obj = JSONUtil.parseObj(body, config);
        Object stockInfo = obj.get("stockInfo");
        JSONObject entries = JSONUtil.parseObj(stockInfo);
        Object stockDesc = entries.get("stockDesc");
        JSONObject object = JSONUtil.parseObj(stockDesc);
//        System.out.println(object);
        return number;
    }

    public static void sendEmil() {
        MailAccount account = new MailAccount();
        account.setUser("dqcerx@sina.com");
        account.setFrom("dqcerx@sina.com");
        account.setHost("smtp.sina.com");
        account.setPort(465);
        account.setPass("xxx");
        account.setSslEnable(true);
        account.setAuth(true);
        MailUtil.send(account, "dqcerx@sina.com", "test","good job", false);
    }

}
