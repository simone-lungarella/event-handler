package it.os.event.handler.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableTransactionManagement
@Slf4j
public class PersistenceJPAConfig{

	@Autowired
	private DbCfg dbCFG;

	@Bean
	@Profile("dev")
	public DataSource generateDev() {
		log.info("Sono in dev");
		return buildDatasource(dbCFG.getUrl(),dbCFG.getUsername(),dbCFG.getPassword(),dbCFG.getDriver());
	}

	@Bean
	@Profile("!dev")
	public DataSource generateProd() {
		System.out.println("Sono in prod");
		return buildDatasourceProd(dbCFG.getUrl());
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
		log.info("Datasource dev");
		DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.url(url);
		dataSourceBuilder.username(username);
		dataSourceBuilder.password(pwd);
		dataSourceBuilder.driverClassName(driver);
		return dataSourceBuilder.build();
	}

	private HikariDataSource buildDatasourceProd(final String url) {
		log.info("Datasource prod");
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://b6bbe04e368f6f:00b611db@eu-cdbr-west-03.cleardb.net/heroku_f52c4e58b5c2566?reconnect=true");
		return new HikariDataSource(config);
	}

	@Bean
	@Profile("dev")
	public LocalContainerEntityManagerFactoryBean userEntityManagerDev() {
		log.info("Sono in dev");
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(generateDev());
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

	@Bean
	@Profile("!dev")
	public LocalContainerEntityManagerFactoryBean userEntityManagerProd() {
		log.info("Sono in prod");
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(generateProd());
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

	@Bean
	@Profile("dev")
	public PlatformTransactionManager userTransactionManagerDev() {
		log.info("Sono in dev");
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(userEntityManagerDev().getObject()); 
		return transactionManager;
	}

	@Bean
	@Profile("!dev")
	public PlatformTransactionManager userTransactionManagerProd() {
		log.info("Sono in prod");
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(userEntityManagerProd().getObject()); 
		return transactionManager;
	}

}
