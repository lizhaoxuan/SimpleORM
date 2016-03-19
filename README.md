#SimpleORM

simpleORM 是一个android平台基于Sqlite 数据库的 ORM DB框架。

一直对orm的实现原理懵懵懂懂，觉得很难很高大上啊，在学习java反射机制时突然看到orm的实现可能，于是决定自己造个轮子。

**simpleORM的实现利用了java反射机制，同时因为反射的效率问题，所以做了些缓存优化。**

为什么叫simple呢，因为它真的很简单，只有5个类 + 一个接口。性能也只做了简单的优化，虽然还没有测试，但必然要差GreenDao这样的框架一大截。所以SimpleORM的意义更多在于学习和见借。

##用法

废话不多，先介绍用法。
simpleORM 提供了外部接口 IDataSupport ，IDataSupport有基本的增删改查方法。

	private IDataSupport dataSupport;
	
	dataSupport=DataSupport.getInstance(OrmApplication.getInstance());
	
	dataSupport.insertEntity(sv);
	
	dataSupport.getAllEntity(StudentValue.class)

DataSupport是单例，建议context传入Application。避免activity被静态引用无法销毁。


    
##限制
- simpleORM 不支持表链接。考虑android平台数据库的需求和性能问题，建议所有数据单表查询。

- simpleORM只支持String int double boolean 四种数据类型，不支持表链接，所以避免实体类嵌套。数组、List等数据类型建议用字符串形式存储，下一个版本SimpleORM将增加数组、List类型存储（原理：转为String,逗号分隔）。

- 实体类中，需要存储到数据库的属性，需要有setter方法。

- 实体类名、属性名应为驼峰体。数据库表名、字段名应为小写下划线体。下一版本simpleORM将自动创建数据库表。

