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

##建议

同GreenDao类似，实体类应为很干净单纯的实体类。（只有要存储的属性、及其getter,setter方法）。但同时应用中，我们除了这些基本的属性还因为业务需求需要其他方法及属性怎么办呢？


这里介绍一个设计方法。举例：一个纪念日类：

	public class MemorialDayEntity {

    	protected int id;
    	protected String title;
    	protected String datetime;
    	protected String buildName;
    	protected String buildAccount;
    	protected boolean loop;
		
		public MemorialDayEntity(){}
		
		public MemorialDayEntity(String buildAccount, String buildName, String datetime, int id, boolean loop, String title) {
        this.buildAccount = buildAccount;
        this.buildName = buildName;
        this.datetime = datetime;
        this.id = id;
        this.loop = loop;
        this.title = title;
    }
		
		/**
		* getter and setter
		*/

    }
    
    
    public class MemorialDayDto extends MemorialDayEntity {
    	private static final DateFormat DATE_FORMAT = SimpleDateFormat.getDateInstance();
    	private Calendar calendar = GregorianCalendar.getInstance();

    	private Date nowDate;
    	private Date targetDate;
    	private int dateYear;
    	private int dateMonth;
    	private int dateDay;
    	private String nameStr;
    	private long day;
    	
    	/**
    	* 避免父类强转子类报错
    	*/
    	public MemorialDayDto(MemorialDayEntity entity){
    		super(entity.getBuildAccount , ...);
    	}
    	
    	public MemorialDayDto(String buildAccount, String buildName, String datetime, int id, boolean loop, String title) {
        super(buildAccount, buildName, datetime, id, loop, title);
        try {
            nowDate = new Date();
            targetDate = DATE_FORMAT.parse(datetime);
            calendar.setTime(targetDate);
            dateYear = calendar.get(Calendar.YEAR);
            dateMonth = calendar.get(Calendar.MONTH);
            dateDay = calendar.get(Calendar.DATE);
            calculateDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    }

MemorialDayEntity 是实体类，用于存储到数据库，MemorialDayDto在业务中使用，继承于MemorialDayEntity有实体类的所有数据和gettter，setter方法，也有用于业务的属性和方法。
最后为了避免父类转子类冲突，我们增加了一个构造方法：

	public MemorialDayDto(MemorialDayEntity entity){
    	//建议这样，比较getter setter是有些微性能损耗的
    	super(entity.getBuildAccount(), ...);
    	//如果你不想给实体类增加多余的构造方法也可以这样
    	//super.setBuildAccount(entity.getBuildAccount());
    	...
    }
