package io.gitee.dqcer.mcdull.business.common.audit;

public class Demo {

    public static void main(String[] args) {
        Bean before = new Bean();
        before.setName("张三");
        before.setAge(18);
        before.setSex("男");
        Bean after = new Bean();
        after.setName("张三三");
        after.setAge(20);
        after.setSex("男");

        String s = AuditUtil.compareStr(before, after);
        System.out.println(s);
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

        @AuditDescription(label = "姓名")
        private String name;

        @AuditDescription(label = "年龄")
        private Integer age;

        @AuditDescription(label = "性别")
        private String sex;

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

