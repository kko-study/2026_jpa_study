Hibernate:
select
    m1_0.id,
    m1_0.team_id,
    m1_0.username
from
    ch8_member m1_0
where
    m1_0.id=?
1) class = class chapter8.Team$HibernateProxy
2) class = class chapter8.Team$HibernateProxy

BUILD SUCCESSFUL in 1s
3 actionable tasks: 2 executed, 1 up-to-date
2월 09, 2026 11:52:41 오후 org.hibernate.jpa.internal.util.LogHelper logPersistenceUnitInformation
INFO: HHH008540: Processing PersistenceUnitInfo [name: hyun-persistence]
2월 09, 2026 11:52:42 오후 org.hibernate.Version logVersion
INFO: HHH000001: Hibernate ORM core version 7.2.1.Final
2월 09, 2026 11:52:42 오후 org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProvider configure
WARN: HHH10001002: Using built-in connection pool (not intended for production use)
2월 09, 2026 11:52:42 오후 org.hibernate.engine.jdbc.dialect.internal.DialectFactoryImpl constructDialect
WARN: HHH90000025: H2Dialect does not need to be specified explicitly using 'hibernate.dialect' (remove the property setting and it will be selected by default)
2월 09, 2026 11:52:42 오후 org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator logConnectionInfo
INFO: HHH10001005: Database info:
	Database JDBC URL [jdbc:h2:mem:test]
	Database driver: H2 JDBC Driver
	Database dialect: H2Dialect
	Database version: 2.4.240
	Default catalog/schema: TEST/PUBLIC
	Autocommit mode: false
	Isolation level: READ_COMMITTED
	JDBC fetch size: 100
	Pool: DriverManagerConnectionProvider
	Minimum pool size: 1
	Maximum pool size: 20
2월 09, 2026 11:52:42 오후 org.hibernate.engine.transaction.jta.platform.internal.JtaPlatformInitiator initiateService
INFO: HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
    org.hibernate.LazyInitializationException: Could not initialize proxy [chapter8.Team#10] - no session
