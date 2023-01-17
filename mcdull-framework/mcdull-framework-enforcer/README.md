# mcdull

#### 自动化测试组件

> 用于检查 Java 代码的体系结构，命名，层级调用是否满足规约

##### 快速开始

- 导入maven

```xml
        <dependency>
            <groupId>io.gitee.dqcer</groupId>
            <artifactId>mcdull-framework-enforcer</artifactId>
            <scope>test</scope>
        </dependency>
```

- 编码

```java

    private JavaClasses classes;

    @BeforeEach
    public void setUp() {

        classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_PACKAGE_INFOS)
                .importPackages("io.gitee");
    }

    @Test
    public void requiredRules() {
        for (ArchRule rule : ArchitectureEnforcer.requiredRules) {
            rule.check(classes);
        }
    }
```

##### 内置规约如下

##### 命名规则
    
1. mapper 层下的类应该以'Mapper'结尾
2. 实现DO的类名应该以'DO'结尾
3. 实现DTO的类名应该以'DTO'结尾
4. 实现VO的类名应该以'VO'结尾
5. 枚举类应该以'Enum'结尾
    

##### 层级调用

1. Mapper数据库访问层仅被Repository数据库包装层调用
2. Repository数据库包装层仅被Service业务层、Manager通用逻辑层调用
3. Manager数据库访问层仅被Service业务层调用
4. Service业务层仅被Controller层和ServerFeign层调用


- 异常处理反例

```java
// 反例
     throw new Exception(); 
     throw new RuntimeException("error"); 
     throw new Throwable("error"); 
class CustomException extends Throwable {
    
}

```

- 控制台打印反例

```java
System.out.println("foo");
System.err.println("bar");

OutputStream out = System.out; 
out.write(bytes)

try {
     // ...
} catch (Exception e) {
     e.printStackTrace(); 
 }
```






