package io.gitee.dqcer.mcdull.business.common.audit;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

public class Demo {

    public static void main(String[] args) {
        Bean before = new Bean();
//        before.setName("张三");
        before.setAge(18);
        before.setSex("男");
        before.setCreatedTime(new Date());
        Bean after = new Bean();
        after.setName("张三三");
        after.setAge(20);
        after.setSex("女");
        after.setCreatedTime(DateUtil.offsetDay(new Date(), 1));

        String s = AuditUtil.compareStr(before, after);
        System.out.println(s);
        // 用户信息<性别:"男"更新为"女", 创建时间:"2023-11-08 13:59:00"更新为"2023-11-09 13:59:00", 年龄:"18"更新为"20", 姓名:""更新为"张三三">
    }

    public static class Bean implements Audit {

        @Override
        public String prefix() {
            return "用户信息";
        }

        @Override
        public String[] tagCharacter() {
            return new String[]{"<", ">"};
        }

        @AuditDescription(label = "姓名", sort = 2)
        private String name;

        @AuditDescription(label = "年龄", sort = 1)
        private Integer age;

        @AuditDescription(label = "性别")
        private String sex;

        @AuditDescription(label = "创建时间")
        private Date createdTime;

        public Date getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
}

