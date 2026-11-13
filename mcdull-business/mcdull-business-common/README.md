# mcdull-business-common

mcdull-business-common 是 Mcdull 项目的通用业务模块，提供了一系列常用的工具类和功能，用于支持业务层的开发。

## 功能特性

### 1. 文件处理工具

- **CsvUtil**：CSV 文件解析与导出工具
  - 支持解析 CSV 文件并进行模糊查询
  - 支持指定显示字段
  - 支持导出 CSV 文件到 HTTP 响应
  - 自动处理 BOM 头，防止 Excel 读取乱码

- **CustomMultipartFile**：自定义 MultipartFile 实现
  - 支持从字节数组创建 MultipartFile
  - 支持从输入流创建 MultipartFile
  - 提供与 Spring 标准 MultipartFile 兼容的接口

- **FileNameGeneratorUtil**：文件名生成工具
  - 支持生成带时间戳的文件名
  - 支持 Windows 风格的文件名冲突处理
  - 自动为重复文件名添加序号

### 2. 依赖管理

项目使用 Maven 管理依赖，主要依赖包括：

- **Hutool**：Java 工具类库，提供丰富的工具方法
- **Spring Boot**：提供 Spring 相关功能
- **iTextPDF**：PDF 生成与处理
- **EasyExcel**：Excel 文件处理
- **Apache POI**：Office 文档处理
- **MySQL Connector**：数据库连接

## 目录结构

```
mcdull-business-common/
├── src/
│   ├── main/
│   │   ├── java/io/gitee/dqcer/mcdull/business/common/
│   │   │   ├── CsvUtil.java
│   │   │   ├── CustomMultipartFile.java
│   │   │   └── FileNameGeneratorUtil.java
│   │   └── resources/
│   │       ├── fonts/          # 字体文件
│   │       ├── email_letter.html  # 邮件模板
│   │       ├── pdf.html           # PDF 模板
│   │       └── template.docx      # Word 模板
│   └── test/
│       ├── java/io/gitee/dqcer/mcdull/business/common/
│       │   └── CsvUtilTest.java   # CSV 工具测试
│       └── resources/             # 测试资源
└── pom.xml                        # Maven 配置文件
```

## 使用说明

### 1. CSV 文件处理

```java
// 解析 CSV 文件
List<List<String>> data = CsvUtil.parse(
    inputStream,       // 输入流
    searchMap,         // 模糊查询条件
    displayFields      // 要显示的字段
);

// 导出 CSV 文件到 HTTP 响应
CsvUtil.responseSetProperties(
    fileName,          // 文件名
    bytes,             // 文件内容
    response           // HTTP 响应对象
);
```

### 2. 自定义 MultipartFile

```java
// 从字节数组创建
MultipartFile file = new CustomMultipartFile(
    "file",            // 文件名
    content            // 文件内容字节数组
);

// 从输入流创建
MultipartFile file = new CustomMultipartFile(
    "file",            // 文件名
    inputStream        // 输入流
);
```

### 3. 文件名生成

```java
// 生成带时间戳的文件名
String fileName = FileNameGeneratorUtil.simple("报告");
// 输出: 报告-2024-12-01 12:00:00.xlsx

// 处理 Windows 风格的文件名冲突
String uniqueName = FileNameGeneratorUtil.windowsFileName(
    oldFileNameList,   // 已存在的文件名列表
    newTempFileName    // 新文件名
);
```

## 依赖配置

在 Maven 项目中添加以下依赖：

```xml
<dependency>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-business-common</artifactId>
    <version>${project.version}</version>
</dependency>
```

## 技术栈

- **Java 8+**
- **Maven 3.6+**
- **Spring Boot 2.x**
- **Hutool 5.x**
- **iTextPDF 7.x**
- **EasyExcel 3.x**
- **Apache POI 5.x**

