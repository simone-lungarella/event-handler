package it.os.event.handler.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class PersistenceJPAConfig{

	@Autowired
	private DbCfg dbCFG;
	
	@Bean
	public DataSource generateDS() { 
		return buildDatasource(dbCFG.getUrl(),dbCFG.getUsername(),dbCFG.getPassword(),dbCFG.getDriver());
	}

	/**
	 * Builds the Datasources using configuration params.
	 * 
	 * @param url      URL of DataSource.
	 * @param username Username of Database.
	 * @param pwd      Password to access Database.
	 * @param driver   Driver of database to use for setting up the DataSource.
	 * @return A usable DataSource.
	 */
	private DataSource buildDatasource(final String url, final String username, final String pwd, final String driver) {
		DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.url(url);
		dataSourceBuilder.username(username);
		dataSourceBuilder.password(pwd);
		dataSourceBuilder.driverClassName(driver);
		return dataSourceBuilder.build();
	}

	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean userEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(generateDS());
		em.setPackagesToScan("it.os.event.handler.entity");
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setShowSql(true);
		em.setJpaVendorAdapter(vendorAdapter);
		Map<String, Object> properties = new HashMap<>(); 

		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL55Dialect");
		properties.put("hibernate.hbm2ddl.auto", "update");
		em.setJpaPropertyMap(properties);
		return em;
	} 

	@Primary
	@Bean
	public PlatformTransactionManager userTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(userEntityManager().getObject()); 
		return transactionManager;
	}

}
