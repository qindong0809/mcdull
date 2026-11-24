# mcdull-business-common

`mcdull-business-common` 放置跨业务复用、但不适合下沉到 framework 的能力，例如审计、Excel 模板、PDF 转换、数据库结构导出、区域填充和用户引用解析。

按 `doc/架构分层与依赖治理规范.md` 的约定，本模块**不允许依赖任何 `*-facade` 或 `*-impl`**。当工具需要业务模型时，应在 framework-base 抽最小接口，而不是反向依赖业务契约。

## 功能特性

| 包 | 说明 |
|---|---|
| `common.audit` | 审计注解与审计工具 |
| `common.excel` | Excel 导入导出、动态模板与样式处理 |
| `common.pdf` | HTML 转 PDF、字体与页眉页脚处理 |
| `common.dump` | 数据库表/视图/函数/过程结构导出 |
| `common.mysql` | 数据库元数据与结构差异模型 |
| `common.area` | 区域数据填充工具 |
| `common.user` | 用户引用解析工具 |
| `common.CustomMultipartFile` | 基于字节数组或输入流的 `MultipartFile` 实现 |

## 目录结构

```text
mcdull-business-common/
├── src/
│   ├── main/
│   │   ├── java/io/gitee/dqcer/mcdull/common/
│   │   │   ├── CustomMultipartFile.java
│   │   │   ├── audit/
│   │   │   ├── area/
│   │   │   ├── dump/
│   │   │   ├── excel/
│   │   │   ├── mysql/
│   │   │   ├── pdf/
│   │   │   └── user/
│   │   └── resources/
│   │       ├── fonts/
│   │       ├── email_letter.html
│   │       ├── pdf.html
│   │       └── template.docx
│   └── test/
│       └── java/io/gitee/dqcer/mcdull/business/common/pdf/
│           └── HtmlConvertPdfTest.java
└── pom.xml
```

## 使用说明

### 自定义 MultipartFile

```java
MultipartFile file = new CustomMultipartFile("file", bytes);
MultipartFile fileFromStream = new CustomMultipartFile("file", inputStream);
```

完整构造器还支持显式指定 `originalFilename`、`contentType`：

```java
new CustomMultipartFile(name, originalFilename, contentType, bytes);
```

### HTML 转 PDF

```java
ByteArrayInOutConvert pdf = new HtmlConvertPdf().generatePdf(htmlStr);
```

字体和模板资源位于 `src/main/resources/fonts/` 与 `pdf.html`。当前 `HtmlConvertPdfTest` 因字体缺失时 `generatePdf` 会返回残缺 PDF，被 `@Disabled` 显式跳过，修复该缺陷后应移除该注解。

## 依赖配置

```xml
<dependency>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-business-common</artifactId>
    <version>${project.version}</version>
</dependency>
```

## 技术栈

- Java 17
- Spring Boot 3.5.x
- Hutool 5.8.x
- EasyExcel 4.0.x
- Apache POI 4.1.x
- iText 7.2.x
